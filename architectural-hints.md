# Architectural hints — ddd-cqrs-dsl (and related repos)

Brief orientation notes for working on this DSL. Verify specifics against the code before relying on them.

## The DSL has THREE independent front-ends for ONE language
- **Eclipse plugin** `eclipse/org.fuin.dsl.cqrs` — **source of truth** for the Xtext grammar
  (`CqrsDsl.xtext`) and the hand-written + generated sources.
- **Maven module** `maven/org.fuin.dsl.cqrs` — a **mirror** of the Eclipse plugin, produced by
  `./mirror-eclipse-sources-to-maven.sh` (rsync `--delete --checksum`, eclipse → maven only).
  It regenerates the ANTLR parser **headlessly** via an exec-maven-plugin mwe2 run in
  `generate-sources`, so `./mvnw -pl maven/org.fuin.dsl.cqrs verify` fully builds+tests the Xtext
  side. Mirror excludes maven-specific files: `GenerateCqrsDsl.mwe2` (mavenLayout + composedCheck)
  and `validation/AbstractCqrsDslValidator.java`. Generated files are meant to be byte-identical
  between the two, so regenerating in Maven and copying back to Eclipse is legitimate when you
  can't run the Eclipse mwe2 (then a mirror dry-run `-n` should report no diffs).
- **IntelliJ plugin** `intellij/` — **completely separate**, Gradle-based, hand-written. Uses
  **JFlex** (`src/main/grammar/CqrsDsl.flex`) + **Grammar-Kit BNF** (`CqrsDsl.bnf`) + its own PSI.
  It does NOT reuse the Xtext grammar. `cd intellij && ./gradlew test` regenerates lexer/parser.
  ⇒ Any grammar/keyword change must be made in **both** the Xtext grammar and the JFlex/BNF grammar.

## ddd-cqrs-dsl modules
- `maven/org.fuin.dsl.cqrs` — grammar, parser, EMF model, scoping/validation (the runtime jar).
- `templates` (artifact `ddd-templates`) — ~42 `ArtifactFactory` (Xtend) code generators driven by
  srcgen4j. Two kinds: per-element `ArtifactFactory<Notifier>` and ResourceSet-level
  `ArtifactFactory<ResourceSet>` (in `gen/resourceset/`, which iterate the whole ResourceSet).
  All extend `base/AbstractSource`. Java package names are built centrally in
  `AbstractSource.contextPkg/asPackage` → `joinPackage` (skips null/empty segments; unset basepkg
  ⇒ package starts at the context name). `CtxExternalTypes` is reference-only (registers imports,
  emits nothing).
- `eclipse`, `intellij`, `dsl-examples`.

## Remote model resolution
- The maven module's `CqrsDslGlobalScopeProvider` lazily loads remote dependency models into the
  **same ResourceSet** during cross-reference resolution, via `dependencies.json` →
  `MavenArtifactResolver` → materialized under `.dependencies-cache/` (which sits **inside** the
  model source dir). Useful for parsing; must NOT produce generated artifacts (hence PrimaryResources).
- Catalog entries can use a `local` directory override (remote models read from an arbitrary local
  dir, not the cache) — so "exclude `.dependencies-cache`" is NOT a reliable origin test; use the
  primary/source-dir notion instead.

## Value conversion (Xtext)
- The grammar defines its **own** terminals (no `with org.eclipse.xtext.common.Terminals`). Names use
  both `ID` (simple) and the `FQN`/`FQNWithWildcard` datatype rules; almost all cross-refs are `[X|FQN]`.
- A custom `IValueConverterService` is bound in the concrete `CqrsDslRuntimeModule` (shared by
  Eclipse + Maven; IntelliJ is separate). NOTE: `DefaultTerminalConverters` is in package
  `org.eclipse.xtext.common.services`, NOT `org.eclipse.xtext.conversion.impl`.
- Datatype rules (FQN) do NOT apply the ID converter per segment — they need their own converter.
- Keyword-as-identifier escape is `^` (e.g. `^event`): ID handled by Xtext's default IDValueConverter;
  FQN handled by the custom converter. IntelliJ has no value-converter equivalent — stripping/adding
  `^` is done in PSI (`CqrsNames` helper applied in `CqrsNamedElementImpl.getName/setName`,
  `CqrsReferenceElementImpl`, `CqrsPsiUtil`, `CqrsReference`/completion, manipulator).

## Versions & environment
- xtext **2.42.0**; srcgen4j **0.5.0-SNAPSHOT**; ddd-templates **0.2.0-SNAPSHOT**;
  org.fuin.dsl.cqrs **1.0.0-SNAPSHOT**.
- The xtend artifacts (`org.eclipse.xtend.lib/.core`, `xtend-maven-plugin`) were **relocated** to
  groupId `org.eclipse.xtext` as of 2.42.0 — the new groupId only exists at 2.42.0+.
- Maven/Gradle builds need the Bash tool's sandbox disabled (seccomp denies them otherwise).
- Test harnesses: Xtext side `maven/.../src/test/.../CqrsDslParsingTest` etc. (`ParseHelper`,
  `CqrsDslInjectorProvider`); IntelliJ side `ParsingTestCase` (parsing) + `BasePlatformTestCase`
  (resolution/completion/validation) with fixtures in `intellij/src/test/resources/examples/`.
- The IntelliJ change-notes are an inline ext.pluginChangeNotes in build.gradle;
  The Eclipse changelog lives in eclipse/README.md
