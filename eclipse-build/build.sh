#!/usr/bin/env bash
#
# Headless, pure-PDE build of the org.fuin.dsl.cqrs plugins into a categorized p2
# update site. No Maven, no Tycho. Works locally and on a GitHub Actions runner.
#
#   ./eclipse-build/build.sh
#
# Environment:
#   ECLIPSE_BUILD_CACHE  override the provisioned-Eclipse cache dir
#   BUILD_QUALIFIER      override the version qualifier (default: UTC timestamp)
#   SKIP_TESTS=1         skip the headless test run (build the site only)
#
# Output: eclipse-build/repository/  (the p2 update site)
#         eclipse-build/.build-version  (the full version string)

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/lib.sh"

# --- 1. Ensure the build Eclipse is provisioned ----------------------------------------
"$EB_DIR/provision.sh"

# --- 2. Determine the version --------------------------------------------------------
QUALIFIER="${BUILD_QUALIFIER:-$(date -u +%Y%m%d%H%M)}"
VERSION="1.0.0.${QUALIFIER}"
log "Building version ${VERSION}"

# --- 3. Assemble the PDE build workspace ---------------------------------------------
# PDE build expects source projects under <buildDirectory>/plugins and /features.
log "Assembling build workspace at $WORK_DIR"
rm -rf "$WORK_DIR"
mkdir -p "$WORK_DIR/plugins" "$WORK_DIR/features"

# Runtime + test plugins (test plugins are needed so test.sh can build/run them; the
# feature only references the 3 runtime plugins, so only those land in the site).
for p in org.fuin.dsl.cqrs \
         org.fuin.dsl.cqrs.ide \
         org.fuin.dsl.cqrs.ui \
         org.fuin.dsl.cqrs.tests \
         org.fuin.dsl.cqrs.ui.tests; do
  cp -r "$REPO_ROOT/eclipse/$p" "$WORK_DIR/plugins/$p"
done
cp -r "$REPO_ROOT/eclipse/org.fuin.dsl.cqrs.feature" "$WORK_DIR/features/org.fuin.dsl.cqrs.feature"

# Drop IDE-only state that would confuse the headless compiler.
find "$WORK_DIR" -name bin -type d -prune -exec rm -rf {} + 2>/dev/null || true

# --- 4. Run the headless PDE build ---------------------------------------------------
log "Running org.eclipse.pde.build (antRunner)"
java -jar "$(launcher_jar)" -nosplash -consoleLog \
  -application org.eclipse.ant.core.antRunner \
  -data "$CACHE_DIR/ws" \
  -buildfile "$(pde_build_dir)/scripts/build.xml" \
  -Dbuilder="$EB_DIR/config" \
  -DbaseLocation="$ECLIPSE_HOME" \
  -DbuildDirectory="$WORK_DIR" \
  -DforceContextQualifier="$QUALIFIER" \
  || die "PDE build failed."

[ -f "$WORK_DIR/buildRepo/content.xml" ] || die "PDE build produced no p2 repository at $WORK_DIR/buildRepo."

# --- 5. Apply the categories from category.xml ---------------------------------------
log "Publishing categories into the final update site"
rm -rf "$REPO_OUT"
cp -r "$WORK_DIR/buildRepo" "$REPO_OUT"

run_eclipse_app org.eclipse.equinox.p2.publisher.CategoryPublisher \
  -metadataRepository "file:$REPO_OUT" \
  -categoryDefinition "file:$REPO_ROOT/eclipse/org.fuin.dsl.cqrs.repository/category.xml" \
  -categoryQualifier org.fuin.dsl.cqrs \
  -compress \
  || die "CategoryPublisher failed."

# Compress the artifact index too, so the published site is tidy (content.jar + artifacts.jar).
if [ -f "$REPO_OUT/artifacts.xml" ] && [ ! -f "$REPO_OUT/artifacts.jar" ]; then
  ( cd "$REPO_OUT" && zip -q artifacts.jar artifacts.xml && rm -f artifacts.xml )
fi

printf '%s\n' "$VERSION" > "$VERSION_FILE"
log "Update site ready: $REPO_OUT (version $VERSION)"

# --- 6. Run the headless tests -------------------------------------------------------
if [ "${SKIP_TESTS:-0}" = "1" ]; then
  log "SKIP_TESTS=1 -> skipping headless tests"
else
  "$EB_DIR/test.sh" "$VERSION"
fi

log "Build complete."
