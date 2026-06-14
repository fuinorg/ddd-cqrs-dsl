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
> (e.g. `com.google.gson`, see [below](#resolving-references-against-remote-url-addressed-models)) are
> left untouched.

---

## Resolving references against remote (URL-addressed) models

By default Xtext resolves cross-references (`[Type|FQN]`, `[Exception|FQN]`, …) only against models
that live in the workspace. `CqrsDslGlobalScopeProvider` extends that so a model can reference types
defined in another `.cqrs` model that is declared in a catalog, by one of two **source types**: a
`simple` single file addressed by **URL** (`http(s):` or `file:`), or a `maven` artifact (classifier
`cqrs`, type `tar.gz`) whose archive bundles one or more `.cqrs` models — typically a shared, centrally
published model.

### How it works

Scoping never fetches anything itself; it resolves names against the EMF `Resource`s that are already
loaded. So the feature has two halves — *get the remote model loaded*, and *make its elements visible
to scoping* — split across three classes in
[`org.fuin.dsl.cqrs/.../scoping`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/scoping):

| Class | Responsibility |
|-------|----------------|
| `RemoteScopeCatalog`        | Reads the `dependencies.json` catalog and answers *namespace → typed source* (`RemoteScopeEntry`). |
| `RemoteScopeCache`          | Materializes the remote `.cqrs` model(s) — downloading a `simple` file or resolving + unpacking a `maven` artifact — caches them under `.dependencies-cache/`, and serves them from disk on later runs. |
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
provides, a `type` discriminator and a type-specific `data` block:

```json
[
  { "type": "simple", "namespaces": ["com.acme.billing", "com.acme.catalog"],
    "data": { "url": "http://models.acme.com/billing.cqrs" } },
  { "type": "maven", "namespaces": ["com.acme.shipping"],
    "data": { "groupId": "org.fuin.dsl.cqrs.contexts",
              "artifactId": "cqrs-shipping-model", "version": "0.1.0-SNAPSHOT" } }
]
```

- `namespaces` lists the provided namespaces — the fully qualified `context.namespace` values exactly
  as they are written in an `import`. They say *where those namespaces live*, independent of who imports
  them, so one entry serves every importing model. Listing several namespaces in one entry is handy
  because a single `.cqrs` file or Maven artifact often holds more than one context and namespace. A
  trailing `.*` is ignored, so `import a.b` and `import a.b.*` match the entry that lists `a.b`.
- `type` selects the source and the shape of `data`:
  - **`simple`** — `data.url` is the URL of a single remote `.cqrs` file (`http(s):` and `file:` are
    both supported).
  - **`maven`** — `data.groupId` / `data.artifactId` / `data.version` identify a Maven artifact with
    classifier `cqrs` and type `tar.gz`. The artifact is resolved from the local repository
    (`~/.m2/repository`) first, otherwise downloaded from Maven Central (releases) or Sonatype
    Snapshots (`-SNAPSHOT` versions), and every `.cqrs` file in the archive is unpacked and made
    available for the namespace.

The catalog is re-read automatically when the file's modification time changes, so edits take effect on
the next reconcile — **no Eclipse restart needed**. (Adding a catalog where none existed before is the
one exception: a model that previously found no catalog still needs a restart.)

**2. The cache — `.dependencies-cache/`**

Created automatically next to `dependencies.json`. On first use its `index.json` (entries of
`{ namespace, source, dir }`) is read into memory for fast lookups; each resolved namespace's model(s)
are stored in a sub-directory `<namespace>-<sha1(source)>/` (a single `model.cqrs` for a `simple`
source), or `<namespace>-<version>-<sha1(source)>/` for a `maven` source — the version is part of the
name so different versions of the same artifact stay distinct — holding every `.cqrs` unpacked from the
archive. After the first fetch everything is served from disk, so editing keeps working **offline**.

```
<project root>/
├── dependencies.json
└── .dependencies-cache/
    ├── index.json
    └── com.acme.billing-3f9a1c…​/
        └── model.cqrs
```

A cached model is re-materialized automatically when it goes stale: when the catalog repoints the
namespace to a **different source**, or — for a `simple` `file:` source — when the **source file is
newer** than the cached copy. For an unchanged `http(s):` URL or a `maven` artifact (including a
re-published `-SNAPSHOT`) whose content changed, delete the entry's cache directory (or the whole
`.dependencies-cache/`) to force a refresh.

**3. Optional system property**

| Property | Default | Effect |
|----------|---------|--------|
| `cqrs.dependencies.file` | `dependencies.json` | Name of the catalog file to look for. |

### Worked example

Remote model published at `http://models.acme.com/billing.cqrs`:

```
context com.acme {
    namespace billing {
        type Money
    }
}
```

`dependencies.json` in the local project root:

```json
[ { "type": "simple", "namespaces": ["com.acme.billing"],
    "data": { "url": "http://models.acme.com/billing.cqrs" } } ]
```

(Or, to consume the same namespace from a published Maven artifact instead:)

```json
[ { "type": "maven", "namespaces": ["com.acme.billing"],
    "data": { "groupId": "org.fuin.dsl.cqrs.contexts",
              "artifactId": "cqrs-common-model", "version": "0.1.0-SNAPSHOT" } } ]
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
