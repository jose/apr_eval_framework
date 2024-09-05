#!/usr/bin/env bash

REPAIR_THEM_ALL_FRAMEWORK_DIR=`pwd`

# Get Apache-Maven <= 3.6.x, as any version higher than 3.6.x disables all insecure
# http://* mirrors by default.
# More info in here https://maven.apache.org/docs/3.8.1/release-notes.html#cve-2021-26291
#
# And hack the user's ~/.m2/settings.xml file to address the "Return code is: 501 , ReasonPhrase:HTTPS Required"
# error.
# More info in here https://stackoverflow.com/questions/59763531/maven-dependencies-are-failing-with-a-501-error
#
# Both hacks are required to successfully run `mvn` on some Wickets defects, e.g.,
# 0eb596df.
#
wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.6.3/apache-maven-3.6.3-bin.zip
if [ ! -s "apache-maven-3.6.3-bin.zip" ]; then
  echo "Failed to get apache-maven-3.6.3-bin.zip!"
  exit 1
fi
unzip apache-maven-3.6.3-bin.zip
if [ ! -d "apache-maven-3.6.3" ]; then
  echo "Failed to unzip apache-maven-3.6.3-bin.zip!"
  exit 1
fi
export PATH="$REPAIR_THEM_ALL_FRAMEWORK_DIR/apache-maven-3.6.3/bin:$PATH/"
#
# Hack the user's ~/.m2/settings.xml file
if [ -s ~/.m2/settings.xml ]; then
  mv -v ~/.m2/settings.xml ~/.m2/settings.xml.bak # Backup user's file
fi
mkdir -p ~/.m2
cp -v "$REPAIR_THEM_ALL_FRAMEWORK_DIR/m2_settings.xml" ~/.m2/settings.xml

# Remove any existing data and create required directories
mvn_deps_dir="$REPAIR_THEM_ALL_FRAMEWORK_DIR/mvn_deps"
rm -rf "$mvn_deps_dir"; mkdir -p "$mvn_deps_dir"

mvn_deps_zip="$REPAIR_THEM_ALL_FRAMEWORK_DIR/mvn_deps.zip"

# Try to get a cached version of all maven dependencies
GDRIVE_FILE_ID="1sa6qqiIp_xTqKsyQjHHFP3DkARNRZb91"
GDRIVE_FILE_URL="https://drive.google.com/uc?export=download&id=$GDRIVE_FILE_ID"
gdown "$GDRIVE_FILE_URL" -O "$mvn_deps_zip"

#
# If there a zip file with all maven dependencies, extract it and set the symlinks
#
if [ -s "$mvn_deps_zip" ]; then
  echo "Unzipping $mvn_deps_zip"
  unzip -u "$mvn_deps_zip"

#
# Otherwise, collect and cache all mvn dependencies
#
else
  # Build the project-info-maven-plugin (with Java-8) and install it in the local
  # mvn_deps repository
  echo "Building project-info-maven-plugin"
  git clone https://github.com/tdurieux/project-info-maven-plugin
  cd project-info-maven-plugin
  git checkout 93c8c5f8a4413a8b17f1346af6223ea345aae8cb
  export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn -DskipTests -Dmaven.repo.local="$mvn_deps_dir/plugin" install
  if [ "$?" -ne "0" ]; then
    echo "Failed to build the project-info-maven-plugin project and install it in $mvn_deps_dir/plugin!"
    exit 1
  fi
  cd ..
  rm -rf project-info-maven-plugin

  echo "Collecting and caching all mvn dependencies"

  # Collect apache-jar-resource-bundle, require to latter use the --offline mode
  wget https://repo1.maven.org/maven2/org/apache/apache-jar-resource-bundle/1.4/apache-jar-resource-bundle-1.4.jar
  if [ "$?" -ne "0" ]; then
    echo "Failed to get https://repo1.maven.org/maven2/org/apache/apache-jar-resource-bundle/1.4/apache-jar-resource-bundle-1.4.jar!"
    exit 1
  fi
  export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn \
    install:install-file -Dfile="apache-jar-resource-bundle-1.4.jar" \
    -DgroupId="org.apache" -DartifactId="apache-jar-resource-bundle" -Dversion="1.4" -Dpackaging="jar" \
    -Dmaven.repo.local="$mvn_deps_dir/extra"
  if [ "$?" -ne "0" ]; then
    echo "Failed to manually install apache-jar-resource-bundle-1.4 in $mvn_deps_dir!"
    exit 1
  fi
  rm -f "apache-jar-resource-bundle-1.4.jar"

  bugs_file="$REPAIR_THEM_ALL_FRAMEWORK_DIR/benchmarks/bugs.csv"
  if [ ! -s "$bugs_file" ]; then
    echo "$bugs_file does not exist or it is empty!"
    exit 1
  fi

  # Get maven .m2 per benchmark/project
  while read -r entry; do

    benchmark=$(echo "$entry" | cut -f1 -d',')
      project=$(echo "$entry" | cut -f2 -d',')
          bug=$(echo "$entry" | cut -f3 -d',')

    if [ "$benchmark" == "Defects4J" ] || [ "$benchmark" == "IntroClassJava" ] || [ "$benchmark" == "QuixBugs" ]; then
      continue
    fi
    # For only Bears and BugsDotJar

    echo "Downloading mvn dependencies of $benchmark :: $project :: $bug"

    mtwo_dir="$mvn_deps_dir/$project"
    mkdir -p "$mtwo_dir"

    export PYTHONPATH="$REPAIR_THEM_ALL_FRAMEWORK_DIR/script:$PYTHONPATH" && \
    python "$REPAIR_THEM_ALL_FRAMEWORK_DIR/get-mvn-deps.py" \
      --benchmark "$benchmark" \
      --project "$project" \
      --bug "$bug" \
      --mtwo_dir "$mtwo_dir"
    if [ "$?" -ne "0" ]; then
      echo "get-mvn-deps.py has failed!"
      exit 1
    fi

    if [ "$benchmark" == "Bears" ]; then
      if [ "$project" == "spring-projects-spring-data-jpa" ] && [ "$bug" == "341944401-342236799" ]; then
        wget https://repo1.maven.org/maven2/org/hibernate/hibernate-jpamodelgen/1.3.0.Final/hibernate-jpamodelgen-1.3.0.Final.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hibernate-jpamodelgen-1.3.0.Final.jar" -DgroupId="org.hibernate" -DartifactId="hibernate-jpamodelgen" -Dversion="1.3.0.Final" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f "hibernate-jpamodelgen-1.3.0.Final.jar"

        wget https://repo1.maven.org/maven2/org/aspectj/aspectjtools/1.8.12/aspectjtools-1.8.12.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="aspectjtools-1.8.12.jar" -DgroupId="org.aspectj" -DartifactId="aspectjtools" -Dversion="1.8.12" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f "aspectjtools-1.8.12.jar"
      elif [ "$project" == "spring-projects-spring-data-commons" ]; then
        export PYTHONPATH="$REPAIR_THEM_ALL_FRAMEWORK_DIR/script:$PYTHONPATH" && \
        python "$REPAIR_THEM_ALL_FRAMEWORK_DIR/get-mvn-deps.py" \
          --benchmark "$benchmark" \
          --project "$project" \
          --bug "$bug" \
          --mtwo_dir "$mtwo_dir"
        if [ "$?" -ne "0" ]; then
          echo "get-mvn-deps.py has failed!"
          exit 1
        fi

        wget https://repo1.maven.org/maven2/org/springframework/spring-core/4.2.5.RELEASE/spring-core-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-core-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-core" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-core-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-beans/4.2.5.RELEASE/spring-beans-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-beans-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-beans" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-beans-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-context/4.2.5.RELEASE/spring-context-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-context-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-context" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-context-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-aop/4.2.5.RELEASE/spring-aop-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-aop-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-aop" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-aop-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-expression/4.2.5.RELEASE/spring-expression-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-expression-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-expression" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-expression-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-tx/4.2.5.RELEASE/spring-tx-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-tx-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-tx" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-tx-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-oxm/4.2.5.RELEASE/spring-oxm-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-oxm-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-oxm" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-oxm-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.6.5/jackson-databind-2.6.5.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-databind-2.6.5.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-databind" -Dversion="2.6.5" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-databind-2.6.5.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.6.0/jackson-annotations-2.6.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-annotations-2.6.0.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-annotations" -Dversion="2.6.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-annotations-2.6.0.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.6.5/jackson-core-2.6.5.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-core-2.6.5.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-core" -Dversion="2.6.5" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-core-2.6.5.jar

        wget https://repo1.maven.org/maven2/org/springframework/spring-web/4.2.5.RELEASE/spring-web-4.2.5.RELEASE.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-web-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-web" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f spring-web-4.2.5.RELEASE.jar

        wget https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="javax.servlet-api-3.0.1.jar" -DgroupId="javax.servlet" -DartifactId="javax.servlet-api" -Dversion="3.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f javax.servlet-api-3.0.1.jar

        wget https://repo1.maven.org/maven2/joda-time/joda-time/2.9.2/joda-time-2.9.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="joda-time-2.9.2.jar" -DgroupId="joda-time" -DartifactId="joda-time" -Dversion="2.9.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f joda-time-2.9.2.jar

        wget https://repo1.maven.org/maven2/org/threeten/threetenbp/1.3.1/threetenbp-1.3.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="threetenbp-1.3.1.jar" -DgroupId="org.threeten" -DartifactId="threetenbp" -Dversion="1.3.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f threetenbp-1.3.1.jar

        # TODO do we need this extra step?!
        export PYTHONPATH="$REPAIR_THEM_ALL_FRAMEWORK_DIR/script:$PYTHONPATH" && \
        python "$REPAIR_THEM_ALL_FRAMEWORK_DIR/get-mvn-deps.py" \
          --benchmark "$benchmark" \
          --project "$project" \
          --bug "$bug" \
          --mtwo_dir "$mtwo_dir"
        if [ "$?" -ne "0" ]; then
          echo "get-mvn-deps.py has failed!"
          exit 1
        fi
      elif [ "$project" == "INRIA-spoon" ] && [ "$bug" == "189186902-189233591" ]; then
        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.resources/3.20.200/org.eclipse.core.resources-3.20.200.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.resources-3.20.200.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.resources" -Dversion="3.20.200" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.resources-3.20.200.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.expressions/3.9.400/org.eclipse.core.expressions-3.9.400.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.expressions-3.9.400.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.expressions" -Dversion="3.9.400" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.expressions-3.9.400.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.runtime/3.31.100/org.eclipse.core.runtime-3.31.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.runtime-3.31.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.runtime" -Dversion="3.31.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.runtime-3.31.100.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.jobs/3.15.300/org.eclipse.core.jobs-3.15.300.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.jobs-3.15.300.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.jobs" -Dversion="3.15.300" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.jobs-3.15.300.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.contenttype/3.9.400/org.eclipse.core.contenttype-3.9.400.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.contenttype-3.9.400.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.contenttype" -Dversion="3.9.400" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.filesystem/1.10.400/org.eclipse.core.filesystem-1.10.400.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.filesystem-1.10.400.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.filesystem" -Dversion="1.10.400" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.filesystem-1.10.400.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.commands/3.12.100/org.eclipse.core.commands-3.12.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.commands-3.12.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.commands" -Dversion="3.12.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.commands-3.12.100.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.osgi/3.20.0/org.eclipse.osgi-3.20.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.osgi-3.20.0.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.osgi" -Dversion="3.20.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.osgi-3.20.0.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.common/3.19.100/org.eclipse.equinox.common-3.19.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.common-3.19.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.common" -Dversion="3.19.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.equinox.common-3.19.100.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.registry/3.12.100/org.eclipse.equinox.registry-3.12.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.registry-3.12.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.registry" -Dversion="3.12.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.equinox.registry-3.12.100.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.preferences/3.11.100/org.eclipse.equinox.preferences-3.11.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.preferences-3.11.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.preferences" -Dversion="3.11.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.equinox.preferences-3.11.100.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.app/1.7.100/org.eclipse.equinox.app-1.7.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.app-1.7.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.app" -Dversion="1.7.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.equinox.app-1.7.100.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.text/3.14.100/org.eclipse.text-3.14.100.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.text-3.14.100.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.text" -Dversion="3.14.100" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.text-3.14.100.jar

        wget https://repo1.maven.org/maven2/org/osgi/org.osgi.service.prefs/1.1.2/org.osgi.service.prefs-1.1.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.osgi.service.prefs-1.1.2.jar" -DgroupId="org.osgi" -DartifactId="org.osgi.service.prefs" -Dversion="1.1.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.osgi.service.prefs-1.1.2.jar

        wget https://repo1.maven.org/maven2/org/osgi/osgi.annotation/8.0.1/osgi.annotation-8.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.osgi.service.prefs-1.1.2.jar" -DgroupId="org.osgi" -DartifactId="osgi.annotation" -Dversion="8.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f osgi.annotation-8.0.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/signature/java17/1.0/java17-1.0.pom
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -DgroupId="org.codehaus.mojo.signature" -DartifactId="java17" -Dversion="1.0" -Dpackaging="signature" -Dfile="java17-1.0.pom" -Dmaven.repo.local="$mtwo_dir"
        rm -f java17-1.0.pom

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/versions/versions-api/2.17.1/versions-api-2.17.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="versions-api-2.17.1.jar" -DgroupId="org.codehaus.mojo.versions" -DartifactId="versions-api" -Dversion="2.17.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f versions-api-2.17.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/versions/versions-common/2.17.1/versions-common-2.17.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="versions-common-2.17.1.jar" -DgroupId="org.codehaus.mojo.versions" -DartifactId="versions-common" -Dversion="2.17.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f versions-common-2.17.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/versions/versions-model/2.17.1/versions-model-2.17.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="versions-model-2.17.1.jar" -DgroupId="org.codehaus.mojo.versions" -DartifactId="versions-model" -Dversion="2.17.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f versions-model-2.17.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/versions/versions-model-report/2.17.1/versions-model-report-2.17.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="versions-model-report-2.17.1.jar" -DgroupId="org.codehaus.mojo.versions" -DartifactId="versions-model-report" -Dversion="2.17.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f versions-model-report-2.17.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.17.1/versions-maven-plugin-2.17.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="versions-maven-plugin-2.17.1.jar" -DgroupId="org.codehaus.mojo" -DartifactId="versions-maven-plugin" -Dversion="2.17.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f versions-maven-plugin-2.17.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-xml/3.0.1/plexus-xml-3.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-xml-3.0.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-xml" -Dversion="3.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-xml-3.0.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/4.0.1/plexus-utils-4.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-4.0.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="4.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-4.0.1.jar
      fi
    fi

  done < <(tail -n +2 "$bugs_file")

  find "$mvn_deps_dir" -type f -iname "*.repositories" -exec rm -f {} \;
  find "$mvn_deps_dir" -type f -iname "*.sha1" -exec rm -f {} \;

  # Create a master zip file with all maven dependencies
  zip -r "$mvn_deps_zip" mvn_deps/*
  if [ "$?" -ne "0" ]; then
    echo "Failed to zip the mvn_deps dir!"
    exit 1
  fi
fi

#
# Install junit-4.11.jar and hamcrest-core-1.3.jar in the local m2 (e.g., ~/.m2)
# as IntroClassJava and QuixBugs benchmarks are expecting to find those
# dependencies exactly in that location.
#

export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn \
  install:install-file -Dfile="$REPAIR_THEM_ALL_FRAMEWORK_DIR/libs/arja_external/lib/junit-4.11.jar" \
  -DgroupId="junit" -DartifactId="junit" -Dversion="4.11" -Dpackaging="jar"
if [ "$?" -ne "0" ]; then
  echo "Failed to install junit-4.11 in ~/.m2/!"
  exit 1
fi
if [ -s "~/.m2/repository/junit/junit/4.11/junit-4.11.jar" ]; then
  echo "There is no ~/.m2/repository/junit/junit/4.11/junit-4.11.jar file!"
  exit 1
fi

export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn \
  install:install-file -Dfile="$REPAIR_THEM_ALL_FRAMEWORK_DIR/libs/arja_external/lib/hamcrest-core-1.3.jar" \
  -DgroupId="org.hamcrest" -DartifactId="hamcrest-core" -Dversion="1.3" -Dpackaging="jar"
if [ "$?" -ne "0" ]; then
  echo "Failed to install hamcrest-core-1.3 in ~/.m2/!"
  exit 1
fi
if [ -s "~/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" ]; then
  echo "There is no ~/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar file!"
  exit 1
fi
