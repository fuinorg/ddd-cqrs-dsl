# CqrsDsl Eclipse plugins (`eclipse/`)

The Xtext language plugins for the DDD/CQRS DSL:

| Project | Role |
|---------|------|
| `org.fuin.dsl.cqrs`         | Runtime: grammar, generated model, scoping, validation, generator. |
| `org.fuin.dsl.cqrs.ide`     | Generic IDE support (content assist parser). |
| `org.fuin.dsl.cqrs.ui`      | Eclipse editor (outline, labels, quickfixes). |
| `org.fuin.dsl.cqrs.tests` / `org.fuin.dsl.cqrs.ui.tests` | JUnit 5 tests. |
| `org.fuin.dsl.cqrs.feature` / `org.fuin.dsl.cqrs.repository` | Feature + p2 packaging. |

The headless build that turns these into a p2 update site lives in
[`../eclipse-build`](../eclipse-build).

---

## Regenerating the language infrastructure

The build compiles the checked-in `src-gen`/`xtend-gen` **as-is** — it never runs the Xtext
generator. When you change the grammar
([`CqrsDsl.xtext`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDsl.xtext)), regenerate the
infrastructure (EMF model, ANTLR parser, `src-gen`, `plugin.xml`, manifests, …) first.

In the IDE you do this with the launch config *Generate CqrsDsl (cqrs) Language Infrastructure*; on the command line
use the equivalent script in this folder:

```bash
./eclipse/generate-language-infrastructure.sh
```

It runs the MWE2 workflow `org.fuin.dsl.cqrs.GenerateCqrsDsl` (`Mwe2Launcher`, `-Xmx512m`) and writes
into the `eclipse/org.fuin.dsl.cqrs*` projects — exactly what the launch config does. The classpath is
taken from the bundles of an Eclipse/Xtext install acting as the target platform; by default it reuses
the provisioned Eclipse from the headless build (`.eclipse-build-cache/eclipse`), so run
[`../eclipse-build/provision.sh`](../eclipse-build/provision.sh) once first.

| Variable / argument | Effect |
|---------------------|--------|
| `ECLIPSE_HOME=…` | Use a different Eclipse install (must include the Xtext SDK) instead of the provisioned cache. |
| `-p rootPath=/tmp/out` | Pass-through MWE2 arg; sends the generated output elsewhere for a dry run instead of overwriting the projects. |

> After regenerating, the Xtext generator may rewrite tracked `src-gen`/`plugin.xml`/`MANIFEST.MF`
> files — review the diff and commit it. It manages `Require-Bundle`; manual `Import-Package` entries
> (e.g. `com.google.gson`, see [below](#resolving-references-against-remote-maven-addressed-models)) are
> left untouched.

---

## Resolving references against remote (Maven-addressed) models

By default Xtext resolves cross-references (`[Type|FQN]`, `[Exception|FQN]`, …) only against models
that live in the workspace. `CqrsDslGlobalScopeProvider` extends that so a model can reference types
defined in another `.cqrs` model declared in a catalog. The source is a `maven` artifact (classifier
`cqrs`, type `tar.gz`) whose archive bundles one or more `.cqrs` models — typically a shared, centrally
published model — or, as an override, a `local` directory of `.cqrs` files read directly.

### How it works

Scoping never fetches anything itself; it resolves names against the EMF `Resource`s that are already
loaded. So the feature has two halves — *get the remote model loaded*, and *make its elements visible
to scoping* — split across three classes in
[`org.fuin.dsl.cqrs/.../scoping`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/scoping):

| Class | Responsibility |
|-------|----------------|
| `RemoteScopeCatalog`        | Reads the `dependencies.json` catalog and answers *namespace → typed source* (`RemoteScopeEntry`). |
| `RemoteScopeCache`          | Materializes the remote `.cqrs` model(s) — resolving + unpacking a `maven` artifact (or reading a `local` directory directly) — caches each artifact once per GAV under `.dependencies-cache/`, and serves them from disk on later runs. |
| `MavenArtifactResolver` / `TarGz` | Resolve a Maven artifact (local `~/.m2` first, then Maven Central / Sonatype Snapshots) and unpack its `tar.gz`. JDK only — no Aether or Commons Compress. |
| `CqrsDslGlobalScopeProvider`| The `IGlobalScopeProvider`. For every `import` it consults the catalog/cache and adds the remote model's elements to the global scope. |

For each `import` in a model, the provider takes the **imported namespace**, looks it up in the
catalog, loads the matching remote model (from the local cache, or downloading it once on a miss), and
exposes its objects **by fully qualified name** on top of the normal local scope. The catalog declares
*where a namespace lives*, so the same entry serves every model that imports it — the importing context
is irrelevant. Because the elements are added *directly* — not through the workspace index, which never
contains a remote resource — resolution behaves identically in the **Eclipse editor** and in the
**headless / standalone generator**. The provider is bound once in
[`CqrsDslRuntimeModule`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDslRuntimeModule.xtend), which both
contexts share.

A coordinate that is **not** in the catalog adds nothing, so resolution falls back to the standard
file-based mechanism. Any failure (missing catalog, offline and not yet cached, parse error) is logged
and degrades to the local scope, so editing and generation never break.

> Requires the `com.google.gson` bundle on the classpath (declared via `Import-Package` in
> `org.fuin.dsl.cqrs/META-INF/MANIFEST.MF`; it ships with the Xtext SDK / Orbit target platform).

### Configuration

**1. The catalog — `dependencies.json`**

Place it in your project root (it is discovered by walking up the directory tree from the model being
edited). It is a JSON **array** of typed objects, each declaring the fully qualified `namespaces` it
provides, a `type` discriminator (always `maven`) and a `data` block:

```json
[
  { "type": "maven", 
    "namespaces": ["com.acme.billing", "com.acme.catalog"],
    "data": { 
      "groupId": "org.fuin.dsl.cqrs.contexts",
      "artifactId": "cqrs-billing-model", 
      "version": "0.1.0-SNAPSHOT"
    }
  },
  { "type": "maven", 
    "namespaces": ["dev.workinprogress"],
    "data": { 
      "groupId": "org.acme", 
      "artifactId": "wip-model", 
      "version": "0.0.1-SNAPSHOT",
      "local": "../wip-model/src/main/cqrs"
    }
  }
]
```

- `namespaces` lists the provided namespaces — the fully qualified `context.namespace` values exactly
  as they are written in an `import`. They say *where those namespaces live*, independent of who imports
  them, so one entry serves every importing model. Listing several namespaces in one entry is handy
  because a single Maven artifact often holds more than one context and namespace. A trailing `.*` is
  ignored, so `import a.b` and `import a.b.*` match the entry that lists `a.b`.
- `data.groupId` / `data.artifactId` / `data.version` identify a Maven artifact with classifier
  `cqrs` and type `tar.gz`. The artifact is resolved from the local repository (`~/.m2/repository`)
  first, otherwise downloaded from Maven Central (releases) or Sonatype Snapshots (`-SNAPSHOT`
  versions), and every `.cqrs` file in the archive is unpacked.
- `data.local` (optional) is a local directory of `.cqrs` files (relative to the catalog when not
  absolute). When set, those files are read **directly** from that folder instead of downloading the
  artifact — handy while developing a model that is not published yet.

The catalog is re-read automatically when the file's modification time changes, so edits take effect on
the next reconcile — **no Eclipse restart needed**. (Adding a catalog where none existed before is the
one exception: a model that previously found no catalog still needs a restart.)

**2. The cache — `.dependencies-cache/`**

Created automatically next to `dependencies.json`. On first use its `index.json` (entries of
`{ source, dir }`) is read into memory for fast lookups; each Maven artifact is stored once in a
sub-directory `<artifactId>-<version>-<sha1(gav)>/` holding every `.cqrs` unpacked from the archive.
Keying by the Maven coordinate (not by namespace) means an artifact that provides several namespaces
is unpacked only once and shared by all of them. After the first fetch everything is served from disk,
so editing keeps working **offline**.

```
<project root>/
├── dependencies.json
└── .dependencies-cache/
    ├── index.json
    └── cqrs-billing-model-0.1.0-SNAPSHOT-3f9a1c…​/
        ├── billing.cqrs
        └── catalog.cqrs
```

A `maven` artifact (including a re-published `-SNAPSHOT`) is treated as up to date once cached; delete
the entry's cache directory (or the whole `.dependencies-cache/`) to force a refresh. A `local`
directory is read directly and never cached.

**3. Optional system property**

| Property | Default | Effect |
|----------|---------|--------|
| `cqrs.dependencies.file` | `dependencies.json` | Name of the catalog file to look for. |

### Worked example

Remote model published as the Maven artifact
`org.fuin.dsl.cqrs.contexts:cqrs-common-model:0.1.0-SNAPSHOT` (classifier `cqrs`, type `tar.gz`),
bundling a `billing.cqrs`:

```
context com.acme {
    namespace billing {
        type Money
    }
}
```

`dependencies.json` in the local project root:

```json
[ { "type": "maven", "namespaces": ["com.acme.billing"],
    "data": { "groupId": "org.fuin.dsl.cqrs.contexts",
              "artifactId": "cqrs-common-model", "version": "0.1.0-SNAPSHOT" } } ]
```

(Or, while developing that model locally, point at its source folder instead — its `.cqrs` files are
read directly, no build or publish needed:)

```json
[ { "type": "maven", "namespaces": ["com.acme.billing"],
    "data": { "groupId": "org.fuin.dsl.cqrs.contexts", "artifactId": "cqrs-common-model",
              "version": "0.1.0-SNAPSHOT", "local": "../cqrs-common-model/src/main/cqrs" } } ]
```

Local model that references the remote `Money` type:

```
context com.acme.sales {
    namespace sales {
        import com.acme.billing.*
        value-object Price {
            Money amount
        }
    }
}
```

`Money`'s fully qualified name is `com.acme.billing.Money` (context `com.acme` + namespace `billing` +
`Money`). The `import com.acme.billing.*` therefore makes it visible as the simple name `Money`, and
the catalog entry for the namespace `com.acme.billing` tells the provider where to download the model
defining it. The cross-reference resolves exactly as if `Money` were a local type — `F3` navigates into
the cached copy, and content assist proposes it.

> The import path must match the remote type's FQN prefix, just as it would for a local model. The
> provider only handles *loading*; name resolution stays standard Xtext.

## Version history

- **1.3.0** — A keyword can now be used as an identifier by prefixing it with a caret (e.g.
  `^event`). The caret marks the following word as a plain identifier and is not part of the name.
- **1.2.0** — The dependency catalog is now Maven-only (the `simple` URL source type was removed).
  Artifacts are cached once per Maven coordinate (GAV) instead of per namespace, so an artifact that
  provides several namespaces is downloaded and unpacked only once. A new `local` directory field
  reads `.cqrs` models straight from a folder without downloading.
- **1.1.0** — Remote scope catalog with typed entries (`simple` URL and `maven` `tar.gz` sources).
