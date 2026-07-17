# Changelog

Reflects only changes made in the Eclipse plugin.

## 1.10.0
- A `view` may now declare `business-rule`s and `method`s in a body, the same way a `service` does, so the operations that query a read model can be modelled where the view is defined. The body is mandatory (it may be empty), so an existing `view X uses Y` becomes `view X uses Y { }`.
- Documented that a `projection` may be declared without any `input` events; this already worked but was never spelled out.

## 1.9.0
- Removed the validation rule that required a value object with a `base` to have exactly one attribute of the base type. The rule contradicted the code generation: only "`base String` plus exactly one attribute" is generated as a complete class, while every other shape is generated as an abstract base class plus a hand-written final class that supplies `asBaseType()`. Value objects such as `PhoneNumber` (a `base String` packing a `PhoneType` and the number into one representation) are legal and no longer flagged.

## 1.8.0
- A method's `returns` type may now declare optional generic type arguments, using the same `<...>` syntax already available for attributes and parameters (e.g. `returns List<Customer>`). The generated method signature renders the generic return type and imports the argument types.

## 1.7.0
- Generated event/command/exception messages now use `KeyValueEL` (Jakarta Expression Language), so a `${...}` placeholder may contain a full EL expression (e.g. `${name.toUpperCase()}`, `${quantity * price}`).
- Message-variable validation now only checks simple `${name}` placeholders against the declared attributes; complex EL expressions are no longer flagged as unknown variables.
- Replaced the `${#entityIdPath}` special syntax with the ordinary implicit variable `${entityIdPath}` (the `#` prefix is gone).

## 1.6.0
- Made the `namespace` optional: a `context` may now hold its imports and elements directly, without an enclosing `namespace`.
- Elements declared directly in a context are generated into the `project.context` package; the SrcGen4J `package` pattern's namespace segment is now optional, written as `${project}.${module}.${group}.${context}[.${namespace}]`.

## 1.5.0
- Added project structure above context.
- Switched from "project" to "module" for SrcGen4J config.
