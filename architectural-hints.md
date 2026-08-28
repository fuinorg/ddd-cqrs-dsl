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
an existing keyword token in another rule needs only steps 2 (the rule reference) and 5. A keyword that
starts a NEW ELEMENT of a module or an entity touches a FIFTH: `element_start` in the `.bnf`, the token
set `element_recover` is written against. Miss it and the parse fails in a way that points nowhere near
the cause - `element*` parses the previous element, then the recovery loop eats the new declaration as
garbage because `!(RBRACE | element_start)` still holds at its keyword, and the error is reported at the
top level as "context, module or } expected".**
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

## Grammar traps that cost a rebuild
Three ways an addition to `CqrsDsl.xtext` compiles, regenerates cleanly, and is still wrong. All three
were hit while adding the business-key, soft-delete and business-rule-predicate constructs.

- **Never start a keyword with `.`** — an ANTLR lexer commits to a keyword the moment its first
  character matches and cannot back out. A `'.contains('` token therefore swallows the dot of every
  qualified name whose next segment begins with a `c`, `org.fuin.dsl.cqrs` among them, and the entire
  corpus stops parsing with `mismatched character 'q' expecting 'o'`. Write the operator as separate
  tokens, `'.' 'is-empty' '(' ')'`.
- **Hyphenate the operator word.** A plain `isEmpty` keyword is reserved globally and costs every model
  built on this DSL the right to declare a method by that name — `dsl-examples/15-method.cqrs` already
  declares `method isEmpty`. A hyphen cannot appear in an ID, so `is-empty` reserves nothing. Same rule
  as `aggregate-id`, `on-collision`, `no-key`.
- **A cross-reference whose target may be two unrelated types must be `[ecore::EObject|ID]`**, narrowed
  in the scope provider. `status == IGNORED` and `status == otherStatus` are both a bare ID, so no
  lookahead separates an `EnumInstance` from an `Attribute`; ANTLR reports the second alternative as
  unreachable. Introducing a shared super-type rule (`X: A | B;`) instead is worse: `Attribute` then
  has two supertypes and EMF emits each inherited feature constant twice
  (`ATTRIBUTE__DOC ist bereits definiert`), which fails the Java compile rather than the generation.

The check that catches all of this is not the DSL's own build. Regenerate, then run the console
verifier over a real corpus — `java -jar maven/console/target/ddd-cqrs-dsl-console.jar dsl-examples`
and the same over `melkheftken/model` — and regenerate melkheftken: **an additive grammar change must
leave its generated tree byte-identical**, so `git status` there staying clean is the assertion.

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

## Scoping — a `module` is the unit of visibility
A module sees only what it declares itself; anything else needs an `import`. A `dependency` says
where models come from, an import what of them is visible, and the two share `CqrsDependencies` so
they cannot disagree about what a model depends on:
- **Local (simple names)** — `CqrsDslLocalScopeProvider` (a subclass of Xtext's
  `ImportedNamespaceAwareLocalScopeProvider`) turns the declared `import`s into `ImportNormalizer`s:
  for a `Module` its own qualified name plus its imports, for a `Context` its imports (which every
  module below therefore inherits), and **nothing** at the model root. It is installed by overriding
  **`configureIScopeProviderDelegate`** — *not* `bindIScopeProvider`, which is `CqrsDslScopeProvider`.
  That class handles the references that are **local to the element they are written in** (a row's
  `identified-by`, a business key's attributes, a rule's own attributes and the enum values scoped by
  the attribute on its left) and delegates everything else — which is every reference to a *type* — to
  the import-aware provider above. Two things about it are easy to get wrong: it extends
  **`DelegatingScopeProvider`**, so the old `scope_<Type>_<feature>(ctx, ref)` naming convention is
  never called and `getScope(EObject, EReference)` must be overridden instead; and every element with a
  `name` is indexed, so a local reference left unscoped resolves happily to a same-named element in
  another module. The test that catches that is one asserting a reference must **fail**
  (`row.identifiedBy.eIsProxy`) — an unresolved cross-reference surfaces as a validation issue, not in
  `resource.errors`.
  - A wildcard normalizer maps exactly **one** segment and does not recurse, and both a context and a
    module name may itself be dotted (`common.types`). So a wildcard import cannot be handed over as
    written: besides the literal prefix, one normalizer per **module below that prefix** is added,
    which is what makes `context.*` reach the types of a module. Those modules are read from the
    **index** (`ResourceDescriptionsProvider` + `IContainer.Manager`, the same pair
    `CqrsDslValidator.getAllExceptions` uses), so blocks declared in other files are included in the
    editor and in a headless SrcGen4J run alike.
  - Because Xtext applies the normalizers per container from the outside in, the **innermost block
    wins**: a module's own declaration shadows a same-named imported one. A name that really is
    ambiguous at one level resolves to *nothing* rather than an arbitrary pick, so it has to be
    qualified.
  - A context block is per **file**: two `context p { … }` blocks in two files are two objects, so a
    context-level import in one of them does *not* reach the modules of the other.
  - Caveat: `getImportedNamespaceResolvers` is memoized per resource, and the wildcard expansion
    depends on *other* resources. Adding a module in one file may need the referencing file to be
    re-parsed before it is seen.
- **Fully qualified names** — resolved by the **global** scope and therefore never need an import.
  Narrowing visibility is deliberately a *local*-scope concern only; `CqrsDslGlobalScopeProvider` is
  untouched by the import rules.
- **Across projects** — `CqrsDslGlobalScopeProvider` lazily loads the models of a declared
  `dependency "groupId:artifactId:version"` into the **same ResourceSet** during cross-reference
  resolution, via `CqrsModelArchives`. It makes those models *resolvable*; an `import` still decides
  what is visible. They must NOT produce generated artifacts (hence PrimaryResources).
  - **Resolution is each environment's own Maven**, behind `CqrsArtifactResolver`, picked by
    `CqrsArtifactResolvers.get()` probing implementation class names — only one is ever present in a
    given tree:

    | Front end | Implementation |
    |---|---|
    | Eclipse | `M2eArtifactResolver` — m2e's `IMaven.resolve` |
    | IntelliJ | `remote/MavenArtifactResolver` — the IDE's `MavenEmbedderWrapper` |
    | Console, SrcGen4J plugin | `MimaArtifactResolver` — MIMA `standalone-static` |

    So `settings.xml` (mirrors, servers, proxies, local repository) applies everywhere. The two IDE
    implementations are kept out of each other's tree by the exclude list of
    `mirror-eclipse-sources-to-maven.sh` — neither would compile in the other's.
  - **Nothing is unpacked.** The artifact is a plain **zip** (no classifier, `CqrsArtifactResolver.
    EXTENSION`) with the models under `model/`, read in place from the local repository: EMF gets
    `archive:file:/…/x.zip!/model/public/types.cqrs`, IntelliJ mounts it with `JarFileSystem`, which
    takes a zip like any other archive. A zip and not a jar because the models are data — nothing in
    there ever belongs on a classpath. The last segment still ends in `.cqrs`, so the resource factory
    applies as usual. Entries are taken **recursively** below `model/` and everything outside it is
    ignored, which is what lets a producer split its models into folders of its own (`model/public/…`)
    and ship a script next to them.
  - A model may name a JavaScript in its `SrcGen4J` hint, and the path is written **from the enclosing
    `model` folder**, not relative to the `.cqrs` that declares it (`CqrsScripts.anchor`). That is what
    makes one and the same string work on disk and inside the archive, whatever the depth. A model that
    lies in no `model` folder keeps the old relative behaviour.
  - **Publishing only part of a model splits a module across files**, so anything that walks the model
    has to walk the *resource set*, not the resource. `CqrsEventExtensions.getFiringEntity` did the
    latter and silently turned every event bound to its aggregate by a bare `fires` clause into a
    plain one — a change in generated Java, not just in scoping. Iterate a **copy** of
    `resourceSet.resources`: comparing the fired events resolves cross references, which may load
    further resources into the set being iterated. What the split legitimately does change is a name
    the module declares itself *and* imports — within one file the own declaration shadows the import,
    across files the two sit at the same level (`CqrsDslLocalScopeProvider` adds both as normalizers)
    and the simple name resolves to nothing, so it has to be written fully qualified.
  - A dependency model therefore has an `archive:` URI and can never be mistaken for a source model —
    the old `.dependencies-cache/` inside the source dir, and the "is it under the cache?" origin
    test, are both gone.
  - What an artifact resolved to is remembered for the **session**, success and failure alike, so a
    bad coordinate is attempted once rather than on every keystroke; the flip side is that an artifact
    appearing later needs a restart (`CqrsModelArchives.invalidate`).
  - `CqrsDependencies.declared` must iterate a **copy** of `resourceSet.resources`: resolving a
    dependency adds resources to the very set being iterated.
  - Inside a Maven build the SrcGen4J Mojo passes no session down, so the build's `-o` / `-s` are not
    inherited. Worse, `settings.xml` cannot be read there **at all**: the DSL jar sits in the plugin's
    class realm next to Maven's own, and building the settings needs `DefaultSettingsDecrypter`, whose
    `SecDispatcher` parameter type exists in *both* realms — linking it throws `LinkageError`.
    `MimaArtifactResolver.createContext` catches that and falls back to the default repositories plus
    the local repository, which covers anything already resolved or on Central. Fixing it properly
    needs the Mojo to hand its session down (MIMA's `embedded-maven` runtime), i.e. a change in
    `srcgen4j`.
- The IntelliJ plugin ports the same rules onto the PSI in `CqrsResolveUtil.resolve`: a simple name is
  matched against two tiers (the enclosing module, then what that module or its context imports) and
  only the **closest non-empty tier** is returned; a *qualified* name is matched against everything
  reachable, so it needs no import either. "Same module" is compared by the module's **qualified
  name**, not by PSI containment, because a module may be split across files.
  `referenceableDeclarations` applies the same filter, which is what makes completion offer only
  imported types.

## The two meanings of "module"
Since `namespace` was renamed to `module` there are two unrelated notions in play — keep them apart:
- the DSL `module` block an element lives in — the `${module}` variable of a SrcGen4J hint's
  `package` pattern;
- the **target Maven module** an artifact is generated into (`shared`, `command.core`, …) — the
  `"module"` key of a hint's `types`/`artifacts` entry, exposed as the `${mvnModule}` pattern
  variable. `AbstractSource.expandPackage` builds both.

## Value conversion (Xtext)
- The grammar defines its **own** terminals (no `with org.eclipse.xtext.common.Terminals`). Names use
  both `ID` (simple) and the `FQN` datatype rule; almost all cross-refs are `[X|FQN]`.
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
