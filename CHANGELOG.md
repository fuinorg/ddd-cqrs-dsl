# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
Initial version
- New `process-manager` element for modelling orchestration (sagas): it reacts to domain events and issues commands through a small, documented state machine — `process-states` plus guarded `reacts-to <Event> in-state <State>` reactions that `correlate-by` an event attribute, `issues-commands`, `transition-to` another state, and `arm-timeout`/`cancel-timeout`. Every state and reaction can carry its own doc comment. Grammar-only for now (like `command-handler`/`view`); code generation of a concrete process-manager class follows.
- A method whose `returns` is declared `optional` is generated as a `java.util.Optional` of the declared type (`returns optional String` becomes `Optional<String>`), so an absent result is part of the signature instead of a remark in the documentation.
