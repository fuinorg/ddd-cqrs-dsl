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
- Flutter: a command's own `slabel`/`label`/`tooltip` reach its descriptor and the translation bundle,
  so a client captions a button with the wording the model gives rather than the documentation.
- Initial version
