#!/usr/bin/env bash
#
# Upload the built p2 update site (eclipse-build/repository) to the JFrog Artifactory
# Generic repository with curl. Artifactory has no native p2 type, so the static p2
# files are simply PUT under a versioned path and mirrored to a stable latest/ path.
#
#   ./eclipse-build/publish.sh
#
# Required environment (never hard-coded):
#   P2_USER   Artifactory user            (CI: repo variable;  local: eclipse-build/.env)
#   P2_TOKEN  Artifactory token/password  (CI: repo secret;    local: eclipse-build/.env)
#
# Optional overrides (defaults target the fuinorg JFrog "ddd-cqrs-dsl" repo):
#   ARTIFACTORY_BASE   default https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl
#   ARTIFACTORY_PATH   sub-path within the repo, default cqrs-dsl
#
# Consumer update-site URL: <ARTIFACTORY_BASE>/<ARTIFACTORY_PATH>/latest

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/lib.sh"

# Load local, untracked credentials if present (CI sets them in the environment instead).
[ -f "$EB_DIR/.env" ] && set -a && . "$EB_DIR/.env" && set +a

: "${P2_USER:?Set P2_USER (Artifactory user) -- e.g. in eclipse-build/.env or a CI variable}"
: "${P2_TOKEN:?Set P2_TOKEN (Artifactory token) -- e.g. in eclipse-build/.env or a CI secret}"

ARTIFACTORY_BASE="${ARTIFACTORY_BASE:-https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl}"
ARTIFACTORY_PATH="${ARTIFACTORY_PATH:-cqrs-dsl}"

[ -d "$REPO_OUT" ] || die "No update site at $REPO_OUT. Run build.sh first."
VERSION="$(cat "$VERSION_FILE" 2>/dev/null || echo unknown)"

# Upload every file under the update site to a destination prefix, preserving structure.
upload_tree() {
  local dest_prefix="$1" f rel url code
  while IFS= read -r -d '' f; do
    rel="${f#"$REPO_OUT"/}"
    url="$ARTIFACTORY_BASE/$ARTIFACTORY_PATH/$dest_prefix/$rel"
    code="$(curl -sS -u "$P2_USER:$P2_TOKEN" -T "$f" -o /dev/null -w '%{http_code}' "$url")"
    case "$code" in
      2*) printf '  %s -> %s\n' "$rel" "$code" ;;
      *)  die "Upload failed (HTTP $code) for $rel -> $url" ;;
    esac
  done < <(find "$REPO_OUT" -type f -print0)
}

log "Publishing version $VERSION to $ARTIFACTORY_BASE/$ARTIFACTORY_PATH"
log "Uploading versioned copy: $ARTIFACTORY_PATH/$VERSION"
upload_tree "$VERSION"
log "Uploading stable 'latest' copy: $ARTIFACTORY_PATH/latest"
upload_tree "latest"

log "Done. Update site URL: $ARTIFACTORY_BASE/$ARTIFACTORY_PATH/latest"
