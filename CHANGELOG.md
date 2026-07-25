# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
Initial version
- `ddd-cqrs-dsl`: the service an operation uses is now introduced by the `operation-context` keyword
  instead of being a bare type name after the parameters. **Breaking** for existing models.
- `ddd-templates`: an operation's `operation-context` is generated as a trailing parameter of the
  aggregate/entity operation, so the body can call the SPI it needs to verify a business rule or fetch
  data. A service declared inline stays a nested interface and is referenced without an import.
- `ddd-templates`: the commented `apply(...)` stub of an operation now spells out one builder setter
  per value the event carries, including `entityIdPath` and `aggregateVersion`, instead of a single
  "set the event's attributes" placeholder.
