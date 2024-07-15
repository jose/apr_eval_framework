#!/usr/bin/env bash

SCRIPT_DIR="$(pwd)"

#
# Print error message to the stdout and exit.
#
die() {
  echo "$@" >&2
  exit 1
}

major=2
minor=7
micro=18

echo ""
echo "Setting up pyenv..."

PYENV_DIR="$SCRIPT_DIR/pyenv"

# Remove any previous file and directory
rm -rf "$PYENV_DIR"

git clone https://github.com/pyenv/pyenv.git "$PYENV_DIR"
if [ "$?" -ne "0" ] || [ ! -d "$PYENV_DIR" ]; then
  die "[ERROR] Clone of 'pyenv' failed!"
fi

pushd . > /dev/null 2>&1
cd "$PYENV_DIR"
  # Switch to 'v2.3.35' branch/tag
  git checkout v2.3.35 || die "[ERROR] Branch/Tag 'v2.3.35' not found!"
popd > /dev/null 2>&1

export PYENV_ROOT="$PYENV_DIR"
export PATH="$PYENV_ROOT/bin:$PATH"

# Check whether 'pyenv' is (now) available
pyenv --version > /dev/null 2>&1 || die "[ERROR] Could not find 'pyenv' to setup Python's virtual environment!"
# Init it
eval "$(pyenv init --path)" || die "[ERROR] Failed to init pyenv!"

echo ""
echo "Installing required Python versions..."

# Install v2.7.18
pyenv install -v "$major.$minor.$micro"

# Switch to the version just installed
pyenv local "$major.$minor.$micro" || die "[ERROR] Python $major.$minor.$micro is not available to pyenv!"

python_version=$(python --version 2>&1)
if [ "$python_version" != "Python $major.$minor.$micro" ]; then
  die "[ERROR] System is still using '$python_version' instead of $major.$minor.$micro!"
fi

# Ensure pip, setuptools, wheel, and cython are up to date
pip install --upgrade pip setuptools wheel cython

echo ""
echo "Installing up virtualenv..."

# Install virtualenv
pip install virtualenv                          || die "[ERROR] Failed to install 'virtualenv'!"
# Runtime sanity check
virtualenv --version > /dev/null 2>&1           || die "[ERROR] Could not find 'virtualenv'!"
# Create virtual environment
rm -rf "$SCRIPT_DIR/env"
virtualenv -p $(which python) "$SCRIPT_DIR/env" || die "[ERROR] Failed to create virtual environment!"
# Activate virtual environment
source "$SCRIPT_DIR/env/bin/activate"           || die "[ERROR] Failed to activate virtual environment!"
# Ensure pip, setuptools, and wheel are up to date
pip install --upgrade pip setuptools wheel      || die "[ERROR] Failed to upgrade 'pip', 'setuptools', and 'wheel'!"
# Install filelock
pip install filelock                            || die "[ERROR] Failed to install 'filelock'!"
# Deactivate virtual environment
deactivate                                      || die "[ERROR] Failed to deactivate virtual environment!"
# Revert to system Python version
rm ".python-version"                            || die
