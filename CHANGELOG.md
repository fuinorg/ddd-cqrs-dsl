# Changelog
Reflects only changes made in [maven](maven) submodules.

The plugins have their own change notes:
- [Eclipse Plugin and DSL](eclipse/CHANGELOG.md)
- [IntelliJ Plugin](intellij/CHANGELOG.md) 

## 1.0.0-SNAPSHOT
- New generated `UiCatalogue`: the wording a `module`, a `view` and a `method` state, as one class a
  client renders navigation from. It is data rather than annotations because the objects4j-ui
  annotations are `@Target({TYPE, FIELD})` - neither a method nor a package can carry one - and a
  module has no Java element at all. Method keys are the `PermissionIds` ids, so one lookup answers
  both "may I call it" and "what do I call it". Each entry carries its module's resource bundle and
  its key beside the literal, so a `<Module>_de.properties` localizes navigation exactly as it
  localizes the labels generated onto fields - a bundle translates what the model states, it never
  adds to it. A model stating no wording gets no class.
- A `module`, a `view` and a `method` may carry UI meta information
  (`slabel`/`label`/`tooltip`/`prompt`/`examples`), so a client can caption navigation from the model
  instead of deriving it from an identifier. Optional everywhere - existing models are unaffected.
- Initial version
