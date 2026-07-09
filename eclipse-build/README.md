# Eclipse plugin build (`eclipse-build/`)

Headless, **pure-PDE** build of the Xtext plugins under [`../eclipse`](../eclipse) into a
[p2 update site](https://wiki.eclipse.org/Equinox/p2), published to a
[JFrog Artifactory](https://jfrog.com/artifactory/) Generic repository. **No Maven or Tycho** is
involved, and nothing under [`../maven`](../maven) is touched. The same scripts run locally and in
the [`eclipse.yml`](../.github/workflows/eclipse.yml) GitHub Action.

## What it produces

A categorized p2 update site containing the `org.fuin.dsl.cqrs.feature` and its three runtime
plugins, versioned `<feature-version>.<UTC-timestamp>` (e.g. `1.5.0.202606071200`, where the base
version is read from [`feature.xml`](../eclipse/org.fuin.dsl.cqrs.feature/feature.xml)), uploaded to:

```
https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl/cqrs-dsl/<version>/   # immutable, per build
https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl/cqrs-dsl/latest/      # stable consumer URL
```

Install in Eclipse via *Help → Install New Software…* pointing at the `…/cqrs-dsl/latest` URL.

## Scripts

| Script | Purpose |
|--------|---------|
| `provision.sh` | Download the Eclipse SDK and p2-install Xtext SDK + EMF SDK into `.eclipse-build-cache/` (idempotent). |
| `build.sh`     | Assemble the workspace, run the `org.eclipse.pde.build` headless build, apply `category.xml`, then run tests. **Main entry point.** |
| `test.sh`      | Compile and run the standalone Xtext JUnit 5 tests off the JUnit + Xtext jars (no Eclipse Test Framework, no Xvfb). Called by `build.sh`. |
| `support/JUnitLauncher.java` | Tiny JUnit Platform launcher used by `test.sh` (writes `test-results/junit.xml`). |
| `publish.sh`   | Upload `repository/` to Artifactory with curl. |
| `config/build.properties` | Master PDE-build configuration. |

## Run locally

```bash
# 1. Build the update site (provisions Eclipse on first run; downloads a few hundred MB).
./eclipse-build/build.sh
#    -> eclipse-build/repository/   (the p2 site)
#    -> eclipse-build/test-results/ (JUnit XML)

# 2. Publish it.
cp eclipse-build/.env.example eclipse-build/.env   # then set P2_USER / P2_TOKEN
./eclipse-build/publish.sh
```

Requirements: a JDK (21+; CI uses 21) and `bash`/`curl`/`tar`/`zip`.

Useful environment variables:

| Variable | Effect |
|----------|--------|
| `SKIP_TESTS=1` | Build the site only, skip the headless tests. |
| `BUILD_QUALIFIER=…` | Override the version qualifier (default: `date -u +%Y%m%d%H%M`). |
| `ECLIPSE_BUILD_CACHE=…` | Relocate the provisioned-Eclipse cache. |
| `ECLIPSE_SDK_URL=…` | Override the Eclipse SDK download (the drop path changes per release). |

## CI

[`eclipse.yml`](../.github/workflows/eclipse.yml) runs `build.sh` on every push/PR touching
`eclipse/**` or `eclipse-build/**`, caches `.eclipse-build-cache/`, and runs `publish.sh` only on
push to `main`. It needs a repo **variable** `P2_USER` and a repo **secret** `P2_TOKEN`, and the
Generic repo `ddd-cqrs-dsl` to exist on `fuinorg.jfrog.io`.

## Notes

- `org.fuin.dsl.cqrs.ui.tests` currently has no `@Test` classes, so only `org.fuin.dsl.cqrs.tests`
  is run. Add new test bundles to `TEST_BUNDLES` in `test.sh`. Should a future test genuinely need
  a running workbench (SWT/UI), it would need the Eclipse Test Framework instead of this
  lightweight runner.
- The Eclipse SDK drop path changes per release; if `provision.sh` 404s, find the current drop via
  the `<child location='R-4.xx-…'>` entry in
  `https://download.eclipse.org/eclipse/updates/<ver>/compositeContent.jar` and set
  `ECLIPSE_SDK_DROP`/`ECLIPSE_SDK_URL`.
- The grammar is **not** regenerated here — the checked-in `src-gen`/`xtend-gen` sources are
  compiled as-is, matching the IDE workflow described in the top-level [`README`](../README.md). To
  regenerate after a grammar change, see *Regenerating the language infrastructure* in the
  [`../eclipse` README](../eclipse/README.md#regenerating-the-language-infrastructure).
  Or run the [Generate CqrsDsl (cqrs) Language Infrastructure](../eclipse/org.fuin.dsl.cqrs/.launch/Generate CqrsDsl (cqrs) Language Infrastructure.launch)
  launch configuration inside the Eclipse IDE.
