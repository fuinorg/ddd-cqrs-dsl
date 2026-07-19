# Changelog

Generated from `ext.pluginChangeNotes` in [build.gradle](build.gradle) - do not edit.

## 1.16.0
- A `view` body may now declare generator `hint`s (JSON-structured, like a `project`), placed right after the opening `{`. The `hint` keyword is syntax-highlighted and offered in context-aware completion inside a view body.

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
