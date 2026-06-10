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
> (e.g. `com.google.gson`, see [below](#resolving-references-against-remote-http-only-models)) are
> left untouched.

---

## Resolving references against remote (HTTP-only) models

By default Xtext resolves cross-references (`[Type|FQN]`, `[Exception|FQN]`, …) only against models
that live in the workspace / on the local file system. `CqrsDslGlobalScopeProvider` extends that so a
model can reference types defined in another `.cqrs` model that is **only available over HTTP** — for
example a shared, centrally published model that is never checked out locally.

### How it works

Scoping never fetches anything itself; it resolves names against the EMF `Resource`s that are already
loaded. So the feature has two halves — *get the remote model loaded*, and *make its elements visible
to scoping* — split across three classes in
[`org.fuin.dsl.cqrs/.../scoping`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/scoping):

| Class | Responsibility |
|-------|----------------|
| `RemoteScopeCatalog`        | Reads the `.remote-scope.json` catalog and answers *(context, namespace) → URL*. |
| `RemoteScopeCache`          | Downloads the remote `.cqrs`, caches it under `.remote-scope-cache/`, and serves it from disk on later runs. |
| `CqrsDslGlobalScopeProvider`| The `IGlobalScopeProvider`. For every `import` it consults the catalog/cache and adds the remote model's elements to the global scope. |

For each `import` in a model, the provider forms the key **(enclosing `context` name, imported
namespace)**, looks it up in the catalog, loads the matching remote model (from the local cache, or
downloading it once on a miss), and exposes its objects **by fully qualified name** on top of the
normal local scope. Because the elements are added *directly* — not through the workspace index, which
never contains an HTTP resource — resolution behaves identically in the **Eclipse editor** and in the
**headless / standalone generator**. The provider is bound once in
[`CqrsDslRuntimeModule`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDslRuntimeModule.xtend), which both
contexts share.

A coordinate that is **not** in the catalog adds nothing, so resolution falls back to the standard
file-based mechanism. Any failure (missing catalog, offline and not yet cached, parse error) is logged
and degrades to the local scope, so editing and generation never break.

> Requires the `com.google.gson` bundle on the classpath (declared via `Import-Package` in
> `org.fuin.dsl.cqrs/META-INF/MANIFEST.MF`; it ships with the Xtext SDK / Orbit target platform).

### Configuration

**1. The catalog — `.remote-scope.json`**

Place it in your project root (it is discovered by walking up the directory tree from the model being
edited). It is a nested object *context → namespace → url*:

```json
{
  "com.acme.sales": {
    "com.acme.billing":  "http://models.acme.com/billing.cqrs",
    "com.acme.shipping": "http://models.acme.com/shipping.cqrs"
  },
  "com.acme.support": {
    "com.acme.billing":  "http://models.acme.com/billing.cqrs"
  }
}
```

- The **context** key is the bounded context that contains the `import` (the enclosing `context …`).
- The **namespace** key is the imported namespace. A trailing `.*` is ignored, so `import a.b` and
  `import a.b.*` map to the same entry.
- The **value** is the URL of the remote `.cqrs` file (must end in `.cqrs`).

**2. The cache — `.remote-scope-cache/`**

Created automatically next to `.remote-scope.json`. On first use its `index.json` is read into memory
for fast key lookups; downloaded models are stored as `<namespace>-<sha1(url)>.cqrs`. After the first
fetch everything is served from disk, so editing keeps working **offline**. To force a refresh, delete
the cached file (or the whole directory).

```
<project root>/
├── .remote-scope.json
└── .remote-scope-cache/
    ├── index.json
    └── com.acme.billing-3f9a1c…​.cqrs
```

**3. Optional system property**

| Property | Default | Effect |
|----------|---------|--------|
| `cqrs.remote.scope.file` | `.remote-scope.json` | Name of the catalog file to look for. |

### Worked example

Remote model published at `http://models.acme.com/billing.cqrs`:

```
context com.acme {
    namespace billing {
        type Money
    }
}
```

`.remote-scope.json` in the local project root:

```json
{ "com.acme.sales": { "com.acme.billing": "http://models.acme.com/billing.cqrs" } }
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
the catalog entry for *(context `com.acme.sales`, namespace `com.acme.billing`)* tells the provider
where to download the model defining it. The cross-reference resolves exactly as if `Money` were a
local type — `F3` navigates into the cached copy, and content assist proposes it.

> The import path must match the remote type's FQN prefix, just as it would for a local model. The
> provider only handles *loading*; name resolution stays standard Xtext.
