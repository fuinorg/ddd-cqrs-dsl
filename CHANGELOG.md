# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
- Grammar: `identified-by` on a value object, `key`/`no-key` on an aggregate or entity, attributes and a
  `requires` predicate on a business rule, bound actuals on a rule usage - the carrier's own identity
  with `own-id` and its prior state with `own`, either handed to the rule or on to a service call - a
  written-out value on the right of a comparison, `slabel`/`label`/`tooltip` on a command, and a `hint`
  slot wherever wording may sit. Local cross references are narrowed by `CqrsDslScopeProvider` so they
  cannot resolve to a same named element elsewhere.
- A `business-rule` with a `requires` condition generates the class that verifies it; one without
  generates nothing, because a stub would let a declared rule go unenforced with the build green.
- The class that verifies everything one aggregate or entity declares is generated, one method per
  operation, so the model is the complete list of what is enforced. A creating operation gets a static
  method, having no instance to read state from.
- A newly created write-once operation calls its validator in one line instead of carrying a
  `// TODO Verify …` per rule. An existing file is untouched.
- An aggregate's and an entity's declared attributes now reach the generated abstract, as fields with a
  `public final` getter and a `protected final` setter. **This is not source compatible**: a write-once
  class declaring its own getter for a declared attribute no longer compiles, which is deliberate.
- A command's `message` is validated, and stricter than an event's, so the two renderers cannot drift.
- A view row that offers a command whose client-answerable gates it cannot answer is reported as a
  warning, naming the row, the command, the rule and what it would have to publish.
- Flutter: a row's identity is read from `identified-by` rather than parsed out of a `@Key` annotation,
  which is no longer read at all and is gone from `cqrs-common`. A declared `EntityIdPath` identity is
  recognised as one; an undeclared path stays a column.
- Flutter: a command carries the rules a client can answer for itself, as a const predicate tree plus
  where each value comes from. Advisory and deliberately incomplete - a rule needing a service call or an
  untyped parameter is left out entirely, and a command with none carries no field at all.
- Flutter: each of those rules also carries its refusal's own wording, so an action a screen greys out
  and the same action pressed anyway say one thing rather than two.
- Flutter: a generated Dart enum carries the attributes the model declares on it, plus an `operator []`
  to reach one by name, without which a client cannot resolve `${provider.id}` in a command message. An
  attribute no Dart `const` can hold is refused at generation time.
- Flutter: a command's own `slabel`/`label`/`tooltip` reach its descriptor and the translation bundle.
- Initial version
