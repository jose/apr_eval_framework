import csv
import os
import shutil
import subprocess
import json
from sets import Set

from config import REPAIR_ROOT, JAVA8_HOME, MAVEN_BIN, BENCHMARK_METADATA_DIR, TEST_TIMEOUT
from core.Benchmark import Benchmark
from core.Bug import Bug
from core.utils import add_benchmark
from core.utils import run_cmd

MVN_FLAGS = """-V -B -Dhttps.protocols=TLSv1.2 --offline \
  -Denforcer.skip=true \
  -Dcheckstyle.skip=true \
  -Dcobertura.skip=true \
  -Djacoco.skip=true \
  -Drat.skip=true \
  -Drat.ignoreErrors=true \
  -Drat.numUnapprovedLicenses=1000000 \
  -Dlicense.skip=true \
  -Dfindbugs.skip=true \
  -Dpmd.skip=true \
  -Dgpg.skip=true \
  -Dskip.npm=true \
  -Dskip.gulp=true \
  -Dskip.bower=true \
  -Dbaseline.skip=true \
  -Dmaven.javadoc.skip=true \
  -Danimal.sniffer.skip=true \
  -Dsort.skip=true \
"""

MVN_DEPS_ROOT_DIR = os.path.join(REPAIR_ROOT, "mvn_deps")

def abs_to_rel(root, folders):
    if root[-1] != '/':
        root += "/"
    output = []
    for folder in folders:
        output.append(folder.replace(root, ""))
    return output


class BugDotJar(Benchmark):
    """Bug_dot_jar Benchmark"""

    def __init__(self):
        super(BugDotJar, self).__init__("Bugs.jar")
        self.path = os.path.join(REPAIR_ROOT, "benchmarks", "Bug-dot-jar")
        self.bugs = None
        self.get_bugs()

        # Cache modules
        with open(os.path.join(REPAIR_ROOT, "data", "benchmarks", "BugsDotJar-modules.txt"), mode='r') as fin:
            reader = csv.reader(fin)
            # TODO it assumes a bug id is unique per project, which is the case
            # as of 08/15/2019
            self.modules = dict((rows[1], rows[2]) for rows in reader) # Project,Bug,Module

    def get_bug(self, bug_id):
        separator = "-"
        if "_" in bug_id:
            separator = "_"
        split = bug_id.split(separator)
        commit = split[-1]
        project = "-".join(split[:-1])
        for bug in self.get_bugs():
            if project.lower() in bug.project.lower():
                if bug.bug_id[:8].lower() == commit[:8]:
                    return bug
        return None

    def get_bugs(self):
        if self.bugs is not None:
            return self.bugs
        self.bugs = []
        dataset_path = os.path.join(self.path, "data")
        for project in os.listdir(dataset_path):
            project_path = os.path.join(dataset_path, project)
            if os.path.isfile(project_path):
                continue
            for commit in os.listdir(project_path):
                commit_path = os.path.join(project_path, commit)
                if not os.path.isfile(commit_path) or commit[-5:] != ".json":
                    continue
                bug = Bug(self, project.title(), commit[:-5])
                self.bugs += [bug]
        return self.bugs

    def checkout(self, bug, working_directory, rm_tests=True, buggy_version=True):
        dataset_path = os.path.join(self.path, "data", bug.project.lower(), "%s.json" % bug.bug_id[:8])
        with open(dataset_path) as fd:
            data = json.load(fd)
            project = bug.project.split("-")[-1].upper()
            if project == "MAVEN":
                project = "MNG"
            branch_id = "bugs-dot-jar_%s-%s_%s" % (project, data['jira_id'], data['commit'])
            
            cmd = "cd " + os.path.join(self.path, "repositories",
                                       bug.project.lower()) + "; git reset .; git checkout -- .; git clean -x -d --force; git checkout " + branch_id
            subprocess.check_output(cmd, shell=True)
            shutil.copytree(os.path.join(self.path, "repositories", bug.project.lower()), working_directory)

            # Remove any repository directories in the working directory and
            # init a new repository with an empty commit -- the latter is
            # important for some projects that check the HEAD pointer.
            cmd = "cd %s && rm -rf .git* .svn && git init && git commit -m 'init' --allow-empty;" %(working_directory)
            subprocess.check_output(cmd, shell=True)

            if not buggy_version:
                # To checkout the fixed version, apply the patch proposed by the
                # developers to the buggy version
                patch_file = os.path.join(BENCHMARK_METADATA_DIR, str(self.name), str(bug.project), str(bug.bug_id), "developer-patch.diff")
                assert os.path.isfile(patch_file),"Developer's patch does not exist in the .bugs-dot-jar directory!"
                cmd = "cd " + working_directory + " && git apply " + patch_file
                subprocess.check_output(cmd, shell=True)

            # Copy over cached dependencies
            if os.path.isdir(MVN_DEPS_ROOT_DIR):
                # Create local .m2 directory
                cmd = "mkdir -p %s/.m2" %(working_directory)
                subprocess.check_output(cmd, shell=True)

                # Copy over project's mvn dependencies
                if os.path.isdir(os.path.join(MVN_DEPS_ROOT_DIR, bug.project)) and len(os.listdir(os.path.join(MVN_DEPS_ROOT_DIR, bug.project))) > 0:
                    cmd = "cp -a %s/* %s/.m2/" %(os.path.join(MVN_DEPS_ROOT_DIR, bug.project), working_directory)
                    subprocess.check_output(cmd, shell=True)

                # Copy over the extra dependencies
                if os.path.isdir(os.path.join(MVN_DEPS_ROOT_DIR, "extra")):
                    cmd = "cp -an %s/* %s/.m2/" %(os.path.join(MVN_DEPS_ROOT_DIR, "extra"), working_directory)
                    subprocess.check_output(cmd, shell=True)

                # Copy over the project-info-maven-plugin dependency
                if os.path.isdir(os.path.join(MVN_DEPS_ROOT_DIR, "plugin")):
                    cmd = "cp -an %s/* %s/.m2/" %(os.path.join(MVN_DEPS_ROOT_DIR, "plugin"), working_directory)
                    subprocess.check_output(cmd, shell=True)

            if str(bug.project) == "Wicket" and str(bug.bug_id) == "087c0a26":
                # Modify the access of NO_PAGE_ID from private to protected to
                # avoid the NO_PAGE_ID has private access in org.apache.wicket.protocol.ws.api.AbstractWebSocketProcessor error
                cmd = """
                sed -i '79s|private |protected |' %s/wicket-native-websocket/wicket-native-websocket-core/src/main/java/org/apache/wicket/protocol/ws/api/AbstractWebSocketProcessor.java;
                """ % (working_directory)
                subprocess.check_output(cmd, shell=True)
            elif str(bug.project) == "Jackrabbit-Oak" and str(bug.bug_id) == "24ce6788":
                # Remove the following goal from the main pom.xml file
                #  <goals>
                #    <goal>scr</goal>
                #  </goals>
                # which leads to "Comparison method violates its general contract" error
                cmd = """
                sed -i '93,95s|.*||g' %s/oak-parent/pom.xml;
                """ % (working_directory)
                subprocess.check_output(cmd, shell=True)

            if rm_tests:
                # Remove known flaky tests
                if bug.rm_tests() != 0:
                    return 1

        return 0

    def compile(self, bug, working_directory, java_home_dir=None):
        if java_home_dir == None:
            java_home_dir = JAVA8_HOME

        export_cmd = "export JAVA_HOME=\"%s\"; \
                      export PATH=\"$JAVA_HOME/bin:%s:$PATH\"; \
                      export _JAVA_OPTIONS=-Djdk.net.URLClassPath.disableClassPathURLCheck=true; \
                      export MAVEN_OPTS=\"-Xmx4g -Xms1g -XX:MaxPermSize=512m\"" % (java_home_dir, MAVEN_BIN)

        failing_module_dir = self.failing_module(bug)
        failing_module_full_path = os.path.abspath(os.path.join(working_directory, failing_module_dir))
        assert os.path.isdir(failing_module_full_path),"Directory '" + failing_module_full_path + "' does not exist!"
        dirs_in_the_failing_module_path = failing_module_dir.split('/')

        #
        # The compilation of each Bugs.jar bug (for example,
        # flink-staging/flink-streaming/flink-streaming-core) is a three step
        # process:
        # 1. If there is a pom.xml in the root directory, run mvn on it.
        # 2. Run mvn on all pom.xml file from the root directory to the
        #    directory of the buggy module (except the later).
        # 3. Run mvn on the pom.xml of the buggy module.
        #

        log_file = file(os.path.join(working_directory, "repair_them_all.compile.log"), 'w')

        # 1. Compile root pom.xml
        pom_xml_file = os.path.join(working_directory, "pom.xml")
        if os.path.isfile(pom_xml_file):
            cmd = """
            %s; cd %s; mvn clean install -DskipTests -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 --fail-never %s;
            """ % (export_cmd, working_directory, working_directory, MVN_FLAGS)
            run_cmd(cmd, log_file, log_file)

        # 2. Compile every single directory in the path of the buggy module, if
        # there is a pom.xml file
        path = os.path.join(working_directory)
        for i in range(len(dirs_in_the_failing_module_path) - 1):
            path = os.path.join(path, dirs_in_the_failing_module_path[i])
            pom_xml_file = os.path.join(path, "pom.xml")
            if os.path.isfile(pom_xml_file):
                cmd = """
                %s; cd %s; mvn install -DskipTests -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 --fail-never %s;
                """ % (export_cmd, path, working_directory, MVN_FLAGS)
                run_cmd(cmd, log_file, log_file)

        # 3. Compile the buggy module and return the exit code of that operation
        path = os.path.join(path, dirs_in_the_failing_module_path[-1])
        cmd = """
        %s; cd %s; mvn install -DskipTests -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 %s;
        """ % (export_cmd, path, working_directory, MVN_FLAGS)
        return run_cmd(cmd, log_file, log_file)

    def init_test(self, bug, working_directory, java_home_dir=None):
        if java_home_dir == None:
            java_home_dir = JAVA8_HOME
        work_dir = os.path.join(working_directory, self.failing_module(bug))
        cmd = """
        export JAVA_HOME="%s";
        export PATH="$JAVA_HOME/bin:%s:$PATH";
        export _JAVA_OPTIONS=-Djdk.net.URLClassPath.disableClassPathURLCheck=true;
        export MAVEN_OPTS="-Xmx4g -Xms1g -XX:MaxPermSize=512m";
        cd %s;
        timeout -s KILL %s mvn test -DskipTests -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 --fail-never %s;
        """ % (java_home_dir, MAVEN_BIN, work_dir, TEST_TIMEOUT, working_directory, MVN_FLAGS)
        log_file = file(os.path.join(working_directory, "repair_them_all.init_test.log"), 'w')
        return run_cmd(cmd, log_file, log_file)

    def run_test(self, bug, working_directory, java_home_dir=None):
        if java_home_dir == None:
            java_home_dir = JAVA8_HOME
        work_dir = os.path.join(working_directory, self.failing_module(bug))
        cmd = """cd %s; 
        export JAVA_HOME="%s";
        export PATH="$JAVA_HOME/bin:%s:$PATH";
        export _JAVA_OPTIONS=-Djdk.net.URLClassPath.disableClassPathURLCheck=true;
        export MAVEN_OPTS="-Xmx4g -Xms1g -XX:MaxPermSize=512m";
        timeout -s KILL %s mvn test -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 --fail-at-end %s;
        """ % (work_dir, java_home_dir, MAVEN_BIN, TEST_TIMEOUT, working_directory, MVN_FLAGS)
        log_file = file(os.path.join(working_directory, "repair_them_all.run_test.log"), 'w')
        return run_cmd(cmd, log_file, log_file)

    def failing_module(self, bug):
        return self.modules[bug.bug_id]

    def failing_tests(self, bug):
        dataset_path = os.path.join(self.path, "data", bug.project.lower(), "%s.json" % bug.bug_id[:8])
        with open(dataset_path) as fd:
            data = json.load(fd)
            return data['failing_tests']

    def source_folders(self, bug):
        failing_module = self.failing_module(bug)
        info = self._get_project_info(bug, failing_module)
        assert len(info['modules']) == 1
        module = info['modules'][0]
        return abs_to_rel(bug.working_directory, module['sources'])

    def test_folders(self, bug):
        failing_module = self.failing_module(bug)
        info = self._get_project_info(bug, failing_module)
        assert len(info['modules']) == 1
        module = info['modules'][0]
        return abs_to_rel(bug.working_directory, module['tests'])

    def bin_folders(self, bug):
        failing_module = self.failing_module(bug)
        info = self._get_project_info(bug, failing_module)
        assert len(info['modules']) == 1
        module = info['modules'][0]
        return abs_to_rel(bug.working_directory, module['binSources'])

    def test_bin_folders(self, bug):
        failing_module = self.failing_module(bug)
        info = self._get_project_info(bug, failing_module)
        assert len(info['modules']) == 1
        module = info['modules'][0]
        return abs_to_rel(bug.working_directory, module['binTests'])

    def classpath(self, bug):
        failing_module = self.failing_module(bug)
        info = self._get_project_info(bug, failing_module)
        assert len(info['modules']) == 1
        module = info['modules'][0]
        return ":".join(module['classpath'])

    def compliance_level(self, bug):
        return 7

add_benchmark("Bugs.jar", BugDotJar)
