# TODO

## Split the templates into a neutral part and the two targets

`org.fuin.dsl.ddd.gen` is the **Java** target, and `org.fuin.dsl.ddd.flutter` is the **Dart** one — but
the base they share still lives inside the Java half. `AbstractSource` is what every factory of both
targets extends, and it carries `asPackage(...)` and `joinPackage(...)`, which are Java-package helpers
the Dart factories inherit and never call.

Nothing is broken by it. What it costs is that "which of these may a second target use" has no answer
you can read off the package name, and the next target added would inherit the same mixture.

The split:

- **neutral** — the artifact-factory base (`init`, `newArtifact`, module/folder resolution), `TypeKeys`,
  `MapExtensions`/the code reference registry, `instanceMeta`, `bundleName`, `contextSegment`,
  `subModule`. Everything a target needs *to be* a target.
- **java** — `asPackage`/`joinPackage` and the `Src*` snippet classes, which emit Java and nothing else.
- **flutter** — already separate; it imports only the three neutral pieces today.

Cheap to describe, not cheap to do: `AbstractSource` is extended by 51 factories, so the move touches
all of them plus their tests. Worth doing when the Dart target is complete enough that the shared half
has stopped moving — doing it while both are changing would mean rebasing the split repeatedly.


## Let a business rule say which attribute it is about

A command refusal arrives as the exception's class name and the model's own wording. Which *field* the
rule was about is not on the wire, and a form wants it: showing "a category named X already exists"
under the name field is a correction, showing it above the form is a puzzle.

Today the Dart target derives it two ways, and both are the model speaking rather than a guess:

1. the exception carries an attribute with the same name as one of the command's, or
2. the command has exactly one attribute, so any field-level refusal is about it.

`CreateCategoryCommand` falls through both. `DuplicateCategoryNameException` carries a `name` **and** a
`kind`, and the command has an attribute of each — so the model does not say. Deriving it from the
exception's class name would put it on `name` and would be a guess that stops working the moment the
attribute is called `newName`, which is exactly what `RenameCategoryCommand` calls it.

What would settle it is a way for a rule to name what it guards, e.g.

    business-rule NameMustBeUniqueForType about name exception DuplicateCategoryNameException

A grammar change, so not free — but it is the one piece of a command form that cannot currently be
generated, and every client either guesses it or does without.


## The Dart fixtures only cover a self-contained model

`templates/src/test/expected-dart/` is generated from `dart-categories.cqrs`, which declares its own
external types and depends on nothing. So the fixtures pin the case where a project generates every type
it uses, and say nothing about the case where it imports a published package — which is the ordinary one
for an application: the shared context is published as its own Dart package and taken from there, the
way the JVM side takes it from a jar. The only difference in the output is which package the imports
name, which is exactly the kind of difference that goes wrong quietly.

`DartForeignPackageTest` now covers it directly. What remains open is the *fixtures*: they still show
only the self-contained shape, so a reader comparing them against a real application's output will find
eleven files differing in their import lines and no note saying why.
