# Changelog

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
