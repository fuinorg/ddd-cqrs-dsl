# ddd-cqrs-dsl-console

A small [Spring Boot](https://spring.io/projects/spring-boot) command line application that
**verifies CQRS DSL (`*.cqrs`) files** — it parses them with the standalone Xtext runtime, runs the
same syntax and validation checks as the Eclipse/IntelliJ editors, and reports any issues. It is
packaged as an executable **fat jar**, so it needs nothing but a JRE to run (no IDE required).

Typical uses: a quick local sanity check of a model, or a CI gate that fails the build when a
`*.cqrs` file has a syntax error (e.g. a method named after a reserved keyword) or an unresolved
reference.

## Download

Every [Maven build](../../.github/workflows/maven.yml) publishes the fat jar as a downloadable
GitHub Actions artifact named **`ddd-cqrs-dsl-console`**. Open the relevant run under the repository's
*Actions* tab and download it from the *Artifacts* section (it unpacks to
`ddd-cqrs-dsl-console.jar`).

## Build locally

From the `maven` directory:

```bash
./mvnw -pl console -am -DskipTests package
```

The executable jar is written to `console/target/ddd-cqrs-dsl-console.jar`.

## Usage

```bash
java -jar ddd-cqrs-dsl-console.jar [--settings <file>] [--offline] <file-or-directory> [more ...]
```

- Each argument may be a single `*.cqrs` file or a directory (scanned recursively for `*.cqrs`).
- **Pass a directory (or all the files) to validate a whole model together** — references between
  files (e.g. one context importing a type from another) only resolve when every referenced file is
  loaded in the same run.

### Options

| Option | Description |
| --- | --- |
| `--settings <file>` | Maven `settings.xml` used to resolve a `dependency` (default: `~/.m2/settings.xml`). |
| `--offline` | Never download; resolve only from the local repository. |

### Exit codes

| Code | Meaning |
| --- | --- |
| `0` | All verified files are valid |
| `1` | At least one syntax or validation error was found |
| `2` | Usage error (no arguments, an unknown option, or no `*.cqrs` file found) |

## Example

```console
$ java -jar ddd-cqrs-dsl-console.jar --offline my-model/
Verifying: /home/me/my-model/master-data/masterdata.cqrs
  OK - no issues
Verifying: /home/me/my-model/exchange-rates/exchangerates.cqrs
  ERROR   line 224:22  Allowed elements in an aggregate are: 'aggregate-id', 'entity', 'event', 'command' and 'value-object'

Result: 1 error(s), 0 warning(s) across 2 file(s) -> FAILED
```

The verification report is written to `stdout`; the DSL runtime's own diagnostics go to `stderr`, so
the report can be captured cleanly (`... > report.txt`).

## Notes on reference resolution

- **Cross-file references** resolve only when all involved files are part of the same run — pass the
  containing directory.
- **Externally provided models** (e.g. `org.fuin.dsl.cqrs.common.*`) are resolved through the
  `dependency` declarations of the model itself, which are materialized under a
  local Maven repository and read straight out of that zip; references that cannot be resolved are reported as
  errors.
