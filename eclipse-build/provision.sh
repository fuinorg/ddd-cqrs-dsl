#!/usr/bin/env bash
#
# Provision a self-contained build Eclipse capable of compiling and assembling the
# org.fuin.dsl.cqrs plugins into a p2 update site WITHOUT Maven/Tycho.
#
# Steps:
#   1. Download the Eclipse SDK (ships PDE build, JDT, p2 director) into the cache.
#   2. Use the p2 director to install, into that same Eclipse, the features needed to
#      compile the plugins and resolve their dependencies:
#        - Xtext SDK  (org.eclipse.xtext.* / xbase / xtend.lib / antlr 3.2)
#        - EMF SDK    (transitive Xtext dependency)
#
# Tests do not need anything extra here: the DSL's tests are standalone Xtext/JUnit 5
# tests that test.sh runs straight off the JUnit + Xtext jars this step installs, so
# the heavyweight Eclipse Test Framework is never required.
#
# Idempotent: re-running with a populated cache is a no-op, which is what makes CI
# caching of .eclipse-build-cache effective.

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/lib.sh"

mkdir -p "$DOWNLOAD_DIR"

# --- 1. Eclipse SDK --------------------------------------------------------------------

if [ ! -x "$ECLIPSE_HOME/eclipse" ] && [ ! -f "$ECLIPSE_HOME/eclipse" ]; then
  log "Downloading Eclipse SDK $ECLIPSE_SDK_VERSION"
  tarball="$DOWNLOAD_DIR/eclipse-sdk.tar.gz"
  if [ ! -f "$tarball" ]; then
    curl -fSL --retry 3 -o "$tarball" "$ECLIPSE_SDK_URL" \
      || die "Failed to download Eclipse SDK from $ECLIPSE_SDK_URL (override ECLIPSE_SDK_URL if the drop path changed)."
  fi
  log "Extracting Eclipse SDK"
  mkdir -p "$ECLIPSE_HOME"
  # The tarball contains a top-level 'eclipse/' dir; strip it into ECLIPSE_HOME.
  tar -xzf "$tarball" -C "$ECLIPSE_HOME" --strip-components=1
else
  log "Eclipse SDK already present at $ECLIPSE_HOME"
fi

# --- 2. Install required features via the p2 director ----------------------------------

# Marker so the (slow) director step is skipped once everything is installed.
PROVISION_MARKER="$ECLIPSE_HOME/.provisioned-${XTEXT_VERSION}"

if [ ! -f "$PROVISION_MARKER" ]; then
  log "Installing Xtext SDK $XTEXT_VERSION and EMF SDK"
  run_eclipse_app org.eclipse.equinox.p2.director \
    -repository "${RELEASE_REPO},${XTEXT_REPO}" \
    -installIU "org.eclipse.xtext.sdk.feature.group,org.eclipse.emf.sdk.feature.group" \
    -destination "$ECLIPSE_HOME" \
    -profile SDKProfile \
    || die "p2 director install failed (check that $XTEXT_REPO and $RELEASE_REPO are reachable)."
  touch "$PROVISION_MARKER"
else
  log "Required features already installed (marker $PROVISION_MARKER)"
fi

log "Provisioning complete: $ECLIPSE_HOME"
