# CQRS DSL — Examples

Small, self-contained `*.cqrs` files, each demonstrating one feature of the DSL with the
smallest possible example. Every file is a complete, valid model and starts with a short
comment explaining what it shows. They follow the grammar in
[`eclipse/org.fuin.dsl.cqrs/.../CqrsDsl.xtext`](../eclipse/org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDsl.xtext).

| File | Shows |
|------|-------|
| [01-context-modules](01-context-modules.cqrs) | The two levels `context` / `module` (dotted module names), and the module as the unit of visibility |
| [02-external-types](02-external-types.cqrs) | External `type`s: plain, `element`, and `generics <n>` |
| [03-value-object](03-value-object.cqrs) | `value-object` — wrapping a `base` type vs. grouping attributes |
| [04-type-metainfo](04-type-metainfo.cqrs) | UI metadata (`slabel` / `label` / `tooltip` / `prompt` / `examples`) and per-attribute overrides |
| [05-attributes-and-generics](05-attributes-and-generics.cqrs) | Attributes: `optional` and generic type arguments `<...>` |
| [06-enum](06-enum.cqrs) | `enum` instances, instance parameters, `deprecated`, `base` |
| [07-exception](07-exception.cqrs) | `exception` with `cid`, attributes and a `message` |
| [08-constraint](08-constraint.cqrs) | `constraint` with `input` (incl. multiple via `|`), `exception` and parameters |
| [09-annotation](09-annotation.cqrs) | `annotation` declaration and `@Name` / `@Name(args)` instances on a value-object |
| [10-invariants](10-invariants.cqrs) | `invariants` on a type and on an attribute (with parameters) |
| [11-aggregate](11-aggregate.cqrs) | `aggregate` + `aggregate-id` (`identifies` / `identifier`) and a `constructor` that `fires` an event |
| [12-entity](12-entity.cqrs) | `entity` + `entity-id` with `root` pointing at the owning aggregate |
| [13-business-rule-consistency](13-business-rule-consistency.cqrs) | `business-rule` in an aggregate and at module level (shared, imported), `strong` vs. `weak` `consistency` (`acceptable` / `detection` / `resolution`), and `business-rules` usage |
| [14-constructor](14-constructor.cqrs) | `constructor` parameters, `fires`, and an inline `event` |
| [15-method](15-method.cqrs) | `method` with `returns`, `fires`, parameter `preconditions`, and `ref` |
| [16-service](16-service.cqrs) | `service` grouping methods and a business rule |
| [17-event](17-event.cqrs) | `event` attributes, `message`, and `copies-attributes-of` |
| [18-command-and-handler](18-command-and-handler.cqrs) | `command` with `target` and `sla`, and a `command-handler` (`handles` / `uses`) |
| [19-projection-and-view](19-projection-and-view.cqrs) | `projection` consuming events and a `view` using it |
| [20-literals-and-comments](20-literals-and-comments.cqrs) | Comment styles (`//`, `/* */`, `/** */`) and literal values (string, number, boolean, `null`) |
| [21-data-protection](21-data-protection.cqrs) | GDPR `data-protection` policies (`protection` level, `category`, `subject`, `purpose`, `lawful-basis`, `retention ... then ...`) applied via `protected-by` |
| [22-hint](22-hint.cqrs) | `hint` — a named, JSON-structured generator hint. The `SrcGen4J` config on a `context`: the `model2JavaPackage` and `artifact2Target` scripts deciding where generated code goes (in [22-hint-scripts](22-hint-scripts)), and `constraintMappings` mapping a DSL constraint onto a Java validation annotation |
| [23-message-el-expressions](23-message-el-expressions.cqrs) | `message` with simple `${var}` placeholders and Jakarta EL expressions (method calls, arithmetic) |
| [24-process-manager](24-process-manager.cqrs) | `process-manager` (saga): `process-states` and `reacts-to <Event> in-state <State>` reactions with `correlate-by`, `issues-commands`, `transition-to` and `arm-timeout`/`cancel-timeout` |
| [25-jpa-hint](25-jpa-hint.cqrs) | `JpaHint` inside a `view` describing JPA tables (`@Table`/`@Column` plus `@Digits`/`@DecimalMin`), generated into the view's package |
| [26-dependency](26-dependency.cqrs) | `dependency "groupId:artifactId:version"` on a `context` and on a `module`, with the optional `local` directory override. Both point at [26-dependency-models](26-dependency-models) and [26-dependency-wip](26-dependency-wip), which stand in for a published artifact and an unpublished one so the example resolves without a network |
| [27-import](27-import.cqrs) | `import` on a `context` and on a `module`: `ctx.*`, `ctx.mod.*` and `ctx.mod.Type`, plus the fully qualified reference that needs none |

Open them with the Eclipse plugin or the
[IntelliJ IDEA plugin](../intellij/README.md) for syntax highlighting and code completion.
