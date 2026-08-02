# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
Initial version

- **Breaking:** a model artifact is a plain `zip` instead of a jar (`CqrsArtifactResolver.EXTENSION`) -
  no classifier, `.cqrs` files below `model/`. The console verifier and the SrcGen4J build resolve
  type `zip` only, so an artifact published as a jar no longer resolves.
- A `SrcGen4J` script path is written **from the enclosing `model` folder** instead of relative to the
  `.cqrs` that declares the hint (`CqrsScripts`), so the same path works on disk and inside the
  artifact at any depth. A model in no `model` folder keeps the old relative behaviour.
- The aggregate an event belongs to is looked for in **every model read** instead of only in the
  event's own file (`CqrsEventExtensions.getFiringEntity`). An event bound to its aggregate by a bare
  `fires` clause otherwise degrades to a plain one once a module is split across files.
