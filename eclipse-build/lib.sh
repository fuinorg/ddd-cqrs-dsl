#!/usr/bin/env bash
#
# Shared paths and helpers for the pure-PDE (no Maven/Tycho) headless build of the
# Eclipse plugins under ../eclipse. Sourced by provision.sh / build.sh / test.sh / publish.sh.
#
# All directories are derived from the repository root so the scripts work the same
# locally and on a GitHub Actions runner.

set -euo pipefail

# --- Locations -------------------------------------------------------------------------

# Absolute path of the eclipse-build directory (this file lives there).
EB_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Repository root (parent of eclipse-build).
REPO_ROOT="$(cd "$EB_DIR/.." && pwd)"

# Where the provisioned build Eclipse + downloads are cached. Starts with a dot, so it is
# already covered by the repo .gitignore. Overridable for CI caching.
CACHE_DIR="${ECLIPSE_BUILD_CACHE:-$REPO_ROOT/.eclipse-build-cache}"
DOWNLOAD_DIR="$CACHE_DIR/downloads"
ECLIPSE_HOME="$CACHE_DIR/eclipse"

# PDE build working area, final categorized p2 site and test results (all gitignored).
WORK_DIR="$EB_DIR/work"
REPO_OUT="$EB_DIR/repository"
TEST_RESULTS="$EB_DIR/test-results"

# Marker file written by build.sh with the full version (e.g. 1.0.0.202606071200).
VERSION_FILE="$EB_DIR/.build-version"

# --- Versions (overridable via environment) --------------------------------------------

# Eclipse release train the plugins target (see README.md "Eclipse 2026-03").
ECLIPSE_RELEASE="${ECLIPSE_RELEASE:-2026-03}"

# Eclipse SDK tarball. The SDK ships PDE build, JDT and the p2 director used below.
# NOTE: the drop sub-directory (R-4.xx-<timestamp>) changes per release; verify/override
# ECLIPSE_SDK_URL if the download 404s. 2026-03 == Eclipse 4.39, drop R-4.39-202602260420.
# (Find a drop's timestamp via the child <child location='R-4.xx-...'> in
#  https://download.eclipse.org/eclipse/updates/<ver>/compositeContent.jar)
ECLIPSE_SDK_VERSION="${ECLIPSE_SDK_VERSION:-4.39}"
ECLIPSE_SDK_DROP="${ECLIPSE_SDK_DROP:-R-4.39-202602260420}"
ECLIPSE_SDK_URL="${ECLIPSE_SDK_URL:-https://download.eclipse.org/eclipse/downloads/drops4/${ECLIPSE_SDK_DROP}/eclipse-SDK-${ECLIPSE_SDK_VERSION}-linux-gtk-x86_64.tar.gz}"

# Xtext version the plugins are built against (matches the bundle versions in the manifests).
XTEXT_VERSION="${XTEXT_VERSION:-2.42.0}"

# p2 update sites used to provision the build Eclipse.
RELEASE_REPO="${RELEASE_REPO:-https://download.eclipse.org/releases/${ECLIPSE_RELEASE}}"
XTEXT_REPO="${XTEXT_REPO:-https://download.eclipse.org/modeling/tmf/xtext/updates/releases/${XTEXT_VERSION}}"

# --- Helpers ---------------------------------------------------------------------------

log() { printf '\n\033[1;34m==> %s\033[0m\n' "$*"; }
die() { printf '\n\033[1;31mERROR: %s\033[0m\n' "$*" >&2; exit 1; }

# Path to the Equinox launcher jar inside the provisioned Eclipse.
launcher_jar() {
  local jar
  jar="$(ls "$ECLIPSE_HOME"/plugins/org.eclipse.equinox.launcher_*.jar 2>/dev/null | sort | tail -1)" || true
  [ -n "$jar" ] || die "Equinox launcher not found in $ECLIPSE_HOME. Run provision.sh first."
  printf '%s' "$jar"
}

# Directory of the installed org.eclipse.pde.build bundle (holds scripts/build.xml).
pde_build_dir() {
  local dir
  dir="$(ls -d "$ECLIPSE_HOME"/plugins/org.eclipse.pde.build_* 2>/dev/null | sort | tail -1)" || true
  [ -n "$dir" ] || die "org.eclipse.pde.build not found in $ECLIPSE_HOME. Run provision.sh first."
  printf '%s' "$dir"
}

# Run an Eclipse application headlessly via the launcher jar.
# Usage: run_eclipse_app <application-id> [args...]
#
# -Declipse.p2.mirrors=false forces p2 to download straight from the canonical
# download.eclipse.org instead of following redirects to geographic mirrors. Some
# of those mirrors (e.g. mirror.ibcp.fr) serve certificates the JDK trust store
# cannot validate, which otherwise breaks provisioning with a PKIX path error.
run_eclipse_app() {
  local app="$1"; shift
  java -Declipse.p2.mirrors=false -jar "$(launcher_jar)" -nosplash -consoleLog -data "$CACHE_DIR/ws" \
    -application "$app" "$@"
}
