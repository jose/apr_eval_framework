#!/usr/bin/env bash

OS_NAME=$(uname -s | tr "[:upper:]" "[:lower:]")
if [[ $OS_NAME != *"linux"* ]]; then
  echo "'$OS_NAME' not supported!"
  exit 1
fi

# from https://askubuntu.com/questions/916976/bash-one-liner-to-check-if-version-is
version_checker() {
    local ver1=$1
    
    while [ `echo $ver1 | egrep -c [^0123456789.]` -gt 0 ]; do
        char=`echo $ver1 | sed 's/.*\([^0123456789.]\).*/\1/'`
        char_dec=`echo -n "$char" | od -b | head -1 | awk {'print $2'}`
        ver1=`echo $ver1 | sed "s/$char/.$char_dec/g"`
    done 
  
    local ver2=$2
    while [ `echo $ver2 | egrep -c [^0123456789.]` -gt 0 ]; do
        char=`echo $ver2 | sed 's/.*\([^0123456789.]\).*/\1/'`
        char_dec=`echo -n "$char" | od -b | head -1 | awk {'print $2'}`
        ver2=`echo $ver2 | sed "s/$char/.$char_dec/g"`
    done    

    ver1=`echo $ver1 | sed 's/\.\./.0/g'`
    ver2=`echo $ver2 | sed 's/\.\./.0/g'`
    
    do_version_check "$ver1" "$ver2"
}

do_version_check() {

    [ "$1" == "$2" ] && return 10

    ver1front=`echo $1 | cut -d "." -f -1`
    ver1back=`echo $1 | cut -d "." -f 2-`
    ver2front=`echo $2 | cut -d "." -f -1`
    ver2back=`echo $2 | cut -d "." -f 2-`

    if [ "$ver1front" != "$1" ] || [ "$ver2front" != "$2" ]; then
        [ "$ver1front" -gt "$ver2front" ] && return 11
        [ "$ver1front" -lt "$ver2front" ] && return 9

        [ "$ver1front" == "$1" ] || [ -z "$ver1back" ] && ver1back=0
        [ "$ver2front" == "$2" ] || [ -z "$ver2back" ] && ver2back=0
        do_version_check "$ver1back" "$ver2back"
        return $?
    else
        [ "$1" -gt "$2" ] && return 11 || return 9
    fi
}

perl_version=`perl -e 'print $];'`
do_version_check "$perl_version" "5.0.10" 
[[ $? -eq 9 ]] && echo "[Error] Perl version >= 5.0.10" && exit 1 ;

which wget > /dev/null
[[ $? -eq 1 ]] && echo "[Error] wget not installed" && exit 1 ;

do_version_check "`svn --version --quiet`" "1.8" 
[[ $? -eq 9 ]] && echo "[Error] snv version >= 1.8" && exit 1 ;

version_checker "`git version | sed "s/git version //"`" "1.9" 
[[ $? -eq 9 ]] && echo "[Error] git version >= 1.9" && exit 1 ;

which gradle > /dev/null
[[ $? -eq 1 ]] && echo "[Error] gradle not installed" && exit 1 ;

which time > /dev/null
[[ $? -eq 1 ]] && echo "[Error] time not installed" && exit 1 ;

which cpanm > /dev/null
[[ $? -eq 1 ]] && echo "[Error] cpanm not installed" && exit 1 ;

# Install Perl packages
cpanm --local-lib=~/perl5 local::lib && eval $(perl -I ~/perl5/lib/perl5/ -Mlocal::lib)
cpanm --installdeps .

# Setup Python
./init-python.sh || exit 1
# Set Python version
source "env/bin/activate" || exit 1

# Get Java-7 and Java-8
./init-java.sh
# Set Java-8 for the remaining of the script
export JAVA_HOME="$(pwd)/jdks/jdk1.8.0_181"
export PATH="$JAVA_HOME/bin:$PATH"

git submodule init;
git submodule update;
cd benchmarks/Bug-dot-jar/;
git submodule init;
git submodule update;
cd ../defects4j;
./init.sh
cd ../../

# Get maven .m2 per benchmark/project
./init-mvn.sh

# Z3
cd libs/z3
python scripts/mk_make.py --java
cd build
make
cd ../../../

# Compile utility project
cd data/benchmarks-metadata/util/collect_test_classes/
mvn clean package

echo "DONE!"
exit 0
