# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
Initial version
- A method whose `returns` is declared `optional` is generated as a `java.util.Optional` of the declared type (`returns optional String` becomes `Optional<String>`), so an absent result is part of the signature instead of a remark in the documentation.
