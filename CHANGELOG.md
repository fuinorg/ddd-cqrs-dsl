# Changelog

## 1.6.0
- Made the `namespace` optional: a `context` may now hold its imports and elements directly, without an enclosing `namespace`.
- Elements declared directly in a context are generated into the `project.context` package; the SrcGen4J `package` pattern's namespace segment is now optional, written as `${project}.${module}.${group}.${context}[.${namespace}]`.

## 1.5.0
- Added project structure above context.
- Switched from "project" to "module" for SrcGen4J config.
