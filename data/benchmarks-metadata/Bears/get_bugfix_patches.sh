#!/usr/bin/env bash
# ------------------------------------------------------------------------------
#
# This script collects bugfixes of all bugs in the Bears benchmark.
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

repository_dir="/tmp/bears-$$"
rm -rf "$repository_dir"

repository_url="https://github.com/bears-bugs/bears-benchmark.git"
git clone "$repository_url" "$repository_dir" || die "Failed to clone $repository_url to $repository_dir!"

while read -r line; do
  pid=$(echo "$line" | cut -f2 -d',')
  bid=$(echo "$line" | cut -f3 -d',')
  echo "project: $pid :: bug: $bid"

  local_metadata_dir="$SCRIPT_DIR/$pid/$bid/"
  mkdir -p "$local_metadata_dir" || die "Failed to create $local_metadata_dir!"
  bugfix_file="$local_metadata_dir/developer-patch.diff"
  rm -f "$bugfix_file" # Clean up, if exists

  pushd . > /dev/null 2>&1
  cd "$repository_dir"
    # Get pid-bid branch
    commit_id="$pid-$bid"
    git checkout "$commit_id" || die "Failed to checkout commit $commit_id!"

    # Get first commit in that branch
    first_commit=$(git rev-list --max-parents=0 HEAD)

    # Diff first commit (i.e., the buggy version) with HEAD (the fixed version)
    git diff $first_commit '***.java' > "$bugfix_file" || die "Failed to get the bugfix of $commit_id!"
    [ -s "$bugfix_file" ] || die "$bugfix_file does not exist or it is empty!"
  popd > /dev/null 2>&1
done < <(grep "Bears," "$BUGS_FILE")

rm -rf "$repository_dir" # Clean up

echo "DONE!"
exit 0
