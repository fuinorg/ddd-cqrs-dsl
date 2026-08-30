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


## The model cannot say an aggregate is a singleton

`CommandDescriptor` now carries `targetOrigin`, so a screen knows where the `entity-id-path` of a
command is supposed to come from: minted by the client, taken from the parent segment of the row being
created under, taken from the row itself, or derived from the command's own attributes. Four cases,
all of them derivable — and one that is not.

An aggregate there is only ever one of looks exactly like an ordinary one. Nothing in the model marks
it, so its constructor reads as `clientGenerated` and a generic screen offering that create would mint
a second one. melkheftken has such an aggregate (`MasterData`, whose single id lives in a hand-written
`MasterDataSingleton` on the JVM side), and it is only out of trouble because a screen matches the
create's `targetType` against the rows it is showing, and no screen lists master data.

What would settle it is the model saying so, e.g. `aggregate MasterData single identifier MasterDataId`,
which is a grammar change. Until then, a create must never be offered on `targetOrigin` alone.


## A composite id is declared in the model and written by hand — **done**

Taken by the first of the two routes this recorded: the encoding is generated from the model on both
sides. `SEPARATOR` and `asString()` moved out of the generate-once final class into the regenerated
abstract one, so the string form is no longer a per-project edit, and the Dart target emits an
`of(...)` constructor built from the same declaration. A client can now address such an aggregate.

Three things the work turned up, worth keeping:

- **The encoding was ambiguous, not merely duplicated.** The split that reads a form back lets only the
  last part contain the separator, and nothing enforced that — so `("x-y", "b")` composed `x-y-b` and
  read back as `("x", "y-b")`, a different identifier reporting itself valid. Every part but the last
  is now percent-escaped, which leaves every previously-safe form byte-identical: `DailyRatesId` is
  still `ECB-2026-08-28`, because a provider's name carries neither separator nor escape character.
- **Not every composite can be generated, and the refusal is right.** `kindOf` knows how to read back an
  enum and a handful of primitives; anything else — a nested identifier, a value object — gets no
  encoding on either side. `AnnualTransactionsId` is `(AccountId, Integer)`, whose form would be
  `<uuid>-<year>`, and a leading UUID is full of separators. The Dart target refuses in step with the
  Java one rather than composing something the generator never produced.
- **Nothing here executes the encoding.** Both fixture sets are byte-compared, and — contrary to what is
  easy to assume — `expected-java` is not added as a source root either, so neither is compiled. The
  round trip is exercised downstream, in melkheftken's `DailyRatesIdTest` and `daily_rates_id_test.dart`,
  which assert the same literal from the two sides.

Still open, and now the thing that stops a *generic* client offering such a command: a descriptor says
a command's target origin is `derived` but not which of its attributes make up the key, or in what
order. melkheftken composes the path in its application layer, calling the generated constructor, which
keeps the encoding in one place at the cost of a line per command. Emitting the key's parts onto the
descriptor would remove that line — but a renderer cannot import generated code, so it would have to
reproduce the escaping and the date format instead, putting the encoding back in two places.
