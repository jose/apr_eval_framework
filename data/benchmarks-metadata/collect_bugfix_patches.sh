#!/usr/bin/env bash
# ------------------------------------------------------------------------------
#
# This script collects the bugfix patch of all defects on Bears, Defects4J, and
# QuixBugs.
#
# Note 1: the bugfixes of defects on Bugs.jar are automatically collected by
# the Bugs.jar/get_metadata.sh script, as that info is already available in the
# benchmark.
#
# Note 2: as discussed in [here](https://github.com/Spirals-Team/IntroClassJava/issues/6))
# there is no fix version of each defect on IntroClassJava, and therefore it is
# not possible to compute the bugfix patch.
#
# Usage:
# ./collect_bugfix_patches.sh
#
# ------------------------------------------------------------------------------

SCRIPT_DIR=$(cd `dirname $0` && pwd)

#
# Print error message and exit
#
die() {
  echo "$@" >&2
  exit 1
}

# ------------------------------------------------------------------ Envs & Args

BUGS_FILE="$SCRIPT_DIR/../../benchmarks/bugs.csv"
[ -s "$BUGS_FILE" ] || die "$BUGS_FILE does not exist or it is empty!"

USAGE="Usage: ${BASH_SOURCE[0]}"
[ "$#" -eq "0" ] || die "$USAGE"

# ------------------------------------------------------------------------- Main

# for benchmark in "Bears" "Defects4J" "QuixBugs"; do
for benchmark in "Defects4J"; do
  pushd . > /dev/null 2>&1
  cd "$benchmark" || die "Failed to switch to '$benchmark'!"
    ./get_bugfix_patches.sh || die "Failed to run get_bugfix_patches.sh on $benchmark!"
  popd > /dev/null 2>&1
done

echo "DONE!"
exit 0
