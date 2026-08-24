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
  Nothing is generated from any of it yet.
- Initial version
