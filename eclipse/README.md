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
> (e.g. `com.google.gson`, see [below](#resolving-references-against-another-projects-models)) are
> left untouched.

---


## Name resolution

The DSL has **no `import`**. Two mechanisms make a name visible, and both are configured in
[`CqrsDslRuntimeModule`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDslRuntimeModule.xtend), which
the Eclipse editor and the headless generator share — so they behave identically.

### A module is the unit of visibility

A `module` sees only what it declares itself. A module may be split across several `.cqrs` files; all
blocks with the same name are one logical block, so "same module" does not mean "same file". A model
that publishes only part of itself is split exactly this way — the aggregates in the private half,
what they fire in the public one — so the split has to be free of side effects: an aggregate still
owns the events it `fires` from another file, and generated code does not change.

The one thing it does change is a name the module declares itself **and** imports from elsewhere.
Inside one file the module's own declaration shadows the imported one; across files the two are
equally close, so the simple name becomes ambiguous and resolves to nothing. Write it fully
qualified there — a fully qualified reference always resolves through the global scope.

Everything else needs an `import` — a sibling module of the very same context included:

```
module ordering {
    import com.acme.sales.types.*      // every type of one module
    import com.acme.sales.tax.TaxRate  // a single type
}
```

The imported name is written over the `context.module.Type` path and may end in a wildcard on any
level: `ctx.*` pulls in every module of a context, `ctx.mod.*` every type of one module, and
`ctx.mod.Type` a single type. An import declared on the `context` applies to every module below it;
note that a context block is per file, so it reaches only the modules written in that same file.

A simple name resolves against the **closest** scope that declares it:

1. the enclosing module,
2. whatever that module or its context imports.

Only the closest match is used, so reusing a name such as `TaxRate` in several modules is unambiguous
— each module sees its own. A name that is genuinely ambiguous at one level resolves to *nothing*
rather than to an arbitrary pick, and has to be qualified. A **fully qualified** name
(`com.acme.sales.journal.TaxRate`) always resolves through the global scope and needs no import.

A type that is neither declared in the module nor imported does not resolve, so the editor marks it
red and the headless build fails — the same error in both.

This is implemented by `CqrsDslLocalScopeProvider`, a subclass of Xtext's
`ImportedNamespaceAwareLocalScopeProvider` that turns the declared imports into wildcard resolvers.
It is installed by overriding `configureIScopeProviderDelegate` (the *declarative*
`CqrsDslScopeProvider` bound by `bindIScopeProvider` stays empty). Because a wildcard resolver covers
a single name segment, a wildcard import is additionally expanded into one resolver per module below
its prefix, read from the Xtext index — that is what makes `ctx.*` reach a module's types, and it
works for dotted module names too.

> Because that expansion is memoized per resource and depends on *other* resources, adding a module
> in one file may need the referencing file to be re-parsed before it is seen. A *Project ▸ Clean* is
> the manual escape hatch.

---

## Resolving references against another context's models

To reach types of a *different* context, declare the Maven artifact that provides them. The source is
an ordinary Maven artifact — a jar, no classifier — holding one or more `.cqrs` models under
`model/`, typically a shared, centrally published model — or, as an override, a `local` directory of
`.cqrs` files read directly.

```
context com.acme.sales {

    dependency "org.fuin.dsl.cqrs.contexts:cqrs-common-model:0.1.0-SNAPSHOT"

    module ordering {
        dependency "org.acme:wip-model:0.0.1-SNAPSHOT" local "../wip-model/src/main/cqrs"
        import org.fuin.dsl.cqrs.common.types.*
        ...
    }

}
```

Every module the artifact declares becomes **importable** — the dependency alone makes nothing
visible, each module still imports what it uses. A dependency declared on the `context` applies to all
of its modules; a `module` may add its own.

### How it works

Scoping never fetches anything itself; it resolves names against the EMF `Resource`s that are already
loaded. So the feature has two halves — *get the model loaded*, and *make its elements visible to
scoping* — split across these classes in
[`org.fuin.dsl.cqrs/.../scoping`](org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/scoping):

| Class | Responsibility |
|-------|----------------|
| `CqrsDependencies`          | Collects the `dependency` declarations that apply to a resource and resolves them to `.cqrs` files. Shared by the global and local scope providers, so the two can never disagree. |
| `RemoteScopeEntry`          | A parsed coordinate (`parse("groupId:artifactId:version", local)`); `null` when malformed. Plain JDK, shared verbatim with the IntelliJ plugin. |
| `CqrsModelArchives`         | Turns a dependency into model URIs: the entries below `model/` **inside** the resolved zip (`archive:file:/…/x.zip!/model/public/types.cqrs`), or the files of a `local` directory. Remembers per session what resolved and what failed. |
| `CqrsArtifactResolver` / `CqrsArtifactResolvers` | Resolves the artifact. In Eclipse that is **m2e** (`IMaven.resolve`), so the IDE's `settings.xml` — repositories, mirrors, servers, proxies, local repository — applies. `M2eArtifactResolver` lives in the **UI** plugin and is registered with `CqrsArtifactResolvers.set(...)`; outside an IDE the resolver is found on the class path. |
| `CqrsDslGlobalScopeProvider`| The `IGlobalScopeProvider`. Loads the models of every declared dependency and adds their elements to the global scope. |

Because a context may be split across files, the context-level dependencies are collected from
**every same-named context block in the resource set**, not just the one the current file contains —
the same union the generator already does for the `SrcGen4J` hint. That is what lets a single
declaration in an `aaa.cqrs` apply to modules declared in sibling files.

Elements are added *directly* — not through the workspace index, which never contains a dependency
resource — so resolution behaves identically in the **Eclipse editor** and in the **headless /
standalone generator**.

A model without dependencies adds nothing, so resolution falls back to the standard file-based
mechanism. Any failure (malformed coordinate, offline and not yet cached, parse error) is logged and
degrades to the local scope, so editing and generation never break.

> **Requires m2e**, provisioned into the headless build's target platform
> (`eclipse-build/provision.sh`). Every Eclipse Java package ships m2e; a bare SDK does not.
>
> Two things about *how* it is declared, both learned the hard way.
> `org.eclipse.m2e.maven.runtime` exports Maven's own Guice (`com.google.inject;provider=m2e`) while
> only **importing** `javax.inject`. So it must never appear in a `Require-Bundle`, which would import
> all of its exports: that Guice then shadows Xtext's, and anything building an injector fails with a
> missing `javax/inject/Provider`. The UI plugin instead uses a narrow `Import-Package` —
> `org.eclipse.m2e.core`, `org.eclipse.m2e.core.embedder`, `org.apache.maven.artifact` and
> `org.apache.maven.artifact.repository` (both with the mandatory `provider=m2e` attribute) — so its
> Guice is never wired in.
>
> And it is the **UI** plugin, not the language bundle: the language bundle is what the
> "Generate CqrsDsl (cqrs) Language Infrastructure" MWE2 launch runs against, and it is the bundle
> mirrored into the plain Maven jar, where m2e cannot exist at all. `CqrsDslUiModule` therefore
> registers `M2eArtifactResolver` with `CqrsArtifactResolvers.set(...)`.

### Nothing is unpacked

The models are read **in place, out of the zip in the local Maven repository**:

```
archive:file:/home/me/.m2/repository/org/fuin/…/cqrs-common-model-0.1.0-SNAPSHOT.zip!/model/public/types.cqrs
```

EMF resolves an `archive:` URI out of the box and the last segment still ends in `.cqrs`, so Xtext's
resource factory applies exactly as for a workspace file — `F3` navigates into the entry. Only files
below `model/` count, taken recursively; anything else in the jar is ignored. There is no
`.dependencies-cache/` any more, and therefore nothing inside the model source directory that the
generator or the console verifier has to skip: a dependency model has an `archive:` URI and can never
be mistaken for a source model.

The local repository is the only cache there is. What a coordinate resolved to — and why it failed —
is remembered for the session, so a bad coordinate is attempted once rather than on every keystroke;
an artifact that appears later is picked up after a restart.

### Validation

| Rule | Severity |
|------|----------|
| Coordinate is not `groupId:artifactId:version` | error |
| Same coordinate declared twice in one block | error |
| A module repeats a dependency its context already declares | warning |
| An import matches no context, module or type | error |
| The same import twice in one block | error |
| The artifact cannot be resolved (no such artifact, `local` directory missing, nothing provided) | error |
| A module repeats an import its context already declares | warning |
| An import nothing in the block refers to | warning |

An artifact that cannot be resolved **is** an error, on the coordinate itself. Otherwise the only
symptom is every type it provides failing to resolve, which points at the models instead of at the
declaration that is actually wrong.

Resolution stays out of the editor's way: an artifact is resolved once and both the result and the
failure are remembered for the session, so a coordinate that cannot be downloaded is not retried on
every keystroke. The flip side is that an artifact which becomes available later is only picked up
after a restart.

### Worked example

Model published as the Maven artifact
`org.fuin.dsl.cqrs.contexts:cqrs-common-model:0.1.0-SNAPSHOT` (a plain zip with the models under `model/`),
bundling a `billing.cqrs`:

```
context common {
    module com.acme.billing {
        type Money
    }
}
```

The consuming model declares the artifact and imports the module that provides `Money`:

```
context sales {

    dependency "org.fuin.dsl.cqrs.contexts:cqrs-common-model:0.1.0-SNAPSHOT"

    module com.acme.sales {
        import common.com.acme.billing.*

        value-object Price {
            Money amount
        }
    }

}
```

`Money`'s fully qualified name is `common.com.acme.billing.Money` (context `common` + module
`com.acme.billing` + `Money`). The dependency makes the artifact's models resolvable and the import
makes that module's types visible, so the simple name resolves; writing the fully qualified name
instead would work without the import. The cross-reference then behaves exactly as if `Money` were a
local type — `F3` navigates into the cached copy, and content assist proposes it.

While developing that model locally, point at its source folder instead — its `.cqrs` files are then
read directly, no build or publish needed:

```
dependency "org.fuin.dsl.cqrs.contexts:cqrs-common-model:0.1.0-SNAPSHOT" local "../cqrs-common-model/src/main/cqrs"
```

The path is relative to the model that declares the dependency.
