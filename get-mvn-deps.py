#!/usr/bin/env python
# ------------------------------------------------------------------------------
#
# This script collects all maven dependencies of a given benchmark, project, and
# bug from the maven central repository.
# 
#
# Usage:
# ./get-mvn-deps.py
#   --benchmark <benchmark>
#   --project <project>
#   --bug <bug>
#   --mtwo_dir <path>
#
# Requirements:
#   export PYTHONPATH=$REPAIR_THEM_ALL_FRAMEWORK_DIR/script:$PYTHONPATH
#
# ------------------------------------------------------------------------------

import argparse
import os
import sys
import traceback
import shutil

import utils

# RTA imports
from config     import JAVA8_HOME
from core.utils import run_cmd, get_benchmark

# ------------------------------------------------------------------ Envs & Args

parser = argparse.ArgumentParser(prog="get-mvn-deps", description='Download all maven dependencies from the maven central repository')
parser.add_argument("--benchmark",   required=True, help="Benchmark name, e.g., Bugs.jar")
parser.add_argument("--project",     required=True, help="Project name, e.g., Commons-Math")
parser.add_argument("--bug",         required=True, help="Bug id, e.g., 7")
parser.add_argument("--mtwo_dir",    required=True, help=".m2 directory")

# Parse arguments
args        = parser.parse_args()
benchmark   = args.benchmark
project     = args.project
bug         = args.bug
mtwo_dir    = args.mtwo_dir

# ------------------------------------------------------------------------- Main

# Create required objects for RTA framework
benchmarkObj = get_benchmark(benchmark)
bugObj       = benchmarkObj.get_bug(project if benchmark == "QuixBugs" else project + "-" + bug)
CHECKOUT_DIR = os.path.join("/tmp", benchmark + "_" + project + "_" + bug)
utils.remove_checkout_dir(CHECKOUT_DIR)

#
# Print error message, remove checkout directory (if exists) and exit.
#
def die(msg=""):
    utils.die(msg=msg, checkout_dir=CHECKOUT_DIR)

#
# Checkout
#
try:
    checkout_exit_code = utils.checkout(bugObj, CHECKOUT_DIR, False, True)
    if checkout_exit_code != 0:
        die()
except:
    traceback.print_exc()
    die()

#
# Get all dependencies
#

if str(benchmark) == "Bear" and str(project) == "dhis2-dhis2-core" and str(bug) == "365246679-365386294":
    fix_pom_cmd = "sed -i 's|http://download.osgeo.org/webdav/geotools/|https://repo.osgeo.org/repository/release/|g' %s/dhis-2/pom.xml" %(CHECKOUT_DIR)
    run_cmd(fix_pom_cmd, sys.stdout, sys.stderr)

cmd  = "export JAVA_HOME=\"%s\"; export PATH=\"$JAVA_HOME/bin:$PATH\";" %(JAVA8_HOME)
cmd += " cd \"%s\"; find . -type f -name pom.xml | while read -r pom_file; do dir=$(dirname \"$pom_file\"); (cd \"$dir\"; mvn -V -B -U -Dhttps.protocols=TLSv1.2 dependency:go-offline -Dmaven.repo.local=\"%s\" --fail-never; mvn -V -B -U -Dhttps.protocols=TLSv1.2 de.qaware.maven:go-offline-maven-plugin:1.2.8:resolve-dependencies -Dmaven.repo.local=\"%s\" --fail-never;) done" %(CHECKOUT_DIR, mtwo_dir)
cmd_exit_code = run_cmd(cmd, sys.stdout, sys.stderr)

# Clean up checkout directory
utils.remove_checkout_dir(CHECKOUT_DIR)

sys.exit(cmd_exit_code)
