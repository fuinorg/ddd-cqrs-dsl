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
- **Dependencies** — resolves cross-references against the `.cqrs` models of a Maven artifact
  declared with `dependency "groupId:artifactId:version"` (see below), interoperable with the
  Eclipse plugin's on-disk cache.
- **Code folding** — collapse any multi-line `{ }` block (context, module, aggregate,
  constructor, `instances`, JSON hints, …) and multi-line block/doc comments. Fold at the caret with
  `Ctrl/Cmd+-`, everything with `Ctrl/Cmd+Shift+-`.
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

## Name resolution

A model has two levels: a `context` holding one or more `module` blocks. Every element lives in a
module, and a module name is a qualified name, so a nested grouping is written with a dot
(`receipts.receiptview`).

A **module is the unit of visibility**: only what it declares itself resolves by a simple name. To
reach anything else — a sibling module of the very same context included — it needs an `import`:

```
context de.fuin.melkheftken {

    module receipts {
        import de.fuin.melkheftken.categories.*      // every type of one module
        import de.fuin.melkheftken.journal.TaxRate   // a single type
        ...
    }
}
```

The imported name is written over the `context.module.Type` path and may end in a wildcard on any
level: `ctx.*` pulls in every module of a context, `ctx.mod.*` every type of one module, and
`ctx.mod.Type` a single type. An import declared on the `context` applies to every module below it —
but a context block is per file, so it only reaches the modules written in that same file.

A simple name resolves against the closest scope that declares it — the enclosing module first, then
whatever is imported. Only that closest scope is used, so reusing a name such as `TaxRate` in several
modules is unambiguous: each module sees its own. A **fully qualified** name
(`de.fuin.melkheftken.journal.TaxRate`) always resolves and needs no import at all.

A type that is neither declared in the module nor imported does not resolve and is marked red, exactly
as the Eclipse plugin and the build report it. Code completion follows the same rule: it offers only
types that are in scope, and after `import` the reachable contexts, modules and types.

An import that matches nothing and a duplicate import are errors; an unused import is a warning.

## Dependencies

To reach types of *another* project, declare the artifact that provides them on a `context` or on a
`module`. A module also inherits every dependency of its context. A dependency makes those models
**resolvable**; an `import` still decides which of their types are visible.

```
context de.fuin.melkheftken {

    dependency "org.fuin.dsl.cqrs.contexts:cqrs-common-model:0.1.0-SNAPSHOT"

    module receipts {
        dependency "org.acme:wip-model:0.0.1-SNAPSHOT" local "../wip-model/src/main/cqrs"
        import org.fuin.dsl.cqrs.common.types.*
        ...
    }
}
```

- The coordinate is `groupId:artifactId:version` and identifies an ordinary Maven artifact — a jar,
  no classifier — holding the `.cqrs` files under `model/`. It is resolved by **the IDE's own Maven**
  (the bundled Maven plugin), so your `settings.xml` — local repository, remote repositories, mirrors,
  servers and proxies — applies exactly as it does for a Maven project. Every module the artifact
  declares becomes importable.
- `local` (optional) points at a directory of `.cqrs` files, relative to the model that declares the
  dependency when not absolute. Those files are read **directly** instead of downloading the
  artifact — handy while developing a model that is not published yet.

**Nothing is unpacked.** The models are read in place, out of the jar in the local repository: the
artifact is mounted with the IDE's `JarFileSystem`, so the entries below `model/` are real virtual
files — go-to-definition lands inside the jar, and find-usages works on them. Only `model/` counts,
taken recursively; anything else in the jar is ignored. There is no `.dependencies-cache/` any more.

The local repository is the only cache. What a coordinate resolved to — and why it failed — is
remembered for the session, so a bad coordinate is attempted once rather than on every keystroke; an
artifact that appears later is picked up after a restart.

Resolution happens on a background thread; while a dependency is being fetched its names resolve once
the download lands (the editor refreshes automatically). Any failure (malformed coordinate, offline
and not yet in the local repository, parse error) degrades gracefully to local-only resolution and is
reported on the coordinate.

> **Requires the bundled Maven plugin** (`org.jetbrains.idea.maven`), declared in `plugin.xml`. It
> ships with IntelliJ IDEA; if it is disabled the DSL plugin cannot resolve a `dependency`.

## License

LGPL v3 — see the repository root.
