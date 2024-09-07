import json
import os
import re
import shutil
import subprocess

from config import REPAIR_ROOT, DATA_PATH, JAVA8_HOME, MAVEN_BIN, TEST_TIMEOUT
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
  -Dgpg.skip=true \
  -Dskip.npm=true \
  -Dskip.gulp=true \
  -Dskip.bower=true \
  -Dbaseline.skip=true \
  -Dmaven.javadoc.skip=true \
  -Danimal.sniffer.skip=true \
"""

MVN_DEPS_ROOT_DIR = os.path.join(REPAIR_ROOT, "mvn_deps")

BUGS_METADATA_FILE = os.path.abspath(os.path.join(DATA_PATH, "benchmarks", "bears", "bugs.json"))

def abs_to_rel(root, folders):
    if root[-1] != '/':
        root += "/"
    output = []
    for folder in folders:
        output.append(folder.replace(root, ""))
    return output

class Bears(Benchmark):
    """Bears Benchmark"""

    def __init__(self):
        super(Bears, self).__init__("Bears")
        self.path = os.path.join(REPAIR_ROOT, "benchmarks", "bears")
        self.bugs = None
        # Cache metadata
        assert os.path.isfile(BUGS_METADATA_FILE),"Bears' metadata file does not exist!"
        self.metadata = self._parse_metadata()
        # Collect all bugs
        self.get_bugs()

    def _parse_metadata(self):
        metadata = {}
        with open(BUGS_METADATA_FILE, mode='r') as fd:
            data = json.load(fd)
            for b in data:
                bug_id = str(b["builds"]["buggyBuild"]["id"])
                assert bug_id is not None
                fix_id = str(b["builds"]["fixerBuild"]["id"])
                assert fix_id is not None
                #
                # 1. Get full local path of the failing module
                # 2. Remove '/root/workspace/' from the full path of the failing module,
                #    as that path only existed on the computer of the people that
                #    collected Bears metadata
                # 3. Remove the bug id from the full path of the failing module, as
                #    there is no directory named bug id when the buggy code is checkout
                # 4. Replace '//' with '/', as the third step might have removed the bug
                #    id in the middle of the path
                # 5. Remove '/' at the very end of the path, if there is any
                #
                key = re.sub(r'/$', '', b["tests"]["failingModule"] \
                        .replace("/root/workspace/", '') \
                        .replace(bug_id, '') \
                        .replace("//", '/'))
                #
                # at this point, `key` is structured as:
                #
                # <organization>/<project>/<submodule, which can be a path composed with more than one directory>
                #
                # e.g.,
                # INRIA/spoon
                # CSU-CS414-WareWolves/cs414-f18-001-WareWolves
                # pippo-java/pippo/pippo-session-parent/pippo-session
                # societe-generale/ci-droid-tasks-consumer/ci-droid-tasks-consumer-services
                key_items    = key.split('/')
                assert len(key_items) >= 2
                organization = str(key_items[0])
                project      = str(key_items[1])
                organization_and_project = organization + "-" + project
                assert "/" not in organization_and_project

                buggy_module_expr = re.search("^"+organization+"\/"+project+"\/(.*)$", key)
                buggy_module = ""
                if buggy_module_expr:
                    buggy_module = str(buggy_module_expr.group(1))

                failing_tests = []
                for failingClass in b["tests"]["failingClasses"]:
                    failing_tests += [failingClass['testClass']]
                assert len(failing_tests) > 0

                metadata["%s-%s-%s" %(organization_and_project, bug_id, fix_id)] = {
                    "organization_and_project": organization_and_project,
                    "bug_id": bug_id,
                    "fix_id": fix_id,
                    "buggy_module": buggy_module,
                    "failing_tests": failing_tests
                }
        return metadata

    def get_bug(self, bug_id):
        # A bug_id is composed by projectName-buggyCommit-fixedCommit
        organization_project_expr = re.search('^(.*)-(\d+-\d+)$', bug_id)
        assert organization_project_expr
        organization_project = str(organization_project_expr.group(1))
        bug_id = str(organization_project_expr.group(2))

        for bug in self.get_bugs():
            if bug.project.lower() == organization_project.lower() and bug.bug_id.lower() == bug_id.lower():
                return bug
        raise Exception("No bug has been identified!")

    def get_bugs(self):
        if self.bugs is not None:
            return self.bugs

        self.bugs = []
        for key, value in self.metadata.items():
            organization_and_project = value['organization_and_project']
            bug_id = str(value['bug_id'])
            fix_id = str(value['fix_id'])
            self.bugs += [Bug(self, organization_and_project, "%s-%s" % (bug_id, fix_id))]
        assert len(self.bugs) > 0
        return self.bugs

    def _get_metadata(self, bug):
        for key, value in self.metadata.items():
            organization_and_project = value['organization_and_project'].lower()
            bug_id = str(value['bug_id'].lower())
            fix_id = str(value['fix_id'].lower())
            if bug.project.lower() == organization_and_project and str(bug.bug_id).lower() == "%s-%s" %(bug_id, fix_id):
                return value
        raise Exception("No metadata has been identified!")

    def failing_module(self, bug):
        return self._get_metadata(bug)['buggy_module']

    def failing_tests(self, bug):
        return self._get_metadata(bug)['failing_tests']

    def _get_project_info(self, bug):
        return super(Bears, self)._get_project_info(bug, self.failing_module(bug))

    def checkout(self, bug, working_directory, rm_tests=True, buggy_version=True):
        branch_id = "%s-%s" % (bug.project, bug.bug_id)

        cmd = "cd " + self.path + "; git reset .; git checkout -- .; git clean -x -d --force; git checkout -f " + branch_id
        subprocess.check_output(cmd, shell=True)

        # Find and checkout the buggy or fixed commit
        if buggy_version:
            cmd = "cd " + self.path + "; git log --format=format:%H --grep='Changes in the tests'"
            commit_hash = subprocess.check_output(cmd, shell=True)
            if len(commit_hash) == 0:
                cmd = "cd " + self.path + "; git log --format=format:%H --grep='Bug commit'"
                commit_hash = subprocess.check_output(cmd, shell=True)
        else:
            cmd = "cd " + self.path + "; git log --format=format:%H --grep='Human patch from'"
            commit_hash = subprocess.check_output(cmd, shell=True)
        assert len(commit_hash) != 0,"Commit hash is empty!"
        cmd = "cd " + self.path + "; git checkout -f " + commit_hash
        subprocess.check_output(cmd, shell=True)
        shutil.copytree(self.path, working_directory)

        cmd = "cd %s && rm -rf .git* .svn && git init && git commit -m 'init' --allow-empty;" %(working_directory)
        subprocess.check_output(cmd, shell=True)

        # Fix dependency's version.
        # Most INRIA's bugs depend on the com.github.stefanbirkner::system-rules (1.9.0)
        # plugin and on commons-io::commons-io (v1.4).  Given that com.github.stefanbirkner::system-rules (1.9.0)
        # also depends on commons-io::commons-io but on any version greater or equal than 2.0,
        # Apache Maven downloads the latest version of commons-io::commons-io which is 2.16.1
        # (as of today; September 3, 2024).  However, the source source code of most INRIA's
        # bugs is not compatible with commons-io::commons-io <= 2.8.0.  To allow the source code
        # to compile, we set the version of commons-io::commons-io to 2.5 (the common version
        # that works for all bugs).
        if str(bug.project) == "INRIA-spoon":
            cmd = """
            sed -i '242s|<version>1.4</version>|<version>2.5</version>|' %s/pom.xml;
            """ % (working_directory)
            subprocess.check_output(cmd, shell=True)
        elif str(bug.project) == "spring-projects-spring-data-commons":
            # Find parent pom's version
            cmd = """
            sed -ne '15s|.*<version>\\(.*\\)</version>.*|\\1|p' %s/pom.xml;
            """ % (working_directory)
            parent_snapshot_version = subprocess.check_output(cmd, shell=True).rstrip()
            # Replace the no longer available SNAPSHOT versions with RELEASE versions
            parent_release_version = ""
            if parent_snapshot_version == "1.7.7.BUILD-SNAPSHOT":
                parent_release_version = "1.8.0.RELEASE"
            elif parent_snapshot_version == "1.8.12.BUILD-SNAPSHOT":
                parent_release_version = "1.9.0.RELEASE"
            else:
                parent_release_version = parent_snapshot_version.replace("BUILD-SNAPSHOT", "RELEASE")
            cmd = """
            sed -i '15s|<version>%s</version>|<version>%s</version>|' %s/pom.xml;
            """ % (parent_snapshot_version, parent_release_version, working_directory)
            subprocess.check_output(cmd, shell=True)

            spring_data_parent_pom_file=os.path.join(MVN_DEPS_ROOT_DIR, str(bug.project), "org", "springframework", "data", "build", "spring-data-parent", parent_release_version, "spring-data-parent-" + parent_release_version + ".pom")
            if os.path.isfile(spring_data_parent_pom_file):
                # Remove lines:
                # <plugin>
                #         <groupId>com.springsource.bundlor</groupId>
                #         <artifactId>com.springsource.bundlor.maven</artifactId>
                #         <version>1.0.0.RELEASE</version>
                # </plugin>
                if parent_release_version == "1.8.0.RELEASE":
                    cmd = """
                    sed -i '645,649s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.0.RELEASE":
                    cmd = """
                    sed -i '707,711s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.5.RELEASE":
                    cmd = """
                    sed -i '732,736s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.7.RELEASE":
                    cmd = """
                    sed -i '732,736s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.9.RELEASE":
                    cmd = """
                    sed -i '725,729s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.10.RELEASE":
                    cmd = """
                    sed -i '755,759s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "2.0.0.RELEASE":
                    pass
                elif parent_release_version == "2.0.1.RELEASE":
                    pass
                elif parent_release_version == "2.2.0.RELEASE":
                    pass
                else:
                    raise Exception("Parent release version: " + str(parent_release_version) + " not supported!")
                subprocess.check_output(cmd, shell=True)

                # Remove lines:
                # <plugin>
                #         <groupId>com.springsource.bundlor</groupId>
                #         <artifactId>com.springsource.bundlor.maven</artifactId>
                #         <configuration>
                #                 <enabled>${bundlor.enabled}</enabled>
                #                 <failOnWarnings>${bundlor.failOnWarnings}</failOnWarnings>
                #         </configuration>
                #         <executions>
                #                 <execution>
                #                         <id>bundlor</id>
                #                         <goals>
                #                                 <goal>bundlor</goal>
                #                         </goals>
                #                 </execution>
                #         </executions>
                # </plugin>
                if parent_release_version == "1.8.0.RELEASE":
                    cmd = """
                    sed -i '769,784s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.0.RELEASE":
                    cmd = """
                    sed -i '839,854s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.5.RELEASE":
                    cmd = """
                    sed -i '864,879s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.7.RELEASE":
                    cmd = """
                    sed -i '864,879s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.9.RELEASE":
                    cmd = """
                    sed -i '857,872s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.10.RELEASE":
                    cmd = """
                    sed -i '893,908s|.*||g' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "2.0.0.RELEASE":
                    pass
                elif parent_release_version == "2.0.1.RELEASE":
                    pass
                elif parent_release_version == "2.2.0.RELEASE":
                    pass
                else:
                    raise Exception("Parent release version: " + str(parent_release_version) + " not supported!")
                subprocess.check_output(cmd, shell=True)

                # Downgrade com.mysema.querydsl:querydsl-[core|apt|collections] from
                # 4.1.0 (which is not available as of today; September 3, 2024)
                # to 3.7.4, in the parent pom
                if parent_release_version == "1.8.0.RELEASE":
                    cmd = """
                    sed -i '103s|<querydsl>4.1.0</querydsl>|<querydsl>3.7.4</querydsl>|' %s;
                    """ % (spring_data_parent_pom_file)
                elif parent_release_version == "1.9.0.RELEASE":
                    pass
                elif parent_release_version == "1.9.5.RELEASE":
                    pass
                elif parent_release_version == "1.9.7.RELEASE":
                    pass
                elif parent_release_version == "1.9.9.RELEASE":
                    pass
                elif parent_release_version == "1.9.10.RELEASE":
                    pass
                elif parent_release_version == "2.0.0.RELEASE":
                    pass
                elif parent_release_version == "2.0.1.RELEASE":
                    pass
                elif parent_release_version == "2.2.0.RELEASE":
                    pass
                else:
                    raise Exception("Parent release version: " + str(parent_release_version) + " not supported!")
                subprocess.check_output(cmd, shell=True)

                # Workaround fix for
                # Failed to execute goal org.apache.maven.plugins:maven-jar-plugin:3.0.2:jar (default-jar)
                # on project spring-data-commons: Error assembling JAR: Manifest file: target/classes/META-INF/MANIFEST.MF does not exist.
                #
                # Replace
                # <manifestFile>${manifest.file}</manifestFile>
                # with
                # <manifestEntries>
                #     <Implementation-Title>${project.name}</Implementation-Title>
                #     <Implementation-Version>${project.version}</Implementation-Version>
                #     <Automatic-Module-Name>${java-module-name}</Automatic-Module-Name>
                # </manifestEntries>
                cmd = """
                sed -i 's|<manifestFile>${manifest.file}</manifestFile>|<manifestEntries><Implementation-Title>${project.name}</Implementation-Title><Implementation-Version>${project.version}</Implementation-Version><Automatic-Module-Name>${java-module-name}</Automatic-Module-Name></manifestEntries>|' %s;
                """ % (spring_data_parent_pom_file)
                subprocess.check_output(cmd, shell=True)

                # Fix for bug 458393275-458823066.
                # Downgrade org.springframework.hateoas:spring-hateoas from 1.0.0.RELEASE
                # to 0.23.0.RELEASE.
                if str(bug.bug_id) == "458393275-458823066":
                    cmd = """
                    sed -i 's|<spring-hateoas>1.0.0.RELEASE</spring-hateoas>|<spring-hateoas>0.23.0.RELEASE</spring-hateoas>|' %s;
                    """ % (spring_data_parent_pom_file)
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
        # The compilation of each Bears bug is a three step process:
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
                %s; cd %s; mvn clean install -DskipTests -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 --fail-never %s;
                """ % (export_cmd, path, working_directory, MVN_FLAGS)
                run_cmd(cmd, log_file, log_file)

        # 3. Compile the buggy module and return the exit of that operation
        path = os.path.join(path, dirs_in_the_failing_module_path[-1])
        cmd = """
        %s; cd %s; mvn clean install -DskipTests -DskipUTs=true -DskipITs=true -Dmaven.repo.local=%s/.m2 %s;
        """ % (export_cmd, path, working_directory, MVN_FLAGS)
        return run_cmd(cmd, log_file, log_file)

    def run_test(self, bug, working_directory, java_home_dir=None):
        if java_home_dir == None:
            java_home_dir = JAVA8_HOME

        failing_module_dir = self.failing_module(bug)
        failing_module_full_path = os.path.abspath(os.path.join(working_directory, failing_module_dir))
        assert os.path.isdir(failing_module_full_path),"Directory '" + failing_module_full_path + "' does not exist!"

        skip_ut_it_tests = " -DskipUTs=true -DskipITs=true "
        if str(bug.project) == "molgenis-molgenis":
            if str(bug.bug_id) == "361210220-361993202" or str(bug.bug_id) == "362517584-362989296" or str(bug.bug_id) == "367638811-367645808" or str(bug.bug_id) == "369514407-370006117":
                skip_ut_it_tests = " -DskipUTs=false -DskipITs=true "

        cmd = """
        export JAVA_HOME="%s";
        export PATH="$JAVA_HOME/bin:%s:$PATH";
        export _JAVA_OPTIONS=-Djdk.net.URLClassPath.disableClassPathURLCheck=true;
        export MAVEN_OPTS="-Xmx4g -Xms1g -XX:MaxPermSize=512m";
        cd %s;
        timeout -s KILL %s mvn test %s -Dmaven.repo.local=%s/.m2 --fail-at-end %s;
        """ % (java_home_dir, MAVEN_BIN, failing_module_full_path, TEST_TIMEOUT, skip_ut_it_tests, working_directory, MVN_FLAGS)
        log_file = file(os.path.join(working_directory, "repair_them_all.run_test.log"), 'w')
        return run_cmd(cmd, log_file, log_file)

    def _get_mvn_metadata(self, bug, entry):
        info = self._get_project_info(bug)
        failing_module = self.failing_module(bug)

        for module in info['modules']:
            if module['baseDir'].endswith(failing_module):
                return module[entry]

        raise Exception("No " + entry + " has been identified!")

    def source_folders(self, bug):
        source_folders = self._get_mvn_metadata(bug, 'sources')
        return abs_to_rel(bug.working_directory, source_folders)

    def test_folders(self, bug):
        if bug.project == "openmrs-openmrs-module-htmlformentry" and bug.bug_id == "452596787-452623087":
            # The buggy module of the openmrs-openmrs-module-htmlformentry-452596787-452623087
            # bug from the Bears benchmark is named 'api', however the
            # triggering test cases are in the module named 'api-tests'
            return ["api-tests/src/test/java"]

        test_folders = self._get_mvn_metadata(bug, 'tests')
        return abs_to_rel(bug.working_directory, test_folders)

    def bin_folders(self, bug):
        bin_folders = self._get_mvn_metadata(bug, 'binSources')
        return abs_to_rel(bug.working_directory, bin_folders)

    def test_bin_folders(self, bug):
        if bug.project == "openmrs-openmrs-module-htmlformentry" and bug.bug_id == "452596787-452623087":
            # The buggy module of the openmrs-openmrs-module-htmlformentry-452596787-452623087
            # bug from the Bears benchmark is named 'api', however the
            # triggering test cases are in the module named 'api-tests'
            return ["api-tests/target/test-classes"]

        test_bin_folders = self._get_mvn_metadata(bug, 'binTests')
        return abs_to_rel(bug.working_directory, test_bin_folders)

    def classpath(self, bug):
        return ":".join(self._get_mvn_metadata(bug, 'classpath'))

    def compliance_level(self, bug):
        return self._get_mvn_metadata(bug, 'complianceLevel')

add_benchmark("Bears", Bears)
