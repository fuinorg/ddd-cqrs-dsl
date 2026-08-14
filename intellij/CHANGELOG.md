# Changelog

Generated from `ext.pluginChangeNotes` in [build.gradle](build.gradle) - do not edit.

## 1.27.0
- New action **Tools | Refresh CQRS Model Dependencies**. What a `dependency` resolved to was cached until the IDE was restarted, so an artifact rebuilt or re-resolved outside it kept answering with what was true at startup - and a stale archive reads as a broken `import`, because only the modules added since go red. The action drops those caches and re-highlights the open files.

## 1.26.0
- A `business-rule` may now be declared at module level, not only inside an entity or an aggregate. A rule that means the same thing everywhere - "the entity must not be deleted" - is stated once and imported like any other type, so its name, its exception and its consistency classification cannot drift apart across contexts. An aggregate may still declare its own rules, and one it declares itself still wins over an imported rule of the same name; a rule written inside an aggregate is no longer flagged as an illegal nested element.

## 1.25.0
- A model opened out of a dependency's archive resolves again. Its names were looked up in the project's index only, which such a file is not part of - so every `import` and every type in it was flagged, including the ones it declares itself. A model that is read rather than authored - an entry of an archive, or a file of a `local` directory outside the project - now resolves against the neighbours it was read beside, and reports nothing: the reader cannot act on the message, and a model publishing only part of itself legitimately names types it kept to itself.

## 1.24.0
- **Breaking:** the `SrcGen4J` hint no longer describes where generated code goes with a `package` pattern and a `types` table. Two JavaScript functions decide instead, named by the hint and written next to the model: `model2JavaPackage(element, typeKey)` returns the Java package of a generated type, `artifact2Target(element, typeKey, artifactFactory)` the Maven module and folder its file is written to. A model that declares neither uses the mapping shipped with the templates, so most models need no hint at all. The two old keys are now rejected by the hint's JSON schema, which is what this release changes for the editor - an unmigrated model is reported here rather than generating into the wrong packages later.<br> **Breaking:** the artifact a `dependency` names is now resolved as a plain `zip` instead of a jar (still no classifier, still holding the `.cqrs` files below `model/`): the models are data and nothing in there belongs on a classpath. Re-publish a model artifact as a zip, otherwise its coordinate is reported as unresolvable. An artifact that resolves but cannot be read - not an archive as far as the IDE is concerned, or holding no models below `model/` - now says so on the coordinate instead of leaving every type it provides unresolved.

## 1.23.1
- A `dependency` declared on a `context` now applies to every file of that context, as it already did in the SrcGen4J build. Until now the editor only saw a `dependency` written in the same file, so a context split across files - the dependency in one, the modules importing its types in the others - had every one of those imports reported as `does not match any context, module or type` while code generation resolved them fine. A `local` directory is resolved relative to the file that declares the dependency.<br> Code completion after `import` now offers the contexts, modules and types a dependency provides. Completion runs on a non-physical copy of the edited file, and that copy has no location on disk, so the dependencies were dropped and only what the project itself declares was proposed.

## 1.23.0
- **Breaking:** the model has two levels instead of three. `project` is gone and the inner block is a `module`: `project P { context C { namespace N { X } } }` becomes `context P { module C.N { X } }`. Module names are qualified names, every element must live in a module, and every element keeps its fully qualified name.<br> **Breaking:** a module is the unit of visibility. Only what a module declares itself resolves by a simple name, so reaching any other module - a sibling of the same context included - needs an `import` over the `context.module.Type` path, optionally ending in a wildcard (`ctx.*`, `ctx.mod.*`, `ctx.mod.Type`). A fully qualified reference needs none.<br> **Breaking:** where models come from is declared in the model itself with `dependency "groupId:artifactId:version"` (optionally `local "../wip/src/main/cqrs"`), replacing the external `dependencies.json` catalog. A dependency makes models resolvable, an import decides what of them is visible.<br> **Breaking:** artifacts are resolved through **the IDE's own Maven** (the bundled Maven plugin, now required), so your `settings.xml` - local repository, remote repositories, mirrors, servers and proxies - applies as for any Maven project. A model artifact is a plain jar holding the `.cqrs` files under `model/`, mounted with `JarFileSystem` and read in place, so go-to-definition lands inside the jar and there is no `.dependencies-cache` directory. The `cqrs.*` system properties are gone.<br> New checks: an unresolvable or duplicate import is an error, an unused one a warning; a malformed, duplicated or unresolvable `dependency` is reported on its coordinate. Code completion offers only what the surrounding module reaches, and the importable paths after `import`. A quote is closed as it is typed and nothing is proposed inside a string, so writing a coordinate no longer pops up the keyword list at every dot.

## 1.22.0
- The service an operation uses is now introduced by the `operation-context` keyword, instead of being written as a bare type name after the parameters where it looked like a parameter that had lost its name: `method rename { CategoryName newName operation-context RenameService }`. The keyword is syntax-highlighted and offered in completion inside a constructor or method body, together with `service`, which was missing there. **Breaking:** every existing reference needs the keyword; the inline `service` declarations themselves are unchanged.<br> A typed cross-reference now also offers - and resolves to - only the kind of declaration the grammar allows there: an `operation-context` names a service, a view's `uses` a projection, `fires` an event, `identifier` an id, `invariants` a constraint, `target` an operation, and so on for every typed reference. Completion used to list every visible type at those positions, and a name of the wrong kind resolved silently while the build rejected it; it is now reported as unresolved, like any unknown name. A position the grammar declares as `[Type]` - `returns`, `instance-key`, an attribute or parameter type, a generic argument, a constraint's `input` - still takes any type.

## 1.21.0
- Code completion inside a `business-rule` now offers the `consistency` clause and its values: `weak`/`strong`, the time units after `acceptable`, and the `detection`/ `resolution` value sets. Previously it offered every visible type declaration there instead, and never the keywords. A half-typed `weak` details block also keeps its tokens, so completion still works while it is being written - a block missing `acceptable`, `detection` or `resolution` is now reported as a semantic error rather than a parse error. Separately, `null`/`true`/ `false` are offered after `examples` and in an instance argument list, and `element` after `type`.

## 1.20.0
- A view `method` may now declare an optional `rest-path` setting the sub path of its REST operation; without it the method name is used. **Breaking:** `rest-path` moved from the body into the header of `view` and `method`: `view X uses Y rest-path "/x" { ... }`. The keyword is syntax-highlighted and offered in completion at those header positions.

## 1.19.0
- A `context` body may now mix `namespace` blocks and type/elements as siblings; previously it was an either/or (all namespaces, or imports and elements directly). Existing models are unaffected.

## 1.18.0
- The process-manager `correlation-id` element was renamed to `instance-key` (it clashed with the unrelated event/command correlation id). `instance-key` names the value that identifies which running process instance an incoming event belongs to; it references any type, exactly as before. The keyword is syntax-highlighted and offered in context-aware completion inside a process-manager body.

## 1.17.0
- A `JpaHint` table may now declare JPA associations to the other tables of the same hint: `@ManyToOne` (with a `@JoinColumn`, and `"foreignKey": "NO_CONSTRAINT"` to skip the database constraint) on the owning side, and `@OneToMany` (with `mappedBy`, `fetch`, `orphanRemoval` and `cascade`) on the inverse side. The new `manyToOnes` and `oneToManys` keys are validated live against the JSON schema.

## 1.16.0
- A `view` body may now declare generator `hint`s (JSON-structured, like a `project`), placed right after the opening `{`. The `hint` keyword is syntax-highlighted and offered in context-aware completion inside a view body. A hint's JSON is now validated live against a JSON schema (`JpaHint` and `SrcGen4J`), and a `JpaHint` declared outside a `view` is flagged with a warning.

## 1.15.0
- A `view` body may now declare an optional `rest-path` and `cron-schedule`: `rest-path` sets the base path of the generated REST controller (otherwise derived from the aggregate name) and `cron-schedule` sets the projection schedule (otherwise the default `* * * * * *`). Both keywords are syntax-highlighted and offered in context-aware completion inside a `view` body, and `cron-schedule` is validated as a Spring Boot cron expression (on a `view` and a `process-manager`). Grammar-only for now; code generation follows.

## 1.14.0
- New `process-manager` element for modelling orchestration (sagas): it reacts to domain events and issues commands through a small, documented state machine - `process-states` plus guarded `reacts-to <Event> in-state <State>` reactions that `correlate-by` an event attribute, `issues-commands`, `transition-to` another state, and `arm-timeout`/`cancel-timeout`. Every state and reaction may carry its own doc comment. Grammar-only for now (like `command-handler`/`view`); code generation follows. The new keywords are syntax-highlighted and offered in context-aware completion (the manager clauses inside the block, the reaction clauses inside a `reacts-to`, and the time units after `arm-timeout`). Separately, `view` bodies now offer their `business-rule` and `method` keywords in completion.

## 1.13.0
- A method's `returns` may now be declared `optional` (`returns optional String`), the same way an attribute or a parameter can, to express that the result may be absent. The code generation maps it to a `java.util.Optional` of the declared type.

## 1.12.0
- A `view` may now declare `business-rule`s and `method`s in a body, the same way a `service` does, so the operations that query a read model can be modelled where the view is defined. The body is mandatory (it may be empty), so an existing `view X uses Y` becomes `view X uses Y { }`. A `projection` may be declared without any `input` events.

## 1.11.0
- Two errors that the Eclipse plugin reports are no longer missed. A type that exists somewhere in the project but is **not imported** now shows as unresolved instead of silently resolving, and a command's `target` only accepts the method or constructor it triggers, so pointing it at an attribute is reported. Both cases used to look valid in the editor and then fail the build.

## 1.10.0
- A value object with a `base` is no longer required to have exactly one attribute of the base type. Only "`base String` plus exactly one attribute" is generated as a complete class; every other shape becomes an abstract base class plus a hand-written final class supplying `asBaseType()`. Value objects such as `PhoneNumber`, which pack several attributes into one base representation, are legal and no longer flagged.

## 1.9.0
- A method's `returns` type may now carry generic type arguments, written the same way as on attributes and parameters (e.g. `returns List<Customer>`). The referenced argument types are resolved and offered by code completion.

## 1.7.0
- The `namespace` is now optional: a `context` may hold its imports and elements directly, without an enclosing `namespace`. Parsing, reference resolution and code completion all handle namespace-less contexts (an element declared directly in a context resolves under the `project.context` path).

## 1.6.1
- Go-to-definition / find-usages no longer shows the same type several times ("Multiple Implementations"): declarations reachable through more than one scope path (a Maven artifact providing several imported namespaces, or a cached model also indexed as a project file) are now de-duplicated by source location.

## 1.6.0
- Added code folding: every multi-line `{ }` block collapses individually (including an `enum`'s `instances` list and JSON `hint` bodies), as do multi-line block and doc comments. Nothing is collapsed when a file is opened.

## 1.5.1
- Unresolvable cross-references are now shown as error.

## 1.5.0
- Added project structure above context.

## 1.4.1
- Code completion inside a `data-protection` block now offers the clause values: protection levels after `protection`, the lawful bases after `lawful-basis`, special categories after `category`, time units after a `retention` amount and erasure strategies after `then`. Parser error recovery for partially typed clauses was improved as well.

## 1.4.0
- Added a data-protection (GDPR) concept: reusable `data-protection` policies (protection level, special category, data subject, purpose, lawful basis and a retention period with an erasure strategy) applied to types and attributes via `protected-by`. The attribute/parameter keyword `nullable` was renamed to `optional`, and the time units `weeks`, `months` and `years` were added.

## 1.3.0
- A keyword can now be used as an identifier by prefixing it with a caret (e.g. `^event`). The caret marks the following word as a plain identifier and is not part of the name.

## 1.2.0
- Dependency catalog is now Maven-only (the `simple` URL source was removed). Artifacts are cached once per Maven coordinate (GAV) instead of per namespace, so an artifact providing several namespaces is unpacked only once. A new `local` directory field reads `.cqrs` models straight from a folder without downloading.

## 1.1.0
- Remote scope catalog now supports typed entries: a `simple` single-file source and a `maven` artifact (classifier `cqrs`, `tar.gz`) whose models are resolved and unpacked.

## 1.0.2
- Code completion now offers available types when starting an attribute in a `value-object` or `constraint` body.

## 1.0.1
- Added semantic validation (ported from the Eclipse validator), including the value-object `base` rules.

## 1.0.0
- Initial release.
