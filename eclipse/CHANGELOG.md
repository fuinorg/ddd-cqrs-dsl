# Changelog

Reflects only changes made in the Eclipse plugin.

## 1.33.0
- **An `entity-id` can be `base UUID`** - generated whole from its base type, like `base Integer`.
- **A `base` built from a single value** (`UUID`, a number, a decimal) **must declare exactly one attribute**.

## 1.32.0
- **`cid` is removed.** It generated a `UniquelyNumberedException` whose number nothing on the result
  path ever read; an exception's `code` is now a readable short identifier instead.

## 1.31.0
- **A business `key` generates its uniqueness rule**, the service method answering whether the key is
  taken, and the values each operation hands them. Five checks refuse what cannot be derived.

## 1.30.0
- **An operation can name a business `key` where it names a `business-rule`**, with four checks on where
  a key may sit, its exception, its one `display-as` per type, and the attributes that format names.

## 1.29.0
- **`entity-id-path` declares how an entity is addressed from its root**, with an optional `[min..max]`
  range on a step that may repeat. An entity id is unique inside its root and nowhere else, so a
  reference carrying only the id addresses nothing.
- **`own-path` hands a rule or a service what addresses the carrier**, beside `own-id` and
  `own <attribute>`. Reported on an aggregate, in a constructor, and where no path is declared.

## 1.28.0
- **A row is checked against the gates it offers** - a view row offering a command gated by a rule it
  cannot answer is warned about, naming what it would have to publish. A warning rather than an error:
  whether the row should publish it is a modelling decision with costs on the other side, and what this
  removes is the silence rather than the choice.
- **A business rule is checked against the refusal it names** - a rule that does not hold what its
  exception needs is reported in the model rather than failing generation downstream.
- **A rule usage is checked against what the rule declares** - the actuals bind positionally, so one too
  few silently shifts the rest.
- **A business rule declares the values it is handed and a `requires` condition** over them: `!`,
  `&&`, `||`, parentheses, the six comparisons and `.is-empty()`, every name a cross reference.
- **A rule usage binds its actuals** to what the carrying operation holds, so one rule can be carried by
  operations that agree on nothing.
- **`own-id` hands a rule the carrier's own identity**, in the actuals or on to a service call. A
  constructor may not use it: it is what creates the identity.
- **`own <attribute>` hands a rule what the carrier holds now**, which a bare name cannot reach when the
  operation's parameter is named after the field it would overwrite.
- **A condition may compare against a value written out in it**, such as `referenceCount == 0`. `null`
  keeps its own spelling.
- **A command's `message` is checked, and is stricter than an event's** - a plain variable or a dotted
  path only, because the client renders it before sending and has no expression language.
- **`identified-by` names the attribute that identifies a read model row**, as a cross reference rather
  than the unchecked string `@Key` took.
- **`key`/`no-key` make a business key a construct**: its attributes, an `on-collision` strategy, its own
  `consistency` and an optional `display-as`. `no-key` needs documentation, so the reason cannot be a
  shrug.
- **A `command` carries `slabel`/`label`/`tooltip`** like every other named element except an event.
- **A `hint` may sit wherever wording may**, rather than only on a context and a view.

## 1.27.0
- **A dependency cycle between modules is now an error.** The modules a module depends on are read
  from the references that actually resolve, not from its `import` lines - an unused import would
  otherwise invent a dependency and a fully qualified reference would hide one. The nodes are the
  `module` blocks as declared, which is what keeps the seam between two contexts legal: a process
  manager may react to another context's events without dragging the context it lives beside into a
  cycle. Blocks sharing a name are one module, so a context split over a public and a private file is
  not reported as depending on itself.

## 1.26.0
- **A `module`, a `view` and a `method` may now carry UI meta information** - the same
  `slabel`/`label`/`tooltip`/`prompt`/`examples` block a type and an attribute take, written at the top
  of the block. A module name is a single lowercase identifier and a view name is a type name, so
  neither can be turned into a caption by any rule a client could write - `businesspartners` is not
  "Business partners". What to call a module in a menu, a view on a tab and a method as a screen title
  is now something the model states rather than something each application invents from an identifier.
  All of it stays optional, so every existing model parses unchanged.

## 1.25.0
- **An `enum` instance may now carry UI meta information** - the same
  `slabel`/`label`/`tooltip`/`prompt`/`examples` block an attribute takes, written after the instance
  name. What to call a constant on screen is now something the model states rather than something each
  application derives from the constant's name.

## 1.24.0
- **A `business-rule` may now be declared at module level**, not only inside an entity or an aggregate.
  A rule that means the same thing everywhere - "the entity must not be deleted" - is stated once and
  imported like any other type, so its name, its exception and its consistency classification cannot
  drift apart across contexts. An aggregate may still declare its own rules, and one it declares itself
  still wins over an imported rule of the same name.
- A rule written inside an aggregate is no longer reported as an illegal nested element. Rules became
  model elements with the change above, and the check that restricts what an aggregate may nest looked
  at every element it contained - including the aggregate's own rules.

## 1.23.0
- **The types a `dependency` provides resolve in the editor again** - they were all red with "Couldn't
  resolve reference to ...". An editor's resource set holds only the open file, so a `dependency`
  declared on the `context` in another file, and the models beside a model read as a dependency, are
  now found through the index instead.
- An `import` of a `dependency`'s models is no longer reported as unresolvable, and content assist
  after `import` offers what they provide.
- An `import` that **is** used is no longer marked yellow as unused. The check read the cross references
  as they lay, and Xtext links lazily - a name nobody had asked for yet looked like a name referring to
  nothing. It now resolves them, and stays silent about an import while anything in the block genuinely
  does not resolve.
- `F3` on a type a `dependency` provides opens that model out of the artifact's zip, read-only, the way
  JDT opens a class file from a jar. Nothing is reported in a model opened that way.
- A module spread over several files resolves its own names again: what the module declares itself now
  wins over an imported name whichever file either sits in, which is what splitting a model into a
  published and an internal half requires.
- The `org.fuin.dsl.cqrs.extensions` package is exported, so a bundle requiring the language bundle can
  use the model extensions instead of hitting an access restriction.

## 1.22.0
- The aggregate an event belongs to is looked for in **every model read**, not only in the event's own
  file. An event bound to its aggregate by a bare `fires` clause used to lose that binding - and the
  aggregate id it carries - once the two sat in different files of the same module.

## 1.21.0
- **Breaking:** the artifact a `dependency` names is resolved as a plain `zip` instead of a jar - still
  no classifier, still holding the `.cqrs` files below `model/`, still read in place. The models are
  data: nothing in that artifact ever belongs on a classpath. Re-publish a model artifact as a zip,
  otherwise its coordinate is reported as unresolvable.
- A `SrcGen4J` script path is now written **from the enclosing `model` folder** rather than relative to
  the `.cqrs` file that declares the hint, so one and the same path works on disk and inside the
  artifact, whatever the depth of the model below that folder - which is what lets a published model
  say `public/model2JavaPackage.js` and ship the script next to its models. A model that lies in no
  `model` folder keeps the previous relative behaviour.

## 1.20.0
- **Breaking:** the model has two levels instead of three. `project` is gone and the inner block is a
  `module`: `project P { context C { namespace N { X } } }` becomes `context P { module C.N { X } }`.
  Module names are qualified names, every element must live in a module, and every element's fully
  qualified name - and therefore every generated Java package - is unchanged.
- **Breaking:** a `module` is the unit of visibility. Only what a module declares itself resolves by a
  simple name, so reaching any other module - a sibling of the same context included - needs an
  `import` over the `context.module.Type` path, optionally ending in a wildcard (`ctx.*`,
  `ctx.mod.*`, `ctx.mod.Type`). A fully qualified reference needs none.
- **Breaking:** where models come from is declared in the model itself with
  `dependency "groupId:artifactId:version"` (optionally `local "../wip/src/main/cqrs"`), replacing the
  external `dependencies.json` catalog. A `dependency` makes models resolvable, an `import` decides
  what of them is visible.
- **Breaking:** artifacts are resolved through **m2e**, which is now required, so your `settings.xml` -
  repositories, mirrors, servers, proxies - applies. A model artifact is a plain jar holding the
  `.cqrs` files under `model/`, read in place, so `F3` navigates into the entry and there is no
  `.dependencies-cache/` directory. The `cqrs.*` system properties are gone.
- **Breaking:** the generator hint's package variables are now
  `${context}.${mvnModule}[.${group}].${module}`, and an unknown variable is rejected instead of being
  emitted literally.
- New validation: an unresolvable or duplicate import is an error, an unused one a warning; a
  malformed, duplicated or unresolvable `dependency` is reported on its coordinate.
- Content assist offers only what the surrounding module reaches, and the importable paths after
  `import`.

## 1.19.0
- The service an operation uses is now introduced by the `operation-context` keyword:
  `method rename { CategoryName newName  operation-context RenameService }`. It used to be written as
  a bare type name after the parameters, which was easy to mistake for a parameter that had lost its
  name. **Breaking:** every existing reference needs the keyword; the inline `service` declarations
  themselves are unchanged.

## 1.18.0
- A view `method` may now declare an optional `rest-path` setting the sub path of its REST operation; without it the method name is used. A `{name}` placeholder binds to the parameter of that name, and a `rest-path` outside a view method is flagged.
- **Breaking:** `rest-path` moved from the body into the header of `view` and `method`: `view X uses Y rest-path "/x" { ... }`. The view's `cron-schedule` stays in the body.

## 1.17.0
- A `context` body may now mix `namespace` blocks and type/elements as siblings; previously it was an either/or (all namespaces, or imports and elements directly). Existing models are unaffected.

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
