# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
Initial version

- **Breaking:** a model artifact is a plain `zip` instead of a jar (`CqrsArtifactResolver.EXTENSION`) -
  no classifier, `.cqrs` files below `model/`, read in place from the local repository. The models are
  data: nothing in that artifact ever belongs on a classpath. Both the console verifier and the
  SrcGen4J build resolve type `zip` only, so a model artifact published as a jar no longer resolves.
- A `SrcGen4J` script path is written **from the enclosing `model` folder** instead of relative to the
  `.cqrs` that declares the hint (`CqrsScripts`), so the same path works on disk and inside the
  artifact whatever the model's depth below it. A model in no `model` folder keeps the previous
  relative behaviour.
