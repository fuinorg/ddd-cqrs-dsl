# CQRS DSL — IntelliJ IDEA Plugin

A standalone IntelliJ IDEA plugin that provides editor support for the DDD/CQRS DSL (`*.cqrs` files).
It is self-contained in this `intellij/` folder and has **no** dependency on the `eclipse/`, `maven/`
or `templates/` projects — it ships its own copy of the grammar (a JFlex lexer and a Grammar-Kit
parser) reproducing the language defined by the Xtext grammar.

## Features

- **Syntax highlighting** — keywords, identifiers, strings, numbers, line/block/doc comments, braces.
- **Code completion** — context-aware DSL keywords plus declared names (types, value-objects,
  entities, aggregates, events, exceptions, …).
- **Navigation** — go-to-definition (`Ctrl/Cmd+B`), find-usages, rename, and a structure view.
- **Remote references** — resolves cross-references against remote `.cqrs` models declared in a
  `.remote-scope.json` catalog (see below), interoperable with the Eclipse plugin's on-disk cache.
- Brace matching, line/block commenting (`Ctrl/Cmd+/`) and a color settings page
  (`Settings | Editor | Color Scheme | CQRS DSL`).

## Requirements

- JDK 25 to build and run the plugin.
- IntelliJ IDEA 2026.1 (or newer) as the target/runtime platform.

> The Grammar-Kit code generator runs against an older IntelliJ core that needs **JDK 21**, so the
> `generateCqrsParser`/`generateCqrsLexer` tasks use a Java 21 toolchain. Gradle finds it
> automatically (SDKMAN/standard installs) or provisions it via the Foojay resolver, so a JDK 25-only
> machine builds fine. CI installs both JDK 21 and 25.

## Build

```bash
cd intellij
./gradlew buildPlugin
```

The installable plugin zip is produced under `build/distributions/`.

## Try it in a sandbox IDE

```bash
./gradlew runIde
```

Open or create a `.cqrs` file and start editing.

## Run the tests / verifier

```bash
./gradlew test          # parses every example model with zero parse errors; remote-cache contract
./gradlew verifyPlugin  # IntelliJ Plugin Verifier
```

## Install into your IDE

**From the JFrog Artifactory repository (recommended):**

1. `Settings | Plugins | ⚙ | Manage Plugin Repositories…`
2. Add: `https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl/cqrs-dsl-intellij/latest/updatePlugins.xml`
3. Find **CQRS DSL** on the Marketplace tab and click *Install*.

**From a local build:**

`Settings | Plugins | ⚙ | Install Plugin from Disk…` and pick the zip from `build/distributions/`.

## Publishing to Artifactory

`./gradlew publishToArtifactory` uploads the plugin zip and an `updatePlugins.xml` descriptor to the
JFrog Artifactory generic repository, both under an immutable `<version>/` path and a stable
`latest/` path. Consumers register `…/latest/updatePlugins.xml` as a plugin repository (above).

Credentials are read from the environment (the same ones the Eclipse plugin uses); the upload targets
are overridable:

| Variable | Default | Purpose |
|----------|---------|---------|
| `P2_USER` | — (required) | Artifactory user |
| `P2_TOKEN` | — (required) | Artifactory token/password |
| `ARTIFACTORY_BASE` | `https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl` | Repository base URL |
| `ARTIFACTORY_PATH` | `cqrs-dsl-intellij` | Sub-path within the repository |

`updatePlugins.xml` is also generated on every `./gradlew buildPlugin` (under
`build/distributions/`). In CI the publish step runs only on pushes to `main`.

## Remote references (`.remote-scope.json`)

By default cross-references resolve against `.cqrs` files in the project. To also resolve against a
remote model addressed by URL, add a `.remote-scope.json` catalog to your project (it is discovered
by walking up the directory tree from the model being edited). It is a JSON **array of single-entry
objects** mapping a fully qualified `context.namespace` to the URL of the model that provides it:

```json
[
  { "com.acme.billing":  "http://models.acme.com/billing.cqrs" },
  { "com.acme.shipping": "file:/shared/shipping.cqrs" }
]
```

- The **key** is the provided namespace exactly as written in an `import` (a trailing `.*` is
  ignored, so `import a.b` and `import a.b.*` both match the entry `a.b`).
- The **value** is the URL of the remote `.cqrs` file (`http(s):` and `file:` are supported).

Downloaded models are cached next to the catalog under `.remote-scope-cache/` (an `index.json` plus
files named `<namespace>-<sha1(url)>.cqrs`), so editing keeps working **offline** after the first
fetch. A cached `file:` source is refreshed when it becomes newer; for an unchanged `http(s):` URL
whose content changed, delete the cached file to force a refresh. This is the same layout the Eclipse
plugin uses, so the two can share a cache directory.

| System property | Default | Effect |
|-----------------|---------|--------|
| `cqrs.remote.scope.file` | `.remote-scope.json` | Name of the catalog file to look for. |

Downloads happen on a background thread; while a remote model is being fetched its names resolve once
the download lands (the editor refreshes automatically). Any failure (missing catalog, offline and
not yet cached, parse error) degrades gracefully to local-only resolution.

## License

LGPL v3 — see the repository root.
