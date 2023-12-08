#!/usr/bin/env bash
# ------------------------------------------------------------------------------
#
# This script collects bugfixes of all bugs in the QuixBugs benchmark.
#
# Usage:
# ./get_bugfix_patches.sh
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

BUGS_FILE="$SCRIPT_DIR/../../../benchmarks/bugs.csv"
[ -s "$BUGS_FILE" ] || die "$BUGS_FILE does not exist or it is empty!"

USAGE="Usage: ${BASH_SOURCE[0]}"
[ "$#" -eq "0" ] || die "$USAGE"

# ------------------------------------------------------------------------- Main

repository_dir="/tmp/quixbugs-$$"
rm -rf "$repository_dir"

repository_url="https://github.com/jkoppel/QuixBugs"
git clone "$repository_url" "$repository_dir" || die "Failed to clone $repository_url to $repository_dir!"

while read -r line; do
  # benchmark,project_name,bug_id,repair_them_all_id
  # QuixBugs,POSSIBLE_CHANGE,,POSSIBLE_CHANGE
  pid=$(echo "$line" | cut -f2 -d',')
  echo "project: $pid"

  local_metadata_dir="$SCRIPT_DIR/$pid/"
  mkdir -p "$local_metadata_dir" || die "Failed to create $local_metadata_dir!"
  bugfix_file="$local_metadata_dir/developer-patch.diff"
  rm -f "$bugfix_file" # Clean up, if exists

  buggy_file="$repository_dir/java_programs/$pid.java"
  fixed_file="$repository_dir/correct_java_programs/$pid.java"
  [ -s "$buggy_file" ] || die "$buggy_file does not exist or it is empty!"
  [ -s "$fixed_file" ] || die "$fixed_file does not exist or it is empty!"

  # Diff buggy againts fixed
  diff -u "$buggy_file" "$fixed_file" > "$bugfix_file" || die "Failed to get the bugfix of $pid!"
  [ -s "$bugfix_file" ] || die "$bugfix_file does not exist or it is empty!"
  # Remove path to temporary directory so that the diff is working directory-based
  sed -i "s|$repository_dir/||g" "$bugfix_file"
done < <(grep "QuixBugs," "$BUGS_FILE")

rm -rf "$repository_dir" # Clean up

echo "DONE!"
exit 0
