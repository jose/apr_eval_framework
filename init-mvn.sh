#!/usr/bin/env bash

REPAIR_THEM_ALL_FRAMEWORK_DIR=`pwd`

# Get Apache-Maven v3.5.4.
#
# Any version greater than 3.5.4 causes `Failed to execute goal org.codehaus.mojo:findbugs-maven-plugin:2.5.3:findbugs (findbugs) on project accumulo-project: Unable to parse configuration of mojo org.codehaus.mojo:findbugs-maven-plugin:2.5.3:findbugs for parameter pluginArtifacts: Cannot assign configuration entry 'pluginArtifacts' with value '${plugin.artifacts}' of type java.util.Collections.UnmodifiableRandomAccessList to property of type java.util.ArrayList`.
# on some Accumulo bugs.
#
# Any version greater that 3.6.x disables all insecure http://* mirrors by default
# (more info in here https://maven.apache.org/docs/3.8.1/release-notes.html#cve-2021-26291)
# which causes errors on some pom.xml file that still http://.
#
wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.5.4/apache-maven-3.5.4-bin.zip
if [ ! -s "apache-maven-3.5.4-bin.zip" ]; then
  echo "Failed to get apache-maven-3.5.4-bin.zip!"
  exit 1
fi
unzip apache-maven-3.5.4-bin.zip
if [ ! -d "apache-maven-3.5.4" ]; then
  echo "Failed to unzip apache-maven-3.5.4-bin.zip!"
  exit 1
fi
export PATH="$REPAIR_THEM_ALL_FRAMEWORK_DIR/apache-maven-3.5.4/bin:$PATH/"
#
# Hack the user's ~/.m2/settings.xml file to address the "Return code is: 501 , ReasonPhrase:HTTPS Required"
# error.  More info in here https://stackoverflow.com/questions/59763531/maven-dependencies-are-failing-with-a-501-error
#
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
  # Fix project-info-maven-plugin for
  # Bears :: Activiti-activiti-cloud-app-service :: 459060444-459062447
  # Bears :: societe-generale-ci-droid-tasks-consumer :: 420388707-430936160
  sed -i 's|String version = javaVersion.substring(2, 3);|String version = javaVersion.length() == 1 ? javaVersion : javaVersion.substring(2, 3);|' src/main/java/com/github/tdurieux/repair/maven/plugin/ProjectConfigMojo.java
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

        if [ "$bug" == "185852074-193313389" ]; then
          # Run the following for the very first bug in the list of bugs

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

          wget https://repo1.maven.org/maven2/com/mysema/querydsl/querydsl-core/3.7.4/querydsl-core-3.7.4.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="querydsl-core-3.7.4.jar" -DgroupId="com.mysema.querydsl" -DartifactId="querydsl-core" -Dversion="3.7.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f querydsl-core-3.7.4.jar

          wget https://repo1.maven.org/maven2/com/mysema/querydsl/querydsl-apt/3.7.4/querydsl-apt-3.7.4.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="querydsl-apt-3.7.4.jar" -DgroupId="com.mysema.querydsl" -DartifactId="querydsl-apt" -Dversion="3.7.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f querydsl-apt-3.7.4.jar

          wget https://repo1.maven.org/maven2/com/mysema/querydsl/querydsl-collections/3.7.4/querydsl-collections-3.7.4.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="querydsl-collections-3.7.4.jar" -DgroupId="com.mysema.querydsl" -DartifactId="querydsl-collections" -Dversion="3.7.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f querydsl-collections-3.7.4.jar

          wget https://repo1.maven.org/maven2/javax/ejb/ejb-api/3.0/ejb-api-3.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="ejb-api-3.0.jar" -DgroupId="javax.ejb" -DartifactId="ejb-api" -Dversion="3.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f ejb-api-3.0.jar

          wget https://repo1.maven.org/maven2/javax/el/el-api/1.0/el-api-1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="el-api-1.0.jar" -DgroupId="javax.el" -DartifactId="el-api" -Dversion="1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f el-api-1.0.jar

          wget https://repo1.maven.org/maven2/org/apache/openwebbeans/test/cditest-owb/1.2.8/cditest-owb-1.2.8.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="cditest-owb-1.2.8.jar" -DgroupId="org.apache.openwebbeans.test" -DartifactId="cditest-owb" -Dversion="1.2.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f cditest-owb-1.2.8.jar

          wget https://repo1.maven.org/maven2/org/apache/openwebbeans/test/cditest/1.2.8/cditest-1.2.8.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="cditest-1.2.8.jar" -DgroupId="org.apache.openwebbeans.test" -DartifactId="cditest" -Dversion="1.2.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f cditest-1.2.8.jar

          wget https://repo1.maven.org/maven2/org/apache/geronimo/specs/geronimo-jcdi_1.0_spec/1.0/geronimo-jcdi_1.0_spec-1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="geronimo-jcdi_1.0_spec-1.0.jar" -DgroupId="org.apache.geronimo.specs" -DartifactId="geronimo-jcdi_1.0_spec" -Dversion="1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f geronimo-jcdi_1.0_spec-1.0.jar

          wget https://repo1.maven.org/maven2/org/apache/geronimo/specs/geronimo-atinject_1.0_spec/1.0/geronimo-atinject_1.0_spec-1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="geronimo-atinject_1.0_spec-1.0.jar" -DgroupId="org.apache.geronimo.specs" -DartifactId="geronimo-atinject_1.0_spec" -Dversion="1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f geronimo-atinject_1.0_spec-1.0.jar

          wget https://repo1.maven.org/maven2/org/apache/openwebbeans/openwebbeans-spi/1.2.8/openwebbeans-spi-1.2.8.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="openwebbeans-spi-1.2.8.jar" -DgroupId="org.apache.openwebbeans" -DartifactId="openwebbeans-spi" -Dversion="1.2.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f openwebbeans-spi-1.2.8.jar

          wget https://repo1.maven.org/maven2/org/apache/openwebbeans/openwebbeans-impl/1.2.8/openwebbeans-impl-1.2.8.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="openwebbeans-impl-1.2.8.jar" -DgroupId="org.apache.openwebbeans" -DartifactId="openwebbeans-impl" -Dversion="1.2.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f openwebbeans-impl-1.2.8.jar

          wget https://repo1.maven.org/maven2/org/apache/xbean/xbean-finder-shaded/4.1/xbean-finder-shaded-4.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="xbean-finder-shaded-4.1.jar" -DgroupId="org.apache.xbean" -DartifactId="xbean-finder-shaded" -Dversion="4.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f xbean-finder-shaded-4.1.jar

          wget https://repo1.maven.org/maven2/org/apache/xbean/xbean-asm5-shaded/4.1/xbean-asm5-shaded-4.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="xbean-asm5-shaded-4.1.jar" -DgroupId="org.apache.xbean" -DartifactId="xbean-asm5-shaded" -Dversion="4.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f xbean-asm5-shaded-4.1.jar

          wget https://repo1.maven.org/maven2/org/jboss/interceptor/jboss-interceptor-api/1.1/jboss-interceptor-api-1.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jboss-interceptor-api-1.1.jar" -DgroupId="org.jboss.interceptor" -DartifactId="jboss-interceptor-api" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f jboss-interceptor-api-1.1.jar

          wget https://repo1.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="guava-19.0.jar" -DgroupId="com.google.guava" -DartifactId="guava" -Dversion="19.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f guava-19.0.jar

          wget https://repo1.maven.org/maven2/org/springframework/hateoas/spring-hateoas/0.19.0.RELEASE/spring-hateoas-0.19.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-hateoas-0.19.0.RELEASE.jar" -DgroupId="org.springframework.hateoas" -DartifactId="spring-hateoas" -Dversion="0.19.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f spring-hateoas-0.19.0.RELEASE.jar

          wget https://repo1.maven.org/maven2/org/springframework/spring-webmvc/4.2.5.RELEASE/spring-webmvc-4.2.5.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-webmvc-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-webmvc" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f spring-webmvc-4.2.5.RELEASE.jar

          wget https://repo1.maven.org/maven2/org/springframework/spring-test/4.2.5.RELEASE/spring-test-4.2.5.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="spring-test-4.2.5.RELEASE.jar" -DgroupId="org.springframework" -DartifactId="spring-test" -Dversion="4.2.5.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f spring-test-4.2.5.RELEASE.jar

          wget https://repo1.maven.org/maven2/com/sun/xml/bind/jaxb-impl/2.2.3U1/jaxb-impl-2.2.3U1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jaxb-impl-2.2.3U1.jar" -DgroupId="com.sun.xml.bind" -DartifactId="jaxb-impl" -Dversion="2.2.3U1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f jaxb-impl-2.2.3U1.jar

          wget https://repo1.maven.org/maven2/javax/xml/bind/jaxb-api/2.2.2/jaxb-api-2.2.2.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jaxb-api-2.2.2.jar" -DgroupId="javax.xml.bind" -DartifactId="jaxb-api" -Dversion="2.2.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f jaxb-api-2.2.2.jar

          wget https://repo1.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="activation-1.1.jar" -DgroupId="javax.activation" -DartifactId="activation" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f activation-1.1.jar

          wget https://repo1.maven.org/maven2/javax/xml/stream/stax-api/1.0-2/stax-api-1.0-2.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="stax-api-1.0-2.jar" -DgroupId="javax.xml.stream" -DartifactId="stax-api" -Dversion="1.0-2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f stax-api-1.0-2.jar

          wget https://repo1.maven.org/maven2/xmlunit/xmlunit/1.6/xmlunit-1.6.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="xmlunit-1.6.jar" -DgroupId="xmlunit" -DartifactId="xmlunit" -Dversion="1.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f xmlunit-1.6.jar

          wget https://repo1.maven.org/maven2/org/codehaus/groovy/groovy-all/2.4.4/groovy-all-2.4.4.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="groovy-all-2.4.4.jar" -DgroupId="org.codehaus.groovy" -DartifactId="groovy-all" -Dversion="2.4.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f groovy-all-2.4.4.jar

          wget https://repo1.maven.org/maven2/javax/transaction/javax.transaction-api/1.2/javax.transaction-api-1.2.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="javax.transaction-api-1.2.jar" -DgroupId="javax.transaction" -DartifactId="javax.transaction-api" -Dversion="1.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f javax.transaction-api-1.2.jar

          wget https://repo1.maven.org/maven2/com/jayway/jsonpath/json-path/2.0.0/json-path-2.0.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="json-path-2.0.0.jar" -DgroupId="com.jayway.jsonpath" -DartifactId="json-path" -Dversion="2.0.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f json-path-2.0.0.jar

          wget https://repo1.maven.org/maven2/net/minidev/json-smart/2.1.1/json-smart-2.1.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="json-smart-2.1.1.jar" -DgroupId="net.minidev" -DartifactId="json-smart" -Dversion="2.1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f json-smart-2.1.1.jar

          wget https://repo1.maven.org/maven2/net/minidev/asm/1.0.2/asm-1.0.2.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="asm-1.0.2.jar" -DgroupId="net.minidev" -DartifactId="asm" -Dversion="1.0.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f asm-1.0.2.jar

          wget https://repo1.maven.org/maven2/org/hamcrest/hamcrest-library/1.3/hamcrest-library-1.3.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hamcrest-library-1.3.jar" -DgroupId="org.hamcrest" -DartifactId="hamcrest-library" -Dversion="1.3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f hamcrest-library-1.3.jar

          wget https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hamcrest-core-1.3.jar" -DgroupId="org.hamcrest" -DartifactId="hamcrest-core" -Dversion="1.3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f hamcrest-core-1.3.jar

          wget https://repo1.maven.org/maven2/org/mockito/mockito-core/1.10.19/mockito-core-1.10.19.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="mockito-core-1.10.19.jar" -DgroupId="org.mockito" -DartifactId="mockito-core" -Dversion="1.10.19" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f mockito-core-1.10.19.jar

          wget https://repo1.maven.org/maven2/org/objenesis/objenesis/2.1/objenesis-2.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="objenesis-2.1.jar" -DgroupId="org.objenesis" -DartifactId="objenesis" -Dversion="2.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f objenesis-2.1.jar

          wget https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.19/slf4j-api-1.7.19.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="slf4j-api-1.7.19.jar" -DgroupId="org.slf4j" -DartifactId="slf4j-api" -Dversion="1.7.19" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f slf4j-api-1.7.19.jar

          wget https://repo1.maven.org/maven2/org/slf4j/jcl-over-slf4j/1.7.19/jcl-over-slf4j-1.7.19.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jcl-over-slf4j-1.7.19.jar" -DgroupId="org.slf4j" -DartifactId="jcl-over-slf4j" -Dversion="1.7.19" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f jcl-over-slf4j-1.7.19.jar

          wget https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.1.6/logback-classic-1.1.6.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-classic-1.1.6.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-classic" -Dversion="1.1.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f logback-classic-1.1.6.jar

          wget https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.1.6/logback-core-1.1.6.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-core-1.1.6.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-core" -Dversion="1.1.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f logback-core-1.1.6.jar

          wget https://repo1.maven.org/maven2/org/projectlombok/lombok/1.16.8/lombok-1.16.8.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="lombok-1.16.8.jar" -DgroupId="org.projectlombok" -DartifactId="lombok" -Dversion="1.16.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f lombok-1.16.8.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/bundlor/com.springsource.bundlor.maven/1.0.0.RELEASE/com.springsource.bundlor.maven-1.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.bundlor.maven-1.0.0.RELEASE.jar" -DgroupId="com.springsource.bundlor" -DartifactId="com.springsource.bundlor.maven" -Dversion="1.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.bundlor.maven-1.0.0.RELEASE.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/bundlor/com.springsource.bundlor/1.0.0.RELEASE/com.springsource.bundlor-1.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.bundlor-1.0.0.RELEASE.jar" -DgroupId="com.springsource.bundlor" -DartifactId="com.springsource.bundlor" -Dversion="1.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.bundlor-1.0.0.RELEASE.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/bundlor/com.springsource.bundlor.blint/1.0.0.RELEASE/com.springsource.bundlor.blint-1.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.bundlor.blint-1.0.0.RELEASE.jar" -DgroupId="com.springsource.bundlor" -DartifactId="com.springsource.bundlor.blint" -Dversion="1.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.bundlor.blint-1.0.0.RELEASE.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/util/com.springsource.util.common/2.0.0.RELEASE/com.springsource.util.common-2.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.util.common-2.0.0.RELEASE.jar" -DgroupId="com.springsource.util" -DartifactId="com.springsource.util.common" -Dversion="2.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.util.common-2.0.0.RELEASE.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/util/com.springsource.util.osgi/2.0.0.RELEASE/com.springsource.util.osgi-2.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.util.osgi-2.0.0.RELEASE.jar" -DgroupId="com.springsource.util" -DartifactId="com.springsource.util.osgi" -Dversion="2.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.util.osgi-2.0.0.RELEASE.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/util/com.springsource.util.parser.manifest/2.0.0.RELEASE/com.springsource.util.parser.manifest-2.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.util.parser.manifest-2.0.0.RELEASE.jar" -DgroupId="com.springsource.util" -DartifactId="com.springsource.util.parser.manifest" -Dversion="2.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.util.parser.manifest-2.0.0.RELEASE.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/com/springsource/util/com.springsource.util.math/2.0.0.RELEASE/com.springsource.util.math-2.0.0.RELEASE.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.util.math-2.0.0.RELEASE.jar" -DgroupId="com.springsource.util" -DartifactId="com.springsource.util.math" -Dversion="2.0.0.RELEASE" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.util.math-2.0.0.RELEASE.jar

          wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f plexus-utils-1.1.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/org/objectweb/asm/com.springsource.org.objectweb.asm/3.1.0/com.springsource.org.objectweb.asm-3.1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.org.objectweb.asm-3.1.0.jar" -DgroupId="org.objectweb.asm" -DartifactId="com.springsource.org.objectweb.asm" -Dversion="3.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.org.objectweb.asm-3.1.0.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/org/objectweb/asm/com.springsource.org.objectweb.asm.tree/3.1.0/com.springsource.org.objectweb.asm.tree-3.1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.org.objectweb.asm.tree-3.1.0.jar" -DgroupId="org.objectweb.asm" -DartifactId="com.springsource.org.objectweb.asm.tree" -Dversion="3.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.org.objectweb.asm.tree-3.1.0.jar

          wget https://nexus.squashtest.org/nexus/repository/maven-squashtest-public-releases/org/objectweb/asm/com.springsource.org.objectweb.asm.commons/3.1.0/com.springsource.org.objectweb.asm.commons-3.1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="com.springsource.org.objectweb.asm.commons-3.1.0.jar" -DgroupId="org.objectweb.asm" -DartifactId="com.springsource.org.objectweb.asm.commons" -Dversion="3.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f com.springsource.org.objectweb.asm.commons-3.1.0.jar

          wget https://repo1.maven.org/maven2/org/osgi/org.osgi.core/4.1.0/org.osgi.core-4.1.0.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.osgi.core-4.1.0.jar" -DgroupId="org.osgi" -DartifactId="org.osgi.core" -Dversion="4.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.osgi.core-4.1.0.jar

          wget https://sirius.cs.put.poznan.pl/~inf94272/eclipse/.metadata/.plugins/org.springframework.ide.eclipse.osgi.targetdefinition/2.5.0.201010221000-RELEASE/target/org.eclipse.osgi-3.5.1.R35x_v20091005.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.osgi-3.5.1.R35x_v20091005.jar" -DgroupId="org.eclipse.osgi" -DartifactId="org.eclipse.osgi" -Dversion="3.5.1.R35x_v20091005" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.osgi-3.5.1.R35x_v20091005.jar
        fi

      elif [ "$project" == "INRIA-spoon" ] && [ "$bug" == "189186902-189233591" ]; then
        # Although the following code is only executed on INRIA-spoon::189186902-189233591,
        # it is required for most of the bugs in the INRIA-spoon project.  The condition is in
        # place to avoid executing the following code for every single bug in the INRIA-spoon project.

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.resources/3.20.200/org.eclipse.core.resources-3.20.200.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.resources-3.20.200.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.resources" -Dversion="3.20.200" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.resources-3.20.200.jar

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.resources/3.21.0/org.eclipse.core.resources-3.21.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.resources-3.21.0.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.resources" -Dversion="3.21.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.resources-3.21.0.jar

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

        wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.filesystem/1.11.0/org.eclipse.core.filesystem-1.11.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.filesystem-1.11.0.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.filesystem" -Dversion="1.11.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f org.eclipse.core.filesystem-1.11.0.jar

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

      elif [ "$project" == "debezium-debezium" ] && [ "$bug" == "324021438-324040188" ]; then
        # Although the following code is only executed on debezium-debezium::324021438-324040188,
        # it is required for most of the bugs in the debezium-debezium project.  The condition is in
        # place to avoid executing the following code for every single bug in the debezium-debezium project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "2018swecapstone-h2ms" ] && [ "$bug" == "356638992-356666847" ]; then
        # Although the following code is only executed on 2018swecapstone-h2ms::356638992-356666847,
        # it is required for most of the bugs in the 2018swecapstone-h2ms project.  The condition is in
        # place to avoid executing the following code for every single bug in the 2018swecapstone-h2ms project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "apache-incubator-servicecomb-java-chassis" ] && [ "$bug" == "330826674-331080091" ]; then
        # Although the following code is only executed on apache-incubator-servicecomb-java-chassis::330826674-331080091,
        # it is required for most of the bugs in the apache-incubator-servicecomb-java-chassis project.  The condition is in
        # place to avoid executing the following code for every single bug in the apache-incubator-servicecomb-java-chassis project.

        wget https://repo1.maven.org/maven2/org/apache/apache-incubator-disclaimer-resource-bundle/1.1/apache-incubator-disclaimer-resource-bundle-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="apache-incubator-disclaimer-resource-bundle-1.1.jar" -DgroupId="org.apache" -DartifactId="apache-incubator-disclaimer-resource-bundle" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f apache-incubator-disclaimer-resource-bundle-1.1.jar

      elif [ "$project" == "molgenis-molgenis" ] && [ "$bug" == "336061452-336065127" ]; then
        # Although the following code is only executed on molgenis-molgenis::336061452-336065127,
        # it is required for most of the bugs in the molgenis-molgenis project.  The condition is in
        # place to avoid executing the following code for every single bug in the molgenis-molgenis project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "openzipkin-zipkin" ] && [ "$bug" == "332209085-332270677" ]; then

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/2.0.5/plexus-utils-2.0.5.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-2.0.5.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="2.0.5" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/plugins/maven-antrun-plugin/1.7/maven-antrun-plugin-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-antrun-plugin-1.7.jar" -DgroupId="org.apache.maven.plugins" -DartifactId="maven-antrun-plugin" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-antrun-plugin-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/ant/ant-parent/1.8.2/ant-parent-1.8.2.pom
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="ant-parent-1.8.2.pom" -DpomFile="ant-parent-1.8.2.pom" -Dpackaging="pom" -Dmaven.repo.local="$mtwo_dir"
        rm -f ant-parent-1.8.2.pom

        wget https://repo1.maven.org/maven2/org/apache/ant/ant/1.8.2/ant-1.8.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="ant-1.8.2.jar" -DgroupId="org.apache.ant" -DartifactId="ant" -Dversion="1.8.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f ant-1.8.2.jar

        wget https://repo.maven.apache.org/maven2/org/apache/ant/ant-launcher/1.8.2/ant-launcher-1.8.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="ant-launcher-1.8.2.jar" -DgroupId="org.apache.ant" -DartifactId="ant-launcher" -Dversion="1.8.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f ant-1.8.2.jar

      elif [ "$project" == "thelastpickle-cassandra-reaper" ] && [ "$bug" == "324455111-327555133" ]; then
        # Although the following code is only executed on thelastpickle-cassandra-reaper::324455111-327555133,
        # it is required for most of the bugs in the thelastpickle-cassandra-reaper project.  The condition is in
        # place to avoid executing the following code for every single bug in the thelastpickle-cassandra-reaper project.

        wget https://repo1.maven.org/maven2/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="joda-time-2.9.4.jar" -DgroupId="joda-time" -DartifactId="joda-time" -Dversion="2.9.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f joda-time-2.9.4.jar

        wget https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.21/slf4j-api-1.7.21.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="slf4j-api-1.7.21.jar" -DgroupId="org.slf4j" -DartifactId="slf4j-api" -Dversion="1.7.21" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f slf4j-api-1.7.21.jar

        wget https://repo1.maven.org/maven2/org/slf4j/log4j-over-slf4j/1.7.21/log4j-over-slf4j-1.7.21.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="log4j-over-slf4j-1.7.21.jar" -DgroupId="org.slf4j" -DartifactId="log4j-over-slf4j" -Dversion="1.7.21" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f log4j-over-slf4j-1.7.21.jar

        wget https://repo1.maven.org/maven2/org/slf4j/jcl-over-slf4j/1.7.21/jcl-over-slf4j-1.7.21.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jcl-over-slf4j-1.7.21.jar" -DgroupId="org.slf4j" -DartifactId="jcl-over-slf4j" -Dversion="1.7.21" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jcl-over-slf4j-1.7.21.jar

        wget https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.1.7/logback-classic-1.1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-classic-1.1.7.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-classic" -Dversion="1.1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f logback-classic-1.1.7.jar

        wget https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.1.7/logback-core-1.1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-core-1.1.7.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-core" -Dversion="1.1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f logback-core-1.1.7.jar

        wget https://repo1.maven.org/maven2/org/yaml/snakeyaml/1.15/snakeyaml-1.15.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="snakeyaml-1.15.jar" -DgroupId="org.yaml" -DartifactId="snakeyaml" -Dversion="1.15" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f snakeyaml-1.15.jar

        wget https://repo1.maven.org/maven2/ch/qos/logback/logback-access/1.1.7/logback-access-1.1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-access-1.1.7.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-access" -Dversion="1.1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f logback-access-1.1.7.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/core/jersey-common/2.23.2/jersey-common-2.23.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-common-2.23.2.jar" -DgroupId="org.glassfish.jersey.core" -DartifactId="jersey-common" -Dversion="2.23.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-common-2.23.2.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/core/jersey-client/2.23.2/jersey-client-2.23.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-client-2.23.2.jar" -DgroupId="org.glassfish.jersey.core" -DartifactId="jersey-client" -Dversion="2.23.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-client-2.23.2.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/bundles/repackaged/jersey-guava/2.23.2/jersey-guava-2.23.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-guava-2.23.2.jar" -DgroupId="org.glassfish.jersey.bundles.repackaged" -DartifactId="jersey-guava" -Dversion="2.23.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-guava-2.23.2.jar

        wget https://repo1.maven.org/maven2/org/glassfish/hk2/hk2-api/2.5.0-b05/hk2-api-2.5.0-b05.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hk2-api-2.5.0-b05.jar" -DgroupId="org.glassfish.hk2" -DartifactId="hk2-api" -Dversion="2.5.0-b05" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f hk2-api-2.5.0-b05.jar

        wget https://repo1.maven.org/maven2/org/glassfish/hk2/hk2-utils/2.5.0-b05/hk2-utils-2.5.0-b05.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hk2-utils-2.5.0-b05.jar" -DgroupId="org.glassfish.hk2" -DartifactId="hk2-utils" -Dversion="2.5.0-b05" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f hk2-utils-2.5.0-b05.jar

        wget https://repo1.maven.org/maven2/org/glassfish/hk2/hk2-locator/2.5.0-b05/hk2-locator-2.5.0-b05.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hk2-locator-2.5.0-b05.jar" -DgroupId="org.glassfish.hk2" -DartifactId="hk2-locator" -Dversion="2.5.0-b05" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f hk2-locator-2.5.0-b05.jar

        wget https://repo1.maven.org/maven2/org/glassfish/hk2/external/aopalliance-repackaged/2.5.0-b05/aopalliance-repackaged-2.5.0-b05.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="aopalliance-repackaged-2.5.0-b05.jar" -DgroupId="org.glassfish.hk2.external" -DartifactId="aopalliance-repackaged" -Dversion="2.5.0-b05" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f aopalliance-repackaged-2.5.0-b05.jar

        wget https://repo1.maven.org/maven2/org/glassfish/hk2/external/javax.inject/2.5.0-b05/javax.inject-2.5.0-b05.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="javax.inject-2.5.0-b05.jar" -DgroupId="org.glassfish.hk2.external" -DartifactId="javax.inject" -Dversion="2.5.0-b05" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f javax.inject-2.5.0-b05.jar

        https://repo1.maven.org/maven2/org/objenesis/objenesis/2.1/objenesis-2.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="objenesis-2.1.jar" -DgroupId="org.objenesis" -DartifactId="objenesis" -Dversion="2.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f objenesis-2.1.jar

        wget https://repo1.maven.org/maven2/org/objenesis/objenesis/2.6/objenesis-2.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="objenesis-2.6.jar" -DgroupId="org.objenesis" -DartifactId="objenesis" -Dversion="2.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f objenesis-2.6.jar

      elif [ "$project" == "FasterXML-jackson-dataformats-binary" ] && [ "$bug" == "461131167-461139756" ]; then
        # Although the following code is only executed on FasterXML-jackson-dataformats-binary::461131167-461139756,
        # it is required for most of the bugs in the FasterXML-jackson-dataformats-binary project.  The condition is in
        # place to avoid executing the following code for every single bug in the FasterXML-jackson-dataformats-binary project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "vert-x3-vertx-jdbc-client" ] && [ "$bug" == "438227334-440167311" ]; then
        # Although the following code is only executed on vert-x3-vertx-jdbc-client::438227334-440167311,
        # it is required for most of the bugs in the vert-x3-vertx-jdbc-client project.  The condition is in
        # place to avoid executing the following code for every single bug in the vert-x3-vertx-jdbc-client project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "vert-x3-vertx-web" ] && [ "$bug" == "459797171-460136219" ]; then
        # Although the following code is only executed on vert-x3-vertx-web::459797171-460136219,
        # it is required for most of the bugs in the vert-x3-vertx-web project.  The condition is in
        # place to avoid executing the following code for every single bug in the vert-x3-vertx-web project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/reporting/maven-reporting-api/2.2.1/maven-reporting-api-2.2.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-reporting-api-2.2.1.jar" -DgroupId="org.apache.maven.reporting" -DartifactId="maven-reporting-api" -Dversion="2.2.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-reporting-api-2.2.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/doxia/doxia-sink-api/1.1/doxia-sink-api-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="doxia-sink-api-1.1.jar" -DgroupId="org.apache.maven.doxia" -DartifactId="doxia-sink-api" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f doxia-sink-api-1.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/doxia/doxia-logging-api/1.1/doxia-logging-api-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="doxia-logging-api-1.1.jar" -DgroupId="org.apache.maven.doxia" -DartifactId="doxia-logging-api" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f doxia-logging-api-1.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-plugin-api/3.0/maven-plugin-api-3.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-plugin-api-3.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-plugin-api" -Dversion="3.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-plugin-api-3.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-plugin-api/3.1.0/maven-plugin-api-3.1.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-plugin-api-3.1.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-plugin-api" -Dversion="3.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-plugin-api-3.1.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-core/3.1.0/maven-core-3.1.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-core-3.1.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-core" -Dversion="3.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-core-3.1.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-core/3.0/maven-core-3.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-core-3.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-core" -Dversion="3.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-core-3.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/3.0.0/maven-shared-utils-3.0.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-shared-utils-3.0.0.jar" -DgroupId="org.apache.maven.shared" -DartifactId="maven-shared-utils" -Dversion="3.0.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-shared-utils-3.0.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/file-management/3.0.0/file-management-3.0.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="file-management-3.0.0.jar" -DgroupId="org.apache.maven.shared" -DartifactId="file-management" -Dversion="3.0.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f file-management-3.0.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/plugin-tools/maven-plugin-annotations/3.2/maven-plugin-annotations-3.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-plugin-annotations-3.2.jar" -DgroupId="org.apache.maven.plugin-tools" -DartifactId="maven-plugin-annotations" -Dversion="3.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-plugin-annotations-3.2.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/3.0.15/plexus-utils-3.0.15.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-3.0.15.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="3.0.15" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-3.0.15.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-model/3.0/maven-model-3.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-model-3.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-model" -Dversion="3.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-model-3.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-archiver/3.1.1/maven-archiver-3.1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-archiver-3.1.1.jar" -DgroupId="org.apache.maven" -DartifactId="maven-archiver" -Dversion="3.1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-archiver-3.1.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-archiver/3.4/plexus-archiver-3.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-archiver-3.4.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-archiver" -Dversion="3.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-archiver-3.4.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/3.0.24/plexus-utils-3.0.24.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-3.0.24.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="3.0.24" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-3.0.24.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-io/2.7.1/plexus-io-2.7.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-io-2.7.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-io" -Dversion="2.7.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-io-2.7.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-build-api/0.0.1/plexus-build-api-0.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-build-api-0.0.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-build-api" -Dversion="0.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-build-api-0.0.1.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-build-api/1.0.0/plexus-build-api-1.0.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-build-api-1.0.0.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-build-api" -Dversion="1.0.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-build-api-1.0.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/0.3/maven-shared-utils-0.3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-shared-utils-0.3.jar" -DgroupId="org.apache.maven.shared" -DartifactId="maven-shared-utils" -Dversion="0.3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-shared-utils-0.3.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/2.0.4/plexus-utils-2.0.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-2.0.4.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="2.0.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-2.0.4.jar

        wget https://repo1.maven.org/maven2/org/ow2/asm/asm/6.0_BETA/asm-6.0_BETA.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="asm-6.0_BETA.jar" -DgroupId="org.ow2.asm" -DartifactId="asm" -Dversion="6.0_BETA" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f asm-6.0_BETA.jar

        wget https://repo1.maven.org/maven2/com/thoughtworks/qdox/qdox/2.0-M7/qdox-2.0-M7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="qdox-2.0-M7.jar" -DgroupId="com.thoughtworks.qdox" -DartifactId="qdox" -Dversion="2.0-M7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f qdox-2.0-M7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/maven-filtering/1.2/maven-filtering-1.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-filtering-1.2.jar" -DgroupId="org.apache.maven.shared" -DartifactId="maven-filtering" -Dversion="1.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-filtering-1.2.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-interpolation/1.19/plexus-interpolation-1.19.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-interpolation-1.19.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-interpolation" -Dversion="1.19" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-interpolation-1.19.jar

      elif [ "$project" == "Activiti-activiti-cloud-app-service" ] && [ "$bug" == "459060444-459062447" ]; then
        # Although the following code is only executed on Activiti-activiti-cloud-app-service::459060444-459062447,
        # it is required for most of the bugs in the Activiti-activiti-cloud-app-service project.  The condition is in
        # place to avoid executing the following code for every single bug in the Activiti-activiti-cloud-app-service project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/2.0.4/plexus-utils-2.0.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-2.0.4.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="2.0.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-2.0.4.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/3.0.15/plexus-utils-3.0.15.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-3.0.15.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="3.0.15" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-3.0.15.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-component-annotations/1.6/plexus-component-annotations-1.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-component-annotations-1.6.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-component-annotations" -Dversion="1.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-component-annotations-1.6.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/0.4/maven-shared-utils-0.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-shared-utils-0.4.jar" -DgroupId="org.apache.maven.shared" -DartifactId="maven-shared-utils" -Dversion="0.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-shared-utils-0.4.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/3.1.0/maven-shared-utils-3.1.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-shared-utils-3.1.0.jar" -DgroupId="org.apache.maven.shared" -DartifactId="maven-shared-utils" -Dversion="3.1.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-shared-utils-3.1.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-plugin-api/3.0/maven-plugin-api-3.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-plugin-api-3.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-plugin-api" -Dversion="3.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-plugin-api-3.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-core/3.0/maven-core-3.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-core-3.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-core" -Dversion="3.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-core-3.0.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-java/0.9.2/plexus-java-0.9.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-java-0.9.2.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-java" -Dversion="0.9.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-java-0.9.2.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-compiler-api/2.8.2/plexus-compiler-api-2.8.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-compiler-api-2.8.2.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-compiler-api" -Dversion="2.8.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-compiler-api-2.8.2.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-compiler-manager/2.8.2/plexus-compiler-manager-2.8.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-compiler-manager-2.8.2.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-compiler-manager" -Dversion="2.8.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-compiler-manager-2.8.2.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-compiler-javac/2.8.2/plexus-compiler-javac-2.8.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-compiler-javac-2.8.2.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-compiler-javac" -Dversion="2.8.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-compiler-javac-2.8.2.jar

        wget https://repo1.maven.org/maven2/org/ow2/asm/asm/6.0_BETA/asm-6.0_BETA.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="asm-6.0_BETA.jar" -DgroupId="org.ow2.asm" -DartifactId="asm" -Dversion="6.0_BETA" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f asm-6.0_BETA.jar

        wget https://repo1.maven.org/maven2/org/ow2/asm/asm-parent/6.0_BETA/asm-parent-6.0_BETA.pom
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="asm-parent-6.0_BETA.pom" -DpomFile="asm-parent-6.0_BETA.pom" -Dpackaging="pom" -Dmaven.repo.local="$mtwo_dir"
        rm -f asm-parent-6.0_BETA.pom

        wget https://repo1.maven.org/maven2/commons-codec/commons-codec/1.6/commons-codec-1.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="commons-codec-1.6.jar" -DgroupId="commons-codec" -DartifactId="commons-codec" -Dversion="1.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f commons-codec-1.6.jar

      elif [ "$project" == "awslabs-amazon-kinesis-client" ] && [ "$bug" == "446289265-452166805" ]; then
        # Although the following code is only executed on awslabs-amazon-kinesis-client::446289265-452166805,
        # it is required for most of the bugs in the awslabs-amazon-kinesis-client project.  The condition is in
        # place to avoid executing the following code for every single bug in the awslabs-amazon-kinesis-client project.

        wget https://repo1.maven.org/maven2/com/almworks/sqlite4java/libsqlite4java-osx/1.0.392/libsqlite4java-osx-1.0.392.dylib
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="libsqlite4java-osx-1.0.392.dylib" -DgroupId="com.almworks.sqlite4java" -DartifactId="libsqlite4java-osx" -Dversion="1.0.392" -Dpackaging="dylib" -Dmaven.repo.local="$mtwo_dir"
        rm -f libsqlite4java-osx-1.0.392.dylib

        wget https://repo1.maven.org/maven2/com/almworks/sqlite4java/libsqlite4java-linux-i386/1.0.392/libsqlite4java-linux-i386-1.0.392.so
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="libsqlite4java-linux-i386-1.0.392.so" -DgroupId="com.almworks.sqlite4java" -DartifactId="libsqlite4java-linux-i386" -Dversion="1.0.392" -Dpackaging="so" -Dmaven.repo.local="$mtwo_dir"
        rm -f libsqlite4java-linux-i386-1.0.392.so

        wget https://repo1.maven.org/maven2/com/almworks/sqlite4java/libsqlite4java-linux-amd64/1.0.392/libsqlite4java-linux-amd64-1.0.392.so
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="libsqlite4java-linux-amd64-1.0.392.so" -DgroupId="com.almworks.sqlite4java" -DartifactId="libsqlite4java-linux-amd64" -Dversion="1.0.392" -Dpackaging="so" -Dmaven.repo.local="$mtwo_dir"
        rm -f libsqlite4java-linux-amd64-1.0.392.so

        wget https://repo1.maven.org/maven2/com/almworks/sqlite4java/sqlite4java-win32-x86/1.0.392/sqlite4java-win32-x86-1.0.392.dll
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="sqlite4java-win32-x86-1.0.392.dll" -DgroupId="com.almworks.sqlite4java" -DartifactId="sqlite4java-win32-x86" -Dversion="1.0.392" -Dpackaging="dll" -Dmaven.repo.local="$mtwo_dir"
        rm -f sqlite4java-win32-x86-1.0.392.dll

        wget https://repo1.maven.org/maven2/com/almworks/sqlite4java/sqlite4java-win32-x64/1.0.392/sqlite4java-win32-x64-1.0.392.dll
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="sqlite4java-win32-x64-1.0.392.dll" -DgroupId="com.almworks.sqlite4java" -DartifactId="sqlite4java-win32-x64" -Dversion="1.0.392" -Dpackaging="dll" -Dmaven.repo.local="$mtwo_dir"
        rm -f sqlite4java-win32-x64-1.0.392.dll

      elif [ "$project" == "AxonFramework-AxonFramework" ] && [ "$bug" == "451926440-451937647" ]; then
        # Although the following code is only executed on AxonFramework-AxonFramework::451926440-451937647,
        # it is required for most of the bugs in the AxonFramework-AxonFramework project.  The condition is in
        # place to avoid executing the following code for every single bug in the AxonFramework-AxonFramework project.
        #
        # TODO the following only works on linux-x86_64 for other OS it must be adapted

        wget https://repo1.maven.org/maven2/com/google/protobuf/protoc/3.0.2/protoc-3.0.2-linux-x86_64.exe
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="protoc-3.0.2-linux-x86_64.exe" -DgroupId="com.google.protobuf" -DartifactId="protoc" -Dversion="3.0.2" -Dclassifier="linux-x86_64" -Dpackaging="exe" -Dmaven.repo.local="$mtwo_dir"
        rm -f protoc-3.0.2-linux-x86_64.exe

        wget https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.1.2/protoc-gen-grpc-java-1.1.2-linux-x86_64.exe
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="protoc-gen-grpc-java-1.1.2-linux-x86_64.exe" -DgroupId="io.grpc" -DartifactId="protoc-gen-grpc-java" -Dversion="1.1.2" -Dclassifier="linux-x86_64" -Dpackaging="exe" -Dmaven.repo.local="$mtwo_dir"
        rm -f protoc-gen-grpc-java-1.1.2-linux-x86_64.exe

      elif [ "$project" == "brettwooldridge-HikariCP" ] && [ "$bug" == "446097106-446106182" ]; then
        # Although the following code is only executed on brettwooldridge-HikariCP::446289265-452166805,
        # it is required for most of the bugs in the brettwooldridge-HikariCP project.  The condition is in
        # place to avoid executing the following code for every single bug in the brettwooldridge-HikariCP project.

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/surefire-junit4/2.22.0/surefire-junit4-2.22.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="surefire-junit4-2.22.0.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="surefire-junit4" -Dversion="2.22.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f surefire-junit4-2.22.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/surefire-logger-api/2.22.0/surefire-logger-api-2.22.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="surefire-logger-api-2.22.0.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="surefire-logger-api" -Dversion="2.22.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f surefire-logger-api-2.22.0.jar

      elif [ "$project" == "classgraph-classgraph" ] && [ "$bug" == "448207270-449613955" ]; then
        # Although the following code is only executed on classgraph-classgraph::448207270-449613955,
        # it is required for most of the bugs in the classgraph-classgraph project.  The condition is in
        # place to avoid executing the following code for every single bug in the classgraph-classgraph project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "DataBiosphere-consent-ontology" ] && [ "$bug" == "453433108-453941362" ]; then
        # Although the following code is only executed on DataBiosphere-consent-ontology::453433108-453941362,
        # it is required for most of the bugs in the DataBiosphere-consent-ontology project.  The condition is in
        # place to avoid executing the following code for every single bug in the DataBiosphere-consent-ontology project.

        wget https://repo1.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.1/jsr305-3.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jsr305-3.0.1.jar" -DgroupId="com.google.code.findbugs" -DartifactId="jsr305" -Dversion="3.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jsr305-3.0.1.jar

        wget https://repo1.maven.org/maven2/io/dropwizard/metrics/metrics-core/3.1.2/metrics-core-3.1.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="metrics-core-3.1.2.jar" -DgroupId="io.dropwizard.metrics" -DartifactId="metrics-core" -Dversion="3.1.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f metrics-core-3.1.2.jar

        wget https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="javax.servlet-api-3.0.1.jar" -DgroupId="javax.servlet" -DartifactId="javax.servlet-api" -Dversion="3.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f javax.servlet-api-3.0.1.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.7.8/jackson-core-2.7.8.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-core-2.7.8.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-core" -Dversion="2.7.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-core-2.7.8.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.7.8/jackson-annotations-2.7.8.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-annotations-2.7.8.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-annotations" -Dversion="2.7.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-annotations-2.7.8.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.7.8/jackson-databind-2.7.8.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-databind-2.7.8.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-databind" -Dversion="2.7.8" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-databind-2.7.8.jar

        wget https://repo1.maven.org/maven2/org/json/json/20090211/json-20090211.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="json-20090211.jar" -DgroupId="org.json" -DartifactId="json" -Dversion="20090211" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f json-20090211.jar

        wget https://repo1.maven.org/maven2/xerces/xercesImpl/2.4.0/xercesImpl-2.4.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="xercesImpl-2.4.0.jar" -DgroupId="xerces" -DartifactId="xercesImpl" -Dversion="2.4.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f xercesImpl-2.4.0.jar

        wget https://repo1.maven.org/maven2/commons-io/commons-io/1.3.2/commons-io-1.3.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="commons-io-1.3.2.jar" -DgroupId="commons-io" -DartifactId="commons-io" -Dversion="1.3.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f commons-io-1.3.2.jar

        wget https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="slf4j-api-1.7.25.jar" -DgroupId="org.slf4j" -DartifactId="slf4j-api" -Dversion="1.7.25" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f slf4j-api-1.7.25.jar

        wget https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.1.3/logback-classic-1.1.3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-classic-1.1.3.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-classic" -Dversion="1.1.3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f logback-classic-1.1.3.jar

        wget https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.1.3/logback-core-1.1.3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="logback-core-1.1.3.jar" -DgroupId="ch.qos.logback" -DartifactId="logback-core" -Dversion="1.1.3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f logback-core-1.1.3.jar

        wget https://repo1.maven.org/maven2/org/javassist/javassist/3.18.1-GA/javassist-3.18.1-GA.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="javassist-3.18.1-GA.jar" -DgroupId="org.javassist" -DartifactId="javassist" -Dversion="3.18.1-GA" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f javassist-3.18.1-GA.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/containers/jersey-container-servlet/2.20/jersey-container-servlet-2.20.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-container-servlet-2.20.jar" -DgroupId="org.glassfish.jersey.containers" -DartifactId="jersey-container-servlet" -Dversion="2.20" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-container-servlet-2.20.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.7.6/jackson-annotations-2.7.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-annotations-2.7.6.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-annotations" -Dversion="2.7.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-annotations-2.7.6.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.7.6/jackson-core-2.7.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-core-2.7.6.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-core" -Dversion="2.7.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-core-2.7.6.jar

        wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.7.6/jackson-databind-2.7.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jackson-databind-2.7.6.jar" -DgroupId="com.fasterxml.jackson.core" -DartifactId="jackson-databind" -Dversion="2.7.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jackson-databind-2.7.6.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/test-framework/jersey-test-framework-core/2.20/jersey-test-framework-core-2.20.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-test-framework-core-2.20.jar" -DgroupId="org.glassfish.jersey.test-framework" -DartifactId="jersey-test-framework-core" -Dversion="2.20" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-test-framework-core-2.20.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/containers/jersey-container-servlet-core/2.20/jersey-container-servlet-core-2.20.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-container-servlet-core-2.20.jar" -DgroupId="org.glassfish.jersey.containers" -DartifactId="jersey-container-servlet-core" -Dversion="2.20" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-container-servlet-core-2.20.jar

        wget https://repo1.maven.org/maven2/org/ow2/asm/asm-debug-all/5.0.4/asm-debug-all-5.0.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="asm-debug-all-5.0.4.jar" -DgroupId="org.ow2.asm" -DartifactId="asm-debug-all" -Dversion="5.0.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f asm-debug-all-5.0.4.jar

        wget https://repo1.maven.org/maven2/org/glassfish/jersey/containers/jersey-container-servlet-core/2.23.1/jersey-container-servlet-core-2.23.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jersey-container-servlet-core-2.23.1.jar" -DgroupId="org.glassfish.jersey.containers" -DartifactId="jersey-container-servlet-core" -Dversion="2.23.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jersey-container-servlet-core-2.23.1.jar

      elif [ "$project" == "DmitriiSerikov-money-transfer-service" ] && [ "$bug" == "446104441-446106577" ]; then
        # Although the following code is only executed on DmitriiSerikov-money-transfer-service::446104441-446106577,
        # it is required for most of the bugs in the DmitriiSerikov-money-transfer-service project.  The condition is in
        # place to avoid executing the following code for every single bug in the DmitriiSerikov-money-transfer-service project.

        wget https://repo1.maven.org/maven2/org/codehaus/groovy/groovy/2.5.1/groovy-2.5.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="groovy-2.5.1.jar" -DgroupId="org.codehaus.groovy" -DartifactId="groovy" -Dversion="2.5.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f groovy-2.5.1.jar

        wget https://repo1.maven.org/maven2/io/micronaut/micronaut-inject-java/1.0.0.RC3/micronaut-inject-java-1.0.0.RC3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="micronaut-inject-java-1.0.0.RC3.jar" -DgroupId="io.micronaut" -DartifactId="micronaut-inject-java" -Dversion="1.0.0.RC3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f micronaut-inject-java-1.0.0.RC3.jar

        wget https://repo1.maven.org/maven2/io/micronaut/micronaut-inject-groovy/1.0.0.RC3/micronaut-inject-groovy-1.0.0.RC3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="micronaut-inject-groovy-1.0.0.RC3.jar" -DgroupId="io.micronaut" -DartifactId="micronaut-inject-groovy" -Dversion="1.0.0.RC3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f micronaut-inject-groovy-1.0.0.RC3.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/surefire-junit47/2.22.1/surefire-junit47-2.22.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="surefire-junit47-2.22.1.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="surefire-junit47" -Dversion="2.22.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f surefire-junit47-2.22.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/common-junit48/2.22.1/common-junit48-2.22.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="common-junit48-2.22.1.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="common-junit48" -Dversion="2.22.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f common-junit48-2.22.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/common-junit3/2.22.1/common-junit3-2.22.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="common-junit3-2.22.1.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="common-junit3" -Dversion="2.22.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f common-junit3-2.22.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/common-junit4/2.22.1/common-junit4-2.22.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="common-junit4-2.22.1.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="common-junit4" -Dversion="2.22.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f common-junit4-2.22.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/common-java5/2.22.1/common-java5-2.22.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="common-java5-2.22.1.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="common-java5" -Dversion="2.22.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f common-java5-2.22.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/surefire/surefire-grouper/2.22.1/surefire-grouper-2.22.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="surefire-grouper-2.22.1.jar" -DgroupId="org.apache.maven.surefire" -DartifactId="surefire-grouper" -Dversion="2.22.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f surefire-grouper-2.22.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/shared/maven-shared-utils/0.9/maven-shared-utils-0.9.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-shared-utils-0.9.jar" -DgroupId="org.apache.maven.shared" -DartifactId="maven-shared-utils" -Dversion="0.9" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-shared-utils-0.9.jar

      elif [ "$project" == "FasterXML-jackson-dataformats-text" ] && [ "$bug" == "430357777-445435582" ]; then
        # Although the following code is only executed on FasterXML-jackson-dataformats-text::430357777-445435582,
        # it is required for most of the bugs in the FasterXML-jackson-dataformats-text project.  The condition is in
        # place to avoid executing the following code for every single bug in the FasterXML-jackson-dataformats-text project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "jenkinsci-ansicolor-plugin" ] && [ "$bug" == "440438437-449394834" ]; then
        # Although the following code is only executed on jenkinsci-ansicolor-plugin::440438437-449394834,
        # it is required for most of the bugs in the jenkinsci-ansicolor-plugin project.  The condition is in
        # place to avoid executing the following code for every single bug in the jenkinsci-ansicolor-plugin project.

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/extra-enforcer-rules/1.0-beta-4/extra-enforcer-rules-1.0-beta-4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="extra-enforcer-rules-1.0-beta-4.jar" -DgroupId="org.codehaus.mojo" -DartifactId="extra-enforcer-rules" -Dversion="1.0-beta-4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f extra-enforcer-rules-1.0-beta-4.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/plugin/3.25/plugin-3.25.pom
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plugin-3.25.pom" -DpomFile="plugin-3.25.pom" -Dpackaging="pom" -Dmaven.repo.local="$mtwo_dir"
        rm -f plugin-3.25.pom

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/tools/maven-hpi-plugin/2.6/maven-hpi-plugin-2.6.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-hpi-plugin-2.6.jar" -DgroupId="org.jenkins-ci.tools" -DartifactId="maven-hpi-plugin" -Dversion="2.6" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-hpi-plugin-2.6.jar

        wget https://repo.jenkins-ci.org/releases/org/kohsuke/stapler/maven-stapler-plugin/1.17/maven-stapler-plugin-1.17.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-stapler-plugin-1.17.jar" -DgroupId="org.kohsuke.stapler" -DartifactId="maven-stapler-plugin" -Dversion="1.17" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-stapler-plugin-1.17.jar

        wget https://repo.jenkins-ci.org/releases/org/jvnet/localizer/maven-localizer-plugin/1.24/maven-localizer-plugin-1.24.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-localizer-plugin-1.24.jar" -DgroupId="org.jvnet.localizer" -DartifactId="maven-localizer-plugin" -Dversion="1.24" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-localizer-plugin-1.24.jar

        wget https://repositories.tomtom.com/artifactory/maven/org/codehaus/gmaven/runtime/gmaven-runtime-1.6/1.5-jenkins-3/gmaven-runtime-1.6-1.5-jenkins-3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="gmaven-runtime-1.6-1.5-jenkins-3.jar" -DgroupId="org.codehaus.gmaven.runtime" -DartifactId="gmaven-runtime-1.6" -Dversion="1.5-jenkins-3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f gmaven-runtime-1.6-1.5-jenkins-3.jar

        wget https://repositories.tomtom.com/artifactory/maven/org/codehaus/gmaven/gmaven-plugin/1.5-jenkins-3/gmaven-plugin-1.5-jenkins-3.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="gmaven-plugin-1.5-jenkins-3.jar" -DgroupId="org.codehaus.gmaven" -DartifactId="gmaven-plugin" -Dversion="1.5-jenkins-3" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f gmaven-plugin-1.5-jenkins-3.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/workflow/workflow-step-api/2.15/workflow-step-api-2.15.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="workflow-step-api-2.15.jar" -DgroupId="org.jenkins-ci.plugins.workflow" -DartifactId="workflow-step-api" -Dversion="2.15" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f workflow-step-api-2.15.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/workflow/workflow-cps/2.59/workflow-cps-2.59.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="workflow-cps-2.59.jar" -DgroupId="org.jenkins-ci.plugins.workflow" -DartifactId="workflow-cps" -Dversion="2.59" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f workflow-cps-2.59.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/workflow/workflow-job/2.26/workflow-job-2.26.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="workflow-job-2.26.jar" -DgroupId="org.jenkins-ci.plugins.workflow" -DartifactId="workflow-job" -Dversion="2.26" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f workflow-job-2.26.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/workflow/workflow-durable-task-step/2.25/workflow-durable-task-step-2.25.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="workflow-durable-task-step-2.25.jar" -DgroupId="org.jenkins-ci.plugins.workflow" -DartifactId="workflow-durable-task-step" -Dversion="2.25" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f workflow-durable-task-step-2.25.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/workflow/workflow-basic-steps/2.12/workflow-basic-steps-2.12.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="workflow-basic-steps-2.12.jar" -DgroupId="org.jenkins-ci.plugins.workflow" -DartifactId="workflow-basic-steps" -Dversion="2.12" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f workflow-basic-steps-2.12.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/main/jenkins-core/2.121.2/jenkins-core-2.121.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jenkins-core-2.121.2.jar" -DgroupId="org.jenkins-ci.main" -DartifactId="jenkins-core" -Dversion="2.121.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jenkins-core-2.121.2.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/main/jenkins-war/2.121.2/jenkins-war-2.121.2.war
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jenkins-war-2.121.2.war" -DgroupId="org.jenkins-ci.main" -DartifactId="jenkins-war" -Dversion="2.121.2" -Dpackaging="war" -Dmaven.repo.local="$mtwo_dir"
        rm -f jenkins-war-2.121.2.war

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/main/jenkins-test-harness/2.44/jenkins-test-harness-2.44.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jenkins-test-harness-2.44.jar" -DgroupId="org.jenkins-ci.main" -DartifactId="jenkins-test-harness" -Dversion="2.44" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jenkins-test-harness-2.44.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/test-annotations/1.2/test-annotations-1.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="test-annotations-1.2.jar" -DgroupId="org.jenkins-ci" -DartifactId="test-annotations" -Dversion="1.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f test-annotations-1.2.jar

        wget https://repo1.maven.org/maven2/org/jenkins-ci/annotation-indexer/1.4/annotation-indexer-1.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="annotation-indexer-1.4.jar" -DgroupId="org.jenkins-ci" -DartifactId="annotation-indexer" -Dversion="1.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f annotation-indexer-1.4.jar

        wget https://repo1.maven.org/maven2/org/kohsuke/access-modifier-annotation/1.15/access-modifier-annotation-1.15.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="access-modifier-annotation-1.15.jar" -DgroupId="org.kohsuke" -DartifactId="access-modifier-annotation" -Dversion="1.15" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f access-modifier-annotation-1.15.jar

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/jenkins/1.26/jenkins-1.26.pom
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jenkins-1.26.pom" -DpomFile="jenkins-1.26.pom" -Dpackaging="pom" -Dmaven.repo.local="$mtwo_dir"
        rm -f jenkins-1.26.pom

        wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/version-number/1.4/version-number-1.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="version-number-1.4.jar" -DgroupId="org.jenkins-ci" -DartifactId="version-number" -Dversion="1.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f version-number-1.4.jar

      elif [ "$project" == "openmrs-openmrs-module-webservices.rest" ] && [ "$bug" == "455565885-458312291" ]; then
        # Although the following code is only executed on openmrs-openmrs-module-webservices.rest::455565885-458312291,
        # it is required for most of the bugs in the openmrs-openmrs-module-webservices.rest project.  The condition is in
        # place to avoid executing the following code for every single bug in the openmrs-openmrs-module-webservices.rest project.

        wget https://repo1.maven.org/maven2/junit/junit/4.10/junit-4.10.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="junit-4.10.jar" -DgroupId="junit" -DartifactId="junit" -Dversion="4.10" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f junit-4.10.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-plugin-api/2.0.1/maven-plugin-api-2.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-plugin-api-2.0.1.jar" -DgroupId="org.apache.maven" -DartifactId="maven-plugin-api" -Dversion="2.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-plugin-api-2.0.1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-api/1.7/maven-scm-api-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-api-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-api" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-api-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-manager-plexus/1.7/maven-scm-manager-plexus-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-manager-plexus-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-manager-plexus" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-manager-plexus-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-bazaar/1.7/maven-scm-provider-bazaar-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-bazaar-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-bazaar" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-bazaar-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-svnexe/1.7/maven-scm-provider-svnexe-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-svnexe-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-svnexe" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-svnexe-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-gitexe/1.7/maven-scm-provider-gitexe-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-gitexe-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-gitexe" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-gitexe-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-svn-commons/1.7/maven-scm-provider-svn-commons-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-svn-commons-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-svn-commons" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-svn-commons-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-cvsexe/1.7/maven-scm-provider-cvsexe-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-cvsexe-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-cvsexe" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-cvsexe-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-starteam/1.7/maven-scm-provider-starteam-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-starteam-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-starteam" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-starteam-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-clearcase/1.7/maven-scm-provider-clearcase-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-clearcase-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-clearcase" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-clearcase-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-perforce/1.7/maven-scm-provider-perforce-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-perforce-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-perforce" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-perforce-1.7.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/scm/maven-scm-provider-hg/1.7/maven-scm-provider-hg-1.7.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-hg-1.7.jar" -DgroupId="org.apache.maven.scm" -DartifactId="maven-scm-provider-hg" -Dversion="1.7" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-hg-1.7.jar

        wget https://repo1.maven.org/maven2/com/google/code/maven-scm-provider-svnjava/maven-scm-provider-svnjava/1.13/maven-scm-provider-svnjava-1.13.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-scm-provider-svnjava-1.13.jar" -DgroupId="com.google.code.maven-scm-provider-svnjava" -DartifactId="maven-scm-provider-svnjava" -Dversion="1.13" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-scm-provider-svnjava-1.13.jar

        wget https://repo1.maven.org/maven2/org/tmatesoft/svnkit/svnkit/1.3.5/svnkit-1.3.5.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="svnkit-1.3.5.jar" -DgroupId="org.tmatesoft.svnkit" -DartifactId="svnkit" -Dversion="1.3.5" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f svnkit-1.3.5.jar

        wget https://repo1.maven.org/maven2/net/java/dev/jna/jna/3.2.2/jna-3.2.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jna-3.2.2.jar" -DgroupId="net.java.dev.jna" -DartifactId="jna" -Dversion="3.2.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jna-3.2.2.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.13.0/maven-compiler-plugin-3.13.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-compiler-plugin-3.13.0.jar" -DgroupId="org.apache.maven.plugins" -DartifactId="maven-compiler-plugin" -Dversion="3.13.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-compiler-plugin-3.13.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/4.0.0-beta-1/maven-compiler-plugin-4.0.0-beta-1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-compiler-plugin-4.0.0-beta-1.jar" -DgroupId="org.apache.maven.plugins" -DartifactId="maven-compiler-plugin" -Dversion="4.0.0-beta-1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-compiler-plugin-4.0.0-beta-1.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/plugins/maven-jar-plugin/3.4.2/maven-jar-plugin-3.4.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-jar-plugin-3.4.2.jar" -DgroupId="org.apache.maven.plugins" -DartifactId="maven-jar-plugin" -Dversion="3.4.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-jar-plugin-3.4.2.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/plugins/maven-jar-plugin/4.0.0-beta-1/maven-jar-plugin-4.0.0-beta-1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-jar-plugin-4.0.0-beta-1.jar" -DgroupId="org.apache.maven.plugins" -DartifactId="maven-jar-plugin" -Dversion="4.0.0-beta-1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-jar-plugin-4.0.0-beta-1.jar

      elif [ "$project" == "societe-generale-ci-droid-tasks-consumer" ] && [ "$bug" == "420388707-430936160" ]; then
        # Although the following code is only executed on societe-generale-ci-droid-tasks-consumer::420388707-430936160,
        # it is required for most of the bugs in the societe-generale-ci-droid-tasks-consumer project.  The condition is in
        # place to avoid executing the following code for every single bug in the societe-generale-ci-droid-tasks-consumer project.

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar

      elif [ "$project" == "SpoonLabs-gumtree-spoon-ast-diff" ] && [ "$bug" == "431628140-431912220" ]; then
        # Although the following code is only executed on SpoonLabs-gumtree-spoon-ast-diff::420388707-430936160,
        # it is required for most of the bugs in the SpoonLabs-gumtree-spoon-ast-diff project.  The condition is in
        # place to avoid executing the following code for every single bug in the SpoonLabs-gumtree-spoon-ast-diff project.

        for version in 3.12.0 3.13.0 3.13.100 3.13.200 3.13.300 3.13.400 3.13.500 3.13.600 3.21.0; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.resources/$version/org.eclipse.core.resources-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.resources-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.resources" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.resources-$version.jar
        done

        for version in 3.5.100 3.6.0 3.6.100 3.6.200 3.6.300 3.6.400 3.6.500 3.6.600; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.expressions/$version/org.eclipse.core.expressions-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.expressions-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.expressions" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.expressions-$version.jar
        done

        for version in 3.11.2 3.11.3 3.12.0 3.12.100 3.12.50 3.13.0 3.13.100 3.13.200 3.13.300 3.14.0 3.15.0 3.15.100; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.osgi/$version/org.eclipse.osgi-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.osgi-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.osgi" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.osgi-$version.jar
        done

        for version in 3.8.0 3.9.0 3.10.0 3.10.100 3.10.200 3.10.300 3.10.400 3.10.500 3.10.600; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.common/$version/org.eclipse.equinox.common-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.common-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.common" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.equinox.common-$version.jar
        done

        for version in 3.8.0 3.9.0 3.9.1 3.9.2 3.9.3 3.10.0 3.10.100 3.10.200 3.10.300 3.10.400 3.10.500 3.10.600; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.jobs/$version/org.eclipse.core.jobs-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.jobs-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.jobs" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.jobs-$version.jar
        done

        for version in 3.6.100 3.7.0 3.8.0 3.8.100 3.8.200 3.8.300 3.8.400 3.8.500 3.8.600; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.registry/$version/org.eclipse.equinox.registry-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.registry-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.registry" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.equinox.registry-$version.jar
        done

        for version in 3.6.1 3.7.0 3.7.100 3.7.200 3.7.300 3.7.400 3.7.500 3.7.600; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.preferences/$version/org.eclipse.equinox.preferences-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.preferences-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.preferences" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.equinox.preferences-$version.jar
        done

        for version in 3.5.100 3.6.0 3.7.0 3.7.100 3.7.200 3.7.300 3.7.400 3.7.500; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.contenttype/$version/org.eclipse.core.contenttype-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.contenttype-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.contenttype" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.contenttype-$version.jar
        done

        for version in 1.3.400 1.3.500 1.3.600 1.4.0 1.4.100 1.4.200 1.4.300; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.equinox.app/$version/org.eclipse.equinox.app-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.equinox.app-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.equinox.app" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.equinox.app-$version.jar
        done

        for version in 1.6.1 1.7.0 1.7.100 1.7.200 1.7.300 1.7.400 1.7.500 1.7.600 1.11.0; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.filesystem/$version/org.eclipse.core.filesystem-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.filesystem-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.filesystem" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.filesystem-$version.jar
        done

        for version in 3.6.0 3.6.100 3.6.300 3.7.0 3.8.0 3.8.100 3.8.200 3.9.0 3.10.0 3.14.100; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.text/$version/org.eclipse.text-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.text-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.text" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.text-$version.jar
        done

        for version in 3.8.0 3.8.1 3.9.0 3.9.100 3.9.200 3.9.300 3.9.400 3.9.500 3.9.600; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.commands/$version/org.eclipse.core.commands-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.commands-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.commands" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.commands-$version.jar
        done

        for version in 3.12.0 3.13.0 3.14.0 3.15.0 3.15.100 3.15.200 3.15.300 3.16.0 3.17.0 3.31.100; do
          wget https://repo1.maven.org/maven2/org/eclipse/platform/org.eclipse.core.runtime/$version/org.eclipse.core.runtime-$version.jar
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="org.eclipse.core.runtime-$version.jar" -DgroupId="org.eclipse.platform" -DartifactId="org.eclipse.core.runtime" -Dversion="$version" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
          rm -f org.eclipse.core.runtime-$version.jar
        done

        wget https://repo1.maven.org/maven2/log4j/log4j/1.2.17/log4j-1.2.17.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="log4j-1.2.17.jar" -DgroupId="log4j" -DartifactId="log4j" -Dversion="1.2.17" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f log4j-1.2.17.jar

      elif [ "$project" == "vitorenesduarte-VCD-java-client" ] && [ "$bug" == "437204853-437571024" ]; then
        # Although the following code is only executed on vitorenesduarte-VCD-java-client::437204853-437571024,
        # it is required for most of the bugs in the vitorenesduarte-VCD-java-client project.  The condition is in
        # place to avoid executing the following code for every single bug in the vitorenesduarte-VCD-java-client project.

        wget https://repo1.maven.org/maven2/com/google/protobuf/protoc/3.4.0/protoc-3.4.0-linux-x86_64.exe
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="protoc-3.4.0-linux-x86_64.exe" -DgroupId="com.google.protobuf" -DartifactId="protoc" -Dversion="3.4.0" -Dclassifier="linux-x86_64" -Dpackaging="exe" -Dmaven.repo.local="$mtwo_dir"
        rm -f protoc-3.4.0-linux-x86_64.exe

      elif [ "$project" == "HubSpot-Baragon" ] && [ "$bug" == "444834347-445744181" ]; then
        # Although the following code is only executed on HubSpot-Baragon::444834347-445744181,
        # it is required for most of the bugs in the HubSpot-Baragon project.  The condition is in
        # place to avoid executing the following code for every single bug in the HubSpot-Baragon project.

        for item in cglib:cglib:jar:3.1 com.fasterxml.jackson.core:jackson-annotations:jar:2.6.4 com.fasterxml.jackson.core:jackson-annotations:jar:2.7.0 com.fasterxml.jackson.core:jackson-annotations:jar:2.7.8 com.fasterxml.jackson.core:jackson-core:jar:2.1.3 com.fasterxml.jackson.core:jackson-core:jar:2.6.4 com.fasterxml.jackson.core:jackson-core:jar:2.7.8 com.fasterxml.jackson.core:jackson-databind:jar:2.6.4 com.fasterxml.jackson.core:jackson-databind:jar:2.6.6 com.fasterxml.jackson.core:jackson-databind:jar:2.7.6 com.fasterxml.jackson.core:jackson-databind:jar:2.7.8 com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.6.6 com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:jar:2.7.8 com.fasterxml.jackson.datatype:jackson-datatype-guava:jar:2.7.8 com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.7.8 com.fasterxml.jackson.datatype:jackson-datatype-joda:jar:2.7.8 com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.7.8 com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:jar:2.7.8 com.fasterxml.jackson.module:jackson-module-afterburner:jar:2.7.8 com.google.code.findbugs:jsr305:jar:3.0.0 com.google.errorprone:error_prone_annotations:jar:2.0.12 com.google.guava:guava:jar:17.0 com.google.guava:guava-jdk5:jar:17.0 com.google.inject:guice:jar:3.0 commons-codec:commons-codec:jar:1.9 commons-logging:commons-logging:jar:1.2 com.rabbitmq:amqp-client:jar:3.3.5 com.thoughtworks.paranamer:paranamer:jar:2.5.5 io.dropwizard:dropwizard-core:jar:1.0.0 io.dropwizard:dropwizard-jersey:jar:1.0.0 io.dropwizard:dropwizard-jetty:jar:1.0.0 io.dropwizard:dropwizard-lifecycle:jar:1.0.0 io.dropwizard:dropwizard-servlets:jar:1.0.0 io.dropwizard.metrics:metrics-annotation:jar:3.1.0 io.dropwizard.metrics:metrics-core:jar:3.1.0 io.dropwizard.metrics:metrics-healthchecks:jar:3.1.0 io.dropwizard.metrics:metrics-healthchecks:jar:3.1.2 io.netty:netty:jar:3.7.0.Final javax.mail:mail:jar:1.4 jdiff:jdiff:jar:1.0.9 jline:jline:jar:0.9.94 joda-time:joda-time:jar:2.8.1 org.apache.geronimo.specs:geronimo-jms_1.1_spec:jar:1.0 org.apache.httpcomponents:httpclient:jar:4.0.1 org.apache.httpcomponents:httpclient:jar:4.5.1 org.apache.maven:maven-ant-tasks:jar:2.1.3 org.apache.maven.wagon:wagon-http:jar:2.4 org.apache.rat:apache-rat-tasks:jar:0.6 org.codehaus.groovy:groovy-all:jar:2.4.0 org.codehaus.janino:janino:jar:2.7.8 org.eclipse.jetty:jetty-jmx:jar:9.3.9.v20160517 org.glassfish.grizzly:grizzly-websockets:jar:2.3.14 org.glassfish.hk2.external:aopalliance-repackaged:jar:2.5.0-b05 org.glassfish.hk2.external:javax.inject:jar:2.5.0-b05 org.glassfish.hk2:hk2-api:jar:2.4.0-b34 org.glassfish.jersey.containers:jersey-container-servlet-core:jar:2.22.2 org.glassfish.jersey.core:jersey-server:jar:2.23.1 org.hibernate.javax.persistence:hibernate-jpa-2.1-api:jar:1.0.0.Final org.javassist:javassist:jar:3.18.1-GA org.jsoup:jsoup:jar:1.8.3 org.mockito:mockito-core:jar:2.5.7 org.objenesis:objenesis:jar:2.5 org.ow2.asm:asm-debug-all:jar:5.0.4 org.slf4j:jcl-over-slf4j:jar:1.7.12 org.slf4j:slf4j-api:jar:1.6.4 org.slf4j:slf4j-api:jar:1.7.16 org.slf4j:slf4j-api:jar:1.7.20 org.slf4j:slf4j-api:jar:1.7.7 org.slf4j:slf4j-api:jar:1.7.9 org.vafer:jdeb:jar:0.8 xerces:xerces:jar:1.4.4; do
          groupId=$(echo "$item" | cut -f1 -d':')
          groupIdWithSlash=$(echo "$groupId" | tr '.' '/')
          artifactId=$(echo "$item" | cut -f2 -d':')
          version=$(echo "$item" | cut -f4 -d':')
          jar_file="$artifactId-$version.jar"
          echo "$groupId :: $artifactId :: $version = $jar_file"
          wget "https://repo1.maven.org/maven2/$groupIdWithSlash/$artifactId/$version/$jar_file"
          if [ "$?" -ne "0" ]; then
            echo "  FAILED TO GET"
            continue
          fi
          export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file \
            -Dmaven.repo.local="$(pwd)" \
            -Dpackaging="jar" \
            -Dfile="$jar_file" \
            -DgroupId="$groupId" -DartifactId="$artifactId" -Dversion="$version"
        done

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.1/plexus-utils-1.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.1.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.1.jar
      fi
    elif [ "$benchmark" == "Bugs.jar" ]; then

      if [ "$project" == "Accumulo" ] && [ "$bug" == "db4a291f" ]; then
        # Although the following code is only executed on Accumulo::db4a291f,
        # it is required for most of the bugs in the Accumulo project.  The condition is in
        # place to avoid executing the following code for every single bug in the Accumulo project.

        wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-core/0.20.2/hadoop-core-0.20.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="hadoop-core-0.20.2.jar" -DgroupId="org.apache.hadoop" -DartifactId="hadoop-core" -Dversion="0.20.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f hadoop-core-0.20.2.jar

        wget https://repo1.maven.org/maven2/org/mortbay/jetty/jetty/6.1.14/jetty-6.1.14.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jetty-6.1.14.jar" -DgroupId="org.mortbay.jetty" -DartifactId="jetty" -Dversion="6.1.14" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jetty-6.1.14.jar

      elif [ "$project" == "Flink" ] && [ "$bug" == "734ba01d" ]; then

        wget https://repo1.maven.org/maven2/org/scalamacros/paradise_2.10.4/2.0.1/paradise_2.10.4-2.0.1.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="paradise_2.10.4-2.0.1.jar" -DgroupId="org.scalamacros" -DartifactId="paradise_2.10.4" -Dversion="2.0.1" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f paradise_2.10.4-2.0.1.jar

        wget https://repo1.maven.org/maven2/commons-beanutils/commons-beanutils/1.8.0/commons-beanutils-1.8.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="commons-beanutils-1.8.0.jar" -DgroupId="commons-beanutils" -DartifactId="commons-beanutils" -Dversion="1.8.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f commons-beanutils-1.8.0.jar

        wget https://repo1.maven.org/maven2/javax/servlet/jsp-api/2.0/jsp-api-2.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="jsp-api-2.0.jar" -DgroupId="javax.servlet" -DartifactId="jsp-api" -Dversion="2.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f jsp-api-2.0.jar

        wget https://repo1.maven.org/maven2/commons-io/commons-io/1.2/commons-io-1.2.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="commons-io-1.2.jar" -DgroupId="commons-io" -DartifactId="commons-io" -Dversion="1.2" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f commons-io-1.2.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-plugin-api/2.0/maven-plugin-api-2.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-plugin-api-2.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-plugin-api" -Dversion="2.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-plugin-api-2.0.jar

        wget https://repo1.maven.org/maven2/org/apache/maven/maven-artifact/2.0/maven-artifact-2.0.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="maven-artifact-2.0.jar" -DgroupId="org.apache.maven" -DartifactId="maven-artifact" -Dversion="2.0" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f maven-artifact-2.0.jar

        wget https://repo1.maven.org/maven2/org/codehaus/plexus/plexus-utils/1.0.4/plexus-utils-1.0.4.jar
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="plexus-utils-1.0.4.jar" -DgroupId="org.codehaus.plexus" -DartifactId="plexus-utils" -Dversion="1.0.4" -Dpackaging="jar" -Dmaven.repo.local="$mtwo_dir"
        rm -f plexus-utils-1.0.4.jar

      elif [ "$project" == "Maven" ] && [ "$bug" == "a7d9b689" ]; then

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/signature/java15/1.0/java15-1.0.pom
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="java15-1.0.pom" -DpomFile="java15-1.0.pom" -Dpackaging="pom" -Dmaven.repo.local="$mtwo_dir"
        rm -f java15-1.0.pom

        wget https://repo1.maven.org/maven2/org/codehaus/mojo/signature/java15/1.0/java15-1.0.signature
        export JAVA_HOME="$REPAIR_THEM_ALL_FRAMEWORK_DIR/jdks/jdk1.8.0_181" && mvn install:install-file -Dfile="java15-1.0.signature" -DgroupId="org.codehaus.mojo.signature" -DartifactId="java15" -Dversion="1.0" -Dpackaging="signature" -Dmaven.repo.local="$mtwo_dir"
        rm -r java15-1.0.signature

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
