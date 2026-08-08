# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
Initial version

- A `business-rule` may be declared **at module level**, not only inside an entity or an aggregate, so a
  rule that applies across contexts is stated once and imported like any other type. An aggregate's own
  rules are unaffected: they still live in its `businessRules` and still win over an imported rule of the
  same name. Entity and aggregate bodies accept `EntityElement` - everything a module may hold except a
  `business-rule` - because allowing it in both made the two indistinguishable to the parser and a rule
  written inside an aggregate silently ended up among its nested elements.
- The checks restricting what an entity or an aggregate may nest now look at the `elements` feature
  instead of at every `AbstractElement` they contain, so an aggregate's own `business-rule` is no longer
  reported as an illegal nested element.
- **ddd-templates:** an enum's `isValid`/`valueOf` compare from their parameter rather than from the
  getter (`SrcStaticEnumCode`). The getter of a non-optional attribute is generated as a Java primitive,
  and `int` has no `equals` - so an enum with a numeric base did not compile.
- **Breaking:** a model artifact is a plain `zip` instead of a jar (`CqrsArtifactResolver.EXTENSION`) -
  no classifier, `.cqrs` files below `model/`. The console verifier and the SrcGen4J build resolve
  type `zip` only, so an artifact published as a jar no longer resolves.
- A `SrcGen4J` script path is written **from the enclosing `model` folder** instead of relative to the
  `.cqrs` that declares the hint (`CqrsScripts`), so the same path works on disk and inside the
  artifact at any depth. A model in no `model` folder keeps the old relative behaviour.
- The aggregate an event belongs to is looked for in **every model read** instead of only in the
  event's own file (`CqrsEventExtensions.getFiringEntity`). An event bound to its aggregate by a bare
  `fires` clause otherwise degrades to a plain one once a module is split across files.
