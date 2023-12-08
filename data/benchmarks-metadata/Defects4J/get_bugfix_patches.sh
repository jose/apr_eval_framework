#!/usr/bin/env bash
# ------------------------------------------------------------------------------
#
# This script collects bugfixes of all bugs in the Defects4J benchmark.
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

D4J_HOME="$SCRIPT_DIR/../../../benchmarks/defects4j"
[ -d "$D4J_HOME" ] || die "$D4J_HOME does not exist!"
export PATH="$D4J_HOME/framework/bin:$PATH"
defects4j info -p Lang > /dev/null 2>&1 || die "defects4j <cmd> does work!" # Sanity check

USAGE="Usage: ${BASH_SOURCE[0]}"
[ "$#" -eq "0" ] || die "$USAGE"

# ------------------------------------------------------------------------- Main

while read -r line; do
  # benchmark,project_name,bug_id,repair_them_all_id
  # Defects4J,Chart,1,Chart-1
  pid=$(echo "$line" | cut -f2 -d',')
  bid=$(echo "$line" | cut -f3 -d',')
  echo "project: $pid :: bug: $bid"

  local_metadata_dir="$SCRIPT_DIR/$pid/$bid"
  mkdir -p "$local_metadata_dir" || die "Failed to create $local_metadata_dir!"
  bugfix_file="$local_metadata_dir/developer-patch.diff"
  #rm -f "$bugfix_file" # Clean up, if exists
  if [ -f "$bugfix_file" ]; then
    # Ignore if it has been computed
    continue;
  fi

  work_dir="/tmp/$pid-$bid-$$"
  rm -rf "$work_dir"
  buggy_work_dir="$work_dir/buggy"
  fixed_work_dir="$work_dir/fixed"

  defects4j checkout -p "$pid" -v "${bid}b" -w "$buggy_work_dir" || die "Failed to checkout the buggy version of $pid::$bid!"
  defects4j checkout -p "$pid" -v "${bid}f" -w "$fixed_work_dir" || die "Failed to checkout the fixed version of $pid::$bid!"

  pushd . > /dev/null 2>&1
  cd "$buggy_work_dir"
    dir_src_classes=$(defects4j export -pdir.src.classes)
  popd > /dev/null 2>&1

  # Diff buggy againts fixed
  diff -u -r "$buggy_work_dir/$dir_src_classes" "$fixed_work_dir/$dir_src_classes" > "$bugfix_file" # || die "Failed to get the bugfix of $pid::$bid!"
  [ -s "$bugfix_file" ] || die "$bugfix_file does not exist or it is empty!"

  # Remove path to temporary directory so that the diff is working directory-based
  sed -i "s|$buggy_work_dir/||g" "$bugfix_file" || die "Failed to remove the path of the temporary directory from the patch!"
  sed -i "s|$fixed_work_dir/||g" "$bugfix_file" || die "Failed to remove the path of the temporary directory from the patch!"

  # Remove diff call from the patch
  sed -i '/^diff -u -r /d' "$bugfix_file" || die "Failed to remove the diff call from the patch!"

  rm -rf "$work_dir"
done < <(grep "Defects4J," "$BUGS_FILE")

echo "DONE!"
exit 0
