#!/usr/bin/env bash
#
# Regenerate the CqrsDsl Xtext language infrastructure from the command line.
#
# Command-line equivalent of the Eclipse launch configuration
#   eclipse/org.fuin.dsl.cqrs/.launch/Generate CqrsDsl (cqrs) Language Infrastructure.launch
#
# It runs the MWE2 workflow "org.fuin.dsl.cqrs.GenerateCqrsDsl" via Mwe2Launcher (-Xmx512m),
# regenerating src-gen / xtext-gen / plugin.xml / MANIFEST sections etc. into the
# eclipse/org.fuin.dsl.cqrs* projects. The workflow's default `rootPath = ".."` resolves
# against the org.fuin.dsl.cqrs project directory (the script's working directory), i.e. the
# eclipse/ folder.
#
# Classpath: the bundles of an Eclipse/Xtext install act as the target platform, exactly like
# the launch configuration does. By default the headless build's provisioned Eclipse is used
# (.eclipse-build-cache/eclipse, created by eclipse-build/provision.sh). Override the location:
#
#   ECLIPSE_HOME=/path/to/eclipse  ./generate-language-infrastructure.sh
#
# Pass extra Mwe2 arguments through, e.g. send the output elsewhere for a dry run:
#
#   ./generate-language-infrastructure.sh -p rootPath=/tmp/cqrs-gen
#
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"   # eclipse/
PROJECT_DIR="$SCRIPT_DIR/org.fuin.dsl.cqrs"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MWE2_MODULE="org.fuin.dsl.cqrs.GenerateCqrsDsl"

ECLIPSE_HOME="${ECLIPSE_HOME:-${ECLIPSE_BUILD_CACHE:-$REPO_ROOT/.eclipse-build-cache}/eclipse}"
PLUGINS_DIR="$ECLIPSE_HOME/plugins"
if [ ! -d "$PLUGINS_DIR" ]; then
	echo "ERROR: Eclipse plugins not found at: $PLUGINS_DIR" >&2
	echo "Provision the build Eclipse once:   ./eclipse-build/provision.sh" >&2
	echo "or point ECLIPSE_HOME at an Eclipse install that has the Xtext SDK." >&2
	exit 1
fi

# Build the classpath from the target-platform bundles (skipping *.source_ bundles), and put the
# project's source folder first so the grammar (CqrsDsl.xtext) and the workflow
# (GenerateCqrsDsl.mwe2) are found as classpath resources.
CP="$PROJECT_DIR/src"
for jar in "$PLUGINS_DIR"/*.jar; do
	case "$jar" in
		*.source_*.jar) ;;          # skip source bundles
		*) CP="$CP:$jar" ;;
	esac
done

JAVA_BIN="${JAVA_HOME:+$JAVA_HOME/bin/}java"

cd "$PROJECT_DIR"
echo "Generating CqrsDsl language infrastructure into $REPO_ROOT/eclipse ..."
exec "$JAVA_BIN" -Xmx512m -cp "$CP" \
	org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher "$MWE2_MODULE" "$@"
