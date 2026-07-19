# Changelog

Reflects only changes made in the Eclipse plugin.

## 1.16.0
- The `process-manager` `correlation-id` element was renamed to `instance-key` (it clashed with the unrelated event/command correlation id). `instance-key` names the value that identifies which running process instance an incoming event belongs to; it references any type, exactly as before, and `correlate-by` is unchanged.

## 1.15.0
- A `JpaHint` table may now declare JPA associations to the other tables of the same hint: a `manyToOnes` entry generates the owning side (`@ManyToOne` + `@JoinColumn`, with `"foreignKey": "NO_CONSTRAINT"` to skip the database constraint) and a `oneToManys` entry generates the inverse collection (`@OneToMany` with `mappedBy`, `fetch`, `orphanRemoval` and `cascade`). The new `manyToOnes`/`oneToManys` keys are validated live against the JSON schema.

## 1.14.0
- A `view` body may now carry generator `hint`s, the same JSON-structured hints a `project` declares, placed right after the opening `{`. Grammar-only for now, like the rest of the `view` element.
- A `JpaHint` declared inside a `view` now generates JPA `@Entity` classes into the view's package (all `@Table`/`@Column` attributes plus `@Digits`/`@DecimalMin`). A hint's JSON is validated live against a JSON schema (`JpaHint` and `SrcGen4J`), and a `JpaHint` outside a view is flagged as a warning.

## 1.13.0
- A `view` may now declare an optional `rest-path` and `cron-schedule` in its body. `rest-path` sets the base path of the generated REST controller (otherwise derived from the aggregate name); `cron-schedule` sets the projection schedule (otherwise the default `* * * * * *`) and is validated as a Spring Boot cron expression (the same check now also applies to a `process-manager`'s `cron-schedule`). Both are optional. Like the rest of the `view` element it is grammar-only for now; code generation follows.

## 1.12.0
- New `process-manager` element for modelling orchestration (sagas). It reacts to domain events and issues commands through a small, documented state machine: an optional `cron-schedule` and `correlation-id`, a `process-states { … }` list, and one or more `reacts-to <Event> in-state <State> { … }` reactions that may `correlate-by` an event attribute, `issues-commands`, `transition-to` another state, and `arm-timeout`/`cancel-timeout`. Every state and reaction may carry its own doc comment. Two validations are added: duplicate state names within a process manager, and a `correlate-by` key that is not an attribute of the reacted event. Like `command-handler`/`view` it is grammar-only for now; code generation follows.

## 1.11.0
- A method's `returns` may now be declared `optional` (`returns optional String`), the same way an attribute or a parameter can, to express that the result may be absent. The code generation maps it to a `java.util.Optional` of the declared type.

## 1.10.0
- A `view` may now declare `business-rule`s and `method`s in a body, the same way a `service` does, so the operations that query a read model can be modelled where the view is defined. The body is mandatory (it may be empty), so an existing `view X uses Y` becomes `view X uses Y { }`.
- Documented that a `projection` may be declared without any `input` events; this already worked but was never spelled out.

## 1.9.0
- Removed the validation rule that required a value object with a `base` to have exactly one attribute of the base type. The rule contradicted the code generation: only "`base String` plus exactly one attribute" is generated as a complete class, while every other shape is generated as an abstract base class plus a hand-written final class that supplies `asBaseType()`. Value objects such as `PhoneNumber` (a `base String` packing a `PhoneType` and the number into one representation) are legal and no longer flagged.

## 1.8.0
- A method's `returns` type may now declare optional generic type arguments, using the same `<...>` syntax already available for attributes and parameters (e.g. `returns List<Customer>`). The generated method signature renders the generic return type and imports the argument types.

## 1.7.0
- Generated event/command/exception messages now use `KeyValueEL` (Jakarta Expression Language), so a `${...}` placeholder may contain a full EL expression (e.g. `${name.toUpperCase()}`, `${quantity * price}`).
- Message-variable validation now only checks simple `${name}` placeholders against the declared attributes; complex EL expressions are no longer flagged as unknown variables.
- Replaced the `${#entityIdPath}` special syntax with the ordinary implicit variable `${entityIdPath}` (the `#` prefix is gone).

## 1.6.0
- Made the `namespace` optional: a `context` may now hold its imports and elements directly, without an enclosing `namespace`.
- Elements declared directly in a context are generated into the `project.context` package; the SrcGen4J `package` pattern's namespace segment is now optional, written as `${project}.${module}.${group}.${context}[.${namespace}]`.

## 1.5.0
- Added project structure above context.
- Switched from "project" to "module" for SrcGen4J config.
