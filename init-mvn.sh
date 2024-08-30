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
    -Dmaven.repo.local="$mvn_deps_dir"
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

  done < <(tail -n +2 "$bugs_file")

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
