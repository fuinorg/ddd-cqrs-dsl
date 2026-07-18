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

## Propagating a grammar change — full checklist
Worked example: adding optional `rest-path`/`cron-schedule` clauses to the `view` rule. Note the two
kinds of keyword: `rest-path` is a **brand-new** keyword (touches all four IntelliJ files below);
`cron-schedule` **reuses the existing** process-manager token (`KW_CRON_SCHEDULE` / the `.flex` entry /
the highlighting set already exist), so it only needs the rule reference + the completion string.

**Xtext side (eclipse = source of truth, maven = mirror):**
1. Edit the rule in **both** `eclipse/org.fuin.dsl.cqrs/.../CqrsDsl.xtext` and the maven mirror
   (identical text). Then **regenerate the committed generated code** — neither the Eclipse PDE
   build nor `mvnw` runs MWE2 as part of a normal build, so stale `src-gen`/`xtext-gen` would
   silently ship. Recipe (offline, JDK 21 at `~/.sdkman/.../21.0.11-zulu`):
   - `./mvnw -o -pl maven/org.fuin.dsl.cqrs generate-sources -s settings.xml` (maven core: EMF model
     + xtext-gen; regenerates e.g. `ViewImpl.getRestPath()/getCron()`).
   - `./mvnw -o -pl maven/org.fuin.dsl.cqrs dependency:build-classpath -Dmdep.outputFile=$PWD/cp.txt`
     then `java -cp "$(cat cp.txt):eclipse/org.fuin.dsl.cqrs/src`
     `" org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher org.fuin.dsl.cqrs.GenerateCqrsDsl`
     `-p rootPath=<abs>/eclipse` (eclipse core + `.ide` content-assist ANTLR + `.ui`).
   - `./mvnw -o -pl maven/org.fuin.dsl.cqrs install -DskipTests` (compiles xtend + generated Java).
   - Verify with `./mirror-eclipse-sources-to-maven.sh -n` → prints no diffs.
   - A harmless `warning(200): … "'}'" using multiple alternatives` appears because the `view` body
     ends in two optional trailing lists (`businessRule* method*`); pre-existing, not an error.

**IntelliJ side (separate hand-written plugin) — a NEW keyword touches FOUR files, not one; reusing
an existing keyword token in another rule needs only steps 2 (the rule reference) and 5:**
2. `intellij/src/main/grammar/CqrsDsl.bnf` — for a new keyword, declare the token in the `tokens { }`
   block (e.g. `KW_REST_PATH='rest-path'`); for any keyword, reference the token in the rule
   (`view_def`). Reusing an existing token (`KW_CRON_SCHEDULE`) needs only the reference — no new decl.
3. `intellij/src/main/grammar/CqrsDsl.flex` — map the literal to the token (new keywords only).
   **Hyphenated** keywords go in the "Hyphenated keywords (must precede the ID rule)" block;
   **plain-word** keywords in the "Structural keywords" block. JFlex longest-match keeps a shorter
   literal from shadowing a longer one that shares its prefix.
4. `intellij/src/main/java/.../CqrsTokenSets.java` — **highlighting is a hand-maintained `TokenSet`**
   (`KEYWORDS`), NOT auto-derived from the generated `CqrsTypes`. A new keyword omitted here parses
   fine but renders uncolored. Add its `CqrsTypes.KW_*` (reused tokens are already in the set).
5. `intellij/src/main/java/.../completion/CqrsCompletionContributor.java` — **completion is a
   hand-written context-aware contributor**, not free from Grammar-Kit. Add the keyword strings in
   the matching context branch (e.g. the `enclosingView(...)` branch) or it will never be offered.
   Regenerate + verify: `./gradlew --offline generateCqrsParser generateCqrsLexer compileJava`
   (JDK 25 for the Gradle JVM, Java-21 toolchain auto-used for codegen). Bare `STRING` clauses get
   no generated PSI accessor — expected, and irrelevant to parse/highlight/complete.
   Tests: fixtures in `intellij/src/test/resources/examples/*.cqrs` drive `CqrsParsingTest`;
   `CqrsCompletionContributorTest` asserts the offered keyword set (uses `containsAll`).

**Versions + changelogs — three independent lines, bump each where the change lives:**
6. IntelliJ: `intellij/gradle.properties` `pluginVersion` + prepend a `<li>` in
   `intellij/build.gradle` `ext.pluginChangeNotes`. **Never hand-edit `intellij/CHANGELOG.md`** —
   it is generated from those change-notes by the `generateChangeLog` task.
7. Eclipse: bump all **five** `Bundle-Version` lines (`org.fuin.dsl.cqrs`, `.ui`, `.ide`, `.tests`,
   `.ui.tests`) **and** `org.fuin.dsl.cqrs.feature/feature.xml` together (OSGi `x.y.z.qualifier`);
   hand-edit `eclipse/CHANGELOG.md`.
8. Maven submodules stay `*-SNAPSHOT`; log DSL/grammar changes as bullets under the single
   `## 1.0.0-SNAPSHOT` heading in the root `CHANGELOG.md`.

## Semantic validation lives in TWO hand-synced validators
- **Xtext:** `.../validation/CqrsDslValidator.xtend` — `@Check def` methods auto-invoked (no manual
  registration); report via `error(msg, obj, CqrsDslPackage.Literals::&lt;FEATURE&gt;, CODE)`. Edit it in
  **both** eclipse + maven; its generated `.java` is produced by the maven xtend build and copied to
  eclipse (byte-identical), like the other `xtend-gen`.
- **IntelliJ:** `.../intellij/CqrsValidationAnnotator.java` — an `Annotator` (registered in
  `plugin.xml`) that ports the same rules onto the PSI; report via
  `holder.newAnnotation(HighlightSeverity.ERROR, msg).range(psi).create()`. Add a branch to its
  `instanceof` dispatch. Reaching a bare literal with no Grammar-Kit accessor (e.g. a `view`'s
  `cron-schedule` STRING, since the rule has two STRINGs) uses the keyword-scan helper
  `CqrsValidationUtil.firstTokenAfter(parent, keyword, token)`; a rule with a single STRING (e.g.
  `process-manager`) has a generated `getString()`.
- **Shared pure logic must be duplicated**, not shared — the two plugins have no common jar. Example:
  `SpringCronExpression.isValid(...)` (validates a `cron-schedule` as a Spring 6-field cron / macro)
  exists as an identical copy in `org.fuin.dsl.cqrs.validation` (Xtext, mirrored eclipse→maven) and
  `org.fuin.dsl.cqrs.intellij` (IntelliJ). Keep the copies in sync. IntelliJ validation tests use
  `BasePlatformTestCase.checkHighlighting` with inline `&lt;error&gt;…&lt;/error&gt;` markup (the tags are
  stripped before parsing and assert a semantic error over that exact range — not DSL syntax).

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
- Three changelogs (see the propagation checklist above): root `CHANGELOG.md` (maven submodules),
  `eclipse/CHANGELOG.md` (hand-written), and `intellij/CHANGELOG.md` (generated from
  `ext.pluginChangeNotes` in `intellij/build.gradle` — do not hand-edit).
