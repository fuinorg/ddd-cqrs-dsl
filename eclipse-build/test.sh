#!/usr/bin/env bash
#
# Run the plugins' JUnit 5 tests during the headless build -- WITHOUT Maven/Tycho and
# without the heavyweight Eclipse Test Framework.
#
#   ./eclipse-build/test.sh
#
# The DSL's tests (org.fuin.dsl.cqrs.tests) are standalone Xtext tests (@InjectWith +
# Guice + ParseHelper); they do not need a running Eclipse/OSGi workbench. So we:
#   1. Build a classpath from the JUnit 5 + Xtext jars already in the provisioned
#      Eclipse, plus the freshly built runtime plugin jars (eclipse-build/repository).
#   2. Compile the test bundle's generated Java (src-gen + xtend-gen; the .xtend is
#      already translated to Java and checked in).
#   3. Run them with a tiny JUnit Platform launcher (support/JUnitLauncher.java).
#
# org.fuin.dsl.cqrs.ui.tests currently contains no @Test classes, so it is not run.

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/lib.sh"

PLUGINS="$ECLIPSE_HOME/plugins"
TEST_CLASSES="$WORK_DIR/test-classes"
LAUNCHER_CLASSES="$WORK_DIR/launcher-classes"

# Test bundles to compile and run (extend this list as tests are added).
TEST_BUNDLES=(org.fuin.dsl.cqrs.tests)

[ -d "$PLUGINS" ] || die "Provisioned Eclipse not found; run build.sh first."
[ -d "$REPO_OUT/plugins" ] || die "Built plugins not found at $REPO_OUT; run build.sh first."

mkdir -p "$TEST_RESULTS" "$TEST_CLASSES" "$LAUNCHER_CLASSES"
rm -f "$TEST_RESULTS"/*.xml 2>/dev/null || true
rm -rf "$TEST_CLASSES"/* "$LAUNCHER_CLASSES"/* 2>/dev/null || true

# --- 1. Build the classpath --------------------------------------------------------------
# The SDK ships two JUnit lines (5.x and 6.x); the tests target [5.1,6.0), so pin 5.x
# explicitly and exclude all junit/opentest4j/apiguardian jars from the bulk Xtext deps.
log "Assembling the test classpath"
PINNED_JUNIT=(
  junit-jupiter-api_5.14.3.jar
  junit-jupiter-engine_5.14.3.jar
  junit-jupiter-params_5.14.3.jar
  junit-platform-commons_1.14.3.jar
  junit-platform-engine_1.14.3.jar
  junit-platform-launcher_1.14.3.jar
  org.opentest4j_1.3.0.jar
  org.apiguardian.api_1.1.2.jar
)

CP=""
for j in "${PINNED_JUNIT[@]}"; do
  [ -f "$PLUGINS/$j" ] || die "Expected JUnit jar missing: $PLUGINS/$j (SDK version drift? adjust PINNED_JUNIT)."
  CP="$CP:$PLUGINS/$j"
done
# Bulk Xtext/EMF/Guice/etc. deps (exclude sources + every junit-ish jar handled above).
while IFS= read -r j; do CP="$CP:$j"; done < <(
  find "$PLUGINS" -maxdepth 1 -name '*.jar' \
    ! -name '*.source_*' ! -iname '*junit*' ! -iname '*opentest4j*' ! -iname '*apiguardian*'
)
# The plugins under test.
while IFS= read -r j; do CP="$CP:$j"; done < <(find "$REPO_OUT/plugins" -maxdepth 1 -name '*.jar')
CP="${CP#:}"

# --- 2. Compile the test bundle(s) -------------------------------------------------------
# Compile the generated Java (src-gen + xtend-gen; the .xtend is already translated and checked
# in) plus any hand-written Java test helpers under src (e.g. TarGzTestSupport) that the
# generated tests reference. The .xtend sources in src are skipped (only *.java is collected).
log "Compiling test sources for: ${TEST_BUNDLES[*]}"
JAVAC_SOURCES=()
for b in "${TEST_BUNDLES[@]}"; do
  for root in src src-gen xtend-gen; do
    [ -d "$REPO_ROOT/eclipse/$b/$root" ] || continue
    while IFS= read -r f; do JAVAC_SOURCES+=("$f"); done < <(find "$REPO_ROOT/eclipse/$b/$root" -name '*.java')
  done
done
[ "${#JAVAC_SOURCES[@]}" -gt 0 ] || die "No compiled test Java found under the test bundles' src-gen/xtend-gen."

# The generated Java is checked in, and the Eclipse Xtend builder overwrites it. When the .xtend
# does not compile in the IDE - a missing Export-Package makes a type "not accessible", say - what
# it writes is a stub that throws at runtime. That compiles cleanly here and fails as a puzzling
# test error, so name it for what it is instead, before running anything.
POISONED=$(grep -rl "Unresolved compilation problems" "${JAVAC_SOURCES[@]}" 2>/dev/null || true)
if [ -n "$POISONED" ]; then
  die "Generated Java carries an Eclipse compile error - fix it in the IDE and regenerate:
$POISONED"
fi

# Never compile on top of an earlier run: a class of a test that has since been renamed or removed
# would still be found and run.
rm -rf "$TEST_CLASSES"
javac --release 21 -encoding UTF-8 -cp "$CP" -d "$TEST_CLASSES" "${JAVAC_SOURCES[@]}" \
  || die "Compiling the test sources failed."

# --- 3. Compile + run the JUnit launcher -------------------------------------------------
log "Running JUnit 5 tests"
javac --release 21 -cp "$CP" -d "$LAUNCHER_CLASSES" "$EB_DIR/support/JUnitLauncher.java" \
  || die "Compiling the JUnit launcher failed."

set +e
java -cp "$LAUNCHER_CLASSES:$TEST_CLASSES:$CP" JUnitLauncher "$TEST_CLASSES" "$TEST_RESULTS/junit.xml"
rc=$?
set -e

log "Test report: $TEST_RESULTS/junit.xml"
[ "$rc" -eq 0 ] || die "JUnit tests failed."
log "All tests passed."
