# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
- Grammar: `identified-by` on a value object, `key`/`no-key` and `soft-delete` on an aggregate or
  entity, attributes and a `requires` predicate on a business rule, bound actuals on a rule usage,
  `slabel`/`label`/`tooltip` on a command, and a `hint` slot wherever wording may sit. Local cross
  references (a rule's own attributes, a row's identity, a key's attributes, an enumeration's values)
  are narrowed by `CqrsDslScopeProvider` so they cannot resolve to a same named element elsewhere.
- Flutter: a row's identity is read from `identified-by` rather than parsed out of a `@Key` annotation,
  so naming an attribute the row does not have is a resolution error instead of a screen with no
  identity. A row that declares an `EntityIdPath` as its identity is recognised as identified by it -
  a child of a root there are many of cannot be addressed by its own id - while an undeclared path
  stays a column, because a row carries a path to another thing far more often than to itself. The
  annotation is no longer read at all, and is gone from `cqrs-common`.
- A newly created write-once operation now **calls its validator in one line** - `new XRules(this).op(...)`,
  or the static form for a create - instead of carrying one `// TODO Verify …` comment per rule. That
  line is the whole contract between the two: adding a rule to an operation never means editing the
  write-once file again. An existing file is untouched, being written once and never regenerated.
- The class that **verifies everything one aggregate or entity declares** is now generated: one method
  per operation, constructing each rule from the actuals the usage binds. Nothing can be skipped - the
  write-once operation names its operation and nothing else, and no method on the validator takes a rule
  from outside, so the model is the complete list of what is enforced. A creating operation gets a
  static method, having no instance to read state from.
- An aggregate's and an entity's **declared attributes now reach the generated abstract**, as fields with
  a `public final` getter and a `protected final` setter - until now the abstract carried only the
  identity, and every attribute the model declared lived as a hand-written field in the write-once class
  with no accessor. A rule validator is a separate class and can only read what the aggregate exposes, so
  it could read almost nothing. **This is not source compatible**: a write-once class that declares its
  own getter for a declared attribute no longer compiles, which is deliberate - the alternative silently
  shadows the field, leaving the validator reading one copy while the operations write another.
- A `business-rule` that declares a `requires` condition now generates the class that verifies it: the
  rule's attributes as its constructor, the condition as the check, and the model's own exception thrown
  when it does not hold. `==` becomes `Objects.equals` (Java's own is identity and every attribute here
  is a value object), only a comparison against `null` stays `== null`, and ordering a date is
  `compareTo`. A rule with **no** `requires` generates nothing at all - those conditions are written by
  hand, and a stub would let a newly declared rule appear unenforced with the build still green.
- A command's `message` is validated: a plain variable or a dotted path, over the command's own
  attributes or its target operation's parameters, plus the implicit `entityIdPath`. Stricter than an
  event's message on purpose - the client renders a command's prompt before sending, and has no
  expression language - so the two renderers cannot drift.
- Flutter: a generated Dart enum now carries the attributes the model declares on it, as final fields
  beside the wire name, plus an `operator []` to reach one by name. Without them a client cannot resolve
  `${provider.id}` in a command message at all - its idea of the provider is the wire name `BAZG_CH`
  where the model's `id` says `BAZG` - so the two renderers would disagree about the same sentence. An
  attribute of a type no Dart `const` can hold is refused at generation time rather than emitted as
  uncompilable Dart. An enumeration that declares no attributes generates exactly what it did.
- Flutter: a command's own `slabel`/`label`/`tooltip` reach its descriptor and the translation bundle,
  so a client captions a button with the wording the model gives rather than the documentation.
- Initial version
