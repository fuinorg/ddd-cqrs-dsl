# Add a `Hint` element (with simplified JSON) to the CqrsDsl grammar

## Context
Add a new `Hint` element to the CqrsDsl grammar so a `context` can carry named, JSON-structured
generator hints. Syntax: `hint <FQN> <json>`, positioned in the `Context` block **before `namespaces`**
(multiple hints allowed, like `namespaces`). The JSON is a "simplified" but standard-shaped JSON value
(double-quoted object keys). Scope is **grammar + model only** — no validator/scope/generator/test
changes. Regeneration and compilation are done manually.

## Change — grammar only
Edit the grammar source `eclipse/org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDsl.xtext` (the Eclipse
project is the source of truth) and the mirrored Maven copy
`maven/org.fuin.dsl.cqrs/src/main/java/org/fuin/dsl/cqrs/CqrsDsl.xtext` (identical edit, so a `mvn`
build of the Maven module also picks it up regardless of whether the mirror script is run first). Do
**not** touch `bin/` or `src-gen/` copies — those are regenerated.

### 1. `Context` rule — insert `hints+=Hint*` before namespaces
```
Context:
    'context' name=FQN '{'
        hints+=Hint*
        namespaces+=Namespace*
    '}';
```

### 2. New `Hint` rule (placed right after the `Import` rule)
Carries an optional leading doc comment via `doc=DOC?`, like the other types (the `DOC` = `/** … */`
terminal is not hidden, so without this a doc comment before a `hint` is a parse error; ordinary `//`
and `/* */` comments are hidden and already allowed).
```
/** A named, JSON-structured generator hint. */
Hint:
    doc=DOC?
    'hint' name=FQN json=JSON;
```

### 3. New simplified-JSON value rules (placed with the other value rules, after `Literal`)
Reuses the existing `STRING` terminal and `Number` datatype rule — no new terminals.
```
/** A simplified JSON value. */
JSON:
    JsonObject | JsonArray | JsonString | JsonNumber | JsonBoolean | JsonNull;

/** JSON object with double-quoted string keys. */
JsonObject:
    {JsonObject} '{' (members+=JsonMember (',' members+=JsonMember)*)? '}';

/** A single "key": value member of a JSON object. */
JsonMember:
    key=STRING ':' value=JSON;

/** JSON array of values. */
JsonArray:
    {JsonArray} '[' (elements+=JSON (',' elements+=JSON)*)? ']';

/** JSON string value. */
JsonString:
    value=STRING;

/** JSON number value. */
JsonNumber:
    value=Number;

/** JSON boolean value. */
JsonBoolean:
    value=('true' | 'false');

/** JSON null value. */
JsonNull:
    {JsonNull} 'null';
```

Notes:
- The alternatives dispatch on distinct first tokens (`{`, `[`, STRING, number, `true`/`false`, `null`),
  so there is no parser ambiguity; `{`/`}`/`[`/`]`/`:`/`,` are just keywords in JSON position.
- Additive change: regeneration adds new EClasses (`Hint`, `JSON`, `JsonObject`, `JsonMember`,
  `JsonArray`, `JsonString`, `JsonNumber`, `JsonBoolean`, `JsonNull`) and one containment reference
  (`Namespace.hints`) in `org.fuin.dsl.cqrs.cqrsDsl`. Existing `templates` imports of `Namespace`/etc.
  keep compiling; nsURI and package name are unchanged.
- `Number` (reused) has no leading `-`, so negative JSON numbers aren't accepted — acceptable for the
  "simplified" scope; can be extended later if needed.

## Example usage
```
context com.example.shop {

    hint com.example.shop.Order {
        "ui": { "icon": "cart", "color": "green" },
        "tags": ["aggregate", "sales"],
        "priority": 1,
        "experimental": true,
        "owner": null
    }

    namespace orders {
        // ... imports and elements ...
    }
}
```

## Verification
- Regenerate the Eclipse artifacts via the `GenerateCqrsDsl` MWE2 launch; build the Maven module
  (`mvn` re-runs MWE2 + parse tests). Optionally add a `dsl-examples/*.cqrs` using a `hint` — it is
  auto-covered by `DslExamplesParsingTest`.

---

# Adjust IntelliJ plugin

The IntelliJ plugin under `intellij/` is a **separate, independent implementation** of the same
language — it has no EMF/Xtext model. It generates its lexer, parser and PSI from two hand-maintained
grammar sources via Grammar-Kit (`build.gradle` tasks `generateCqrsLexer` / `generateCqrsParser`, output
under `build/generated/sources/grammarkit/`). So the Xtext change above does **not** propagate here; the
`hint` element and simplified-JSON value must be mirrored by hand. Unlike Xtext — where `[`, `]`, `:` and
the `hint` keyword are auto-created from literal usage — Grammar-Kit/JFlex require every token to be
declared explicitly in the BNF token list **and** matched in the JFlex lexer, and the hand-written
support classes that enumerate token types must be updated too.

## Change — grammar sources

### 1. `intellij/src/main/grammar/CqrsDsl.flex` — lexer

Add the `hint` keyword alongside the other structural keywords (it must precede the `{ID}` rule, which it
already does since keywords are listed before terminals):
```
"hint"                    { return KW_HINT; }
```
Add the three new punctuation tokens in the `// ---- Punctuation ----` block:
```
":"                       { return COLON; }
"["                       { return LBRACKET; }
"]"                       { return RBRACKET; }
```
`STRING` and `NUMBER` are reused as-is (the existing `NUMBER` already matches `HEX | INT | DECIMAL` with
an optional dotted part, matching the Xtext `Number` datatype); `true` / `false` / `null` already lex to
`KW_TRUE` / `KW_FALSE` / `KW_NULL`.

### 2. `intellij/src/main/grammar/CqrsDsl.bnf` — token list

Declare the new tokens in the `tokens=[ ... ]` block. Put `KW_HINT='hint'` with the structural keywords
and the punctuation with the other symbols:
```
KW_HINT='hint'
...
COLON=':'
LBRACKET='['
RBRACKET=']'
```

### 3. `CqrsDsl.bnf` — `context_def` rule: insert `hint_def*` before `namespace_def*`
Mirrors the Xtext `Context` edit (hints before `namespaces`):
```
context_def ::= KW_CONTEXT qualified_name LBRACE hint_def* namespace_def* RBRACE {
  pin=1
  implements="org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement"
  mixin="org.fuin.dsl.cqrs.intellij.psi.impl.CqrsNamedElementImpl"
}
```

### 4. `CqrsDsl.bnf` — new `hint_def` rule (placed right after `import_decl` / `import_fqn`)
The Xtext side is `Hint: 'hint' name=FQN json=JSON;` — `name` is an FQN definition name (not a
cross-reference), so it is mirrored with `qualified_name` and the `CqrsNamedElement` mixin, consistent
with `context_def` / `namespace_def`:
```
hint_def ::= KW_HINT qualified_name json {
  pin=1
  implements="org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement"
  mixin="org.fuin.dsl.cqrs.intellij.psi.impl.CqrsNamedElementImpl"
}
```

### 5. `CqrsDsl.bnf` — simplified-JSON rules (placed with the value rules, near `literal`)
Direct mirror of the Xtext `JSON` rules; reuses the `STRING` and `NUMBER` terminals and the existing
`KW_TRUE` / `KW_FALSE` / `KW_NULL` keywords:
```
json ::= json_object | json_array | json_string | json_number | json_boolean | json_null

json_object ::= LBRACE (json_member (COMMA json_member)*)? RBRACE { pin=1 }

json_member ::= STRING COLON json { pin=1 }

json_array ::= LBRACKET (json (COMMA json)*)? RBRACKET { pin=1 }

json_string ::= STRING

json_number ::= NUMBER

json_boolean ::= KW_TRUE | KW_FALSE

json_null ::= KW_NULL
```
The alternatives dispatch on distinct first tokens (`{`, `[`, STRING, NUMBER, `true`/`false`, `null`), so
there is no ambiguity — same reasoning as the Xtext version. `json_object` reuses `LBRACE`/`RBRACE`; a
`hint` block therefore looks like a brace block but is parsed by the JSON rules, not `namespace`/element
rules.

## Change — support classes (needed because they enumerate token types by hand)

### 6. `intellij/src/main/java/.../CqrsTokenSets.java`
- Add `CqrsTypes.KW_HINT` to the `KEYWORDS` set (so `hint` highlights as a keyword and is covered by
  `isKeyword`).
- Highlight the new punctuation: add `CqrsTypes.LBRACKET, CqrsTypes.RBRACKET` to the `BRACES` set (or a
  new `BRACKETS` set) and `CqrsTypes.COLON` to the `OPERATORS` set.

  These are consumed by `CqrsSyntaxHighlighter.getTokenHighlights(...)`, which is data-driven off these
  sets — no change needed in the highlighter itself.

### 7. `intellij/src/main/java/.../CqrsBraceMatcher.java` (optional polish)
Add a `[` / `]` pair so bracket matching works inside JSON arrays:
```
new BracePair(CqrsTypes.LBRACKET, CqrsTypes.RBRACKET, false),
```

### 8. `intellij/src/main/java/.../completion/CqrsCompletionContributor.java` (optional polish)
`keywordsFor(position)` offers block-specific keywords. To propose `hint` at context scope, add
`keywords.add("hint")` in the top-level branch (right where `namespace` is offered, since `hint` is now
a sibling of `namespace` inside a context). Not required for parsing.

Notes:
- `CqrsTypes` (the generated `elementTypeHolderClass`) gains the `KW_HINT`, `COLON`, `LBRACKET`,
  `RBRACKET` constants and the `hint_def` / `json*` PSI element types automatically on regeneration, so
  the references in steps 6–8 resolve only after the grammar is regenerated (build them first, or the
  `CqrsTokenSets` edit won't compile).
- No color-settings-page change is required unless a distinct attribute for `[ ]` / `:` is wanted; the
  reused `BRACES` / `OPERATORS` attributes apply automatically.

## Verification (IntelliJ)
- Run `./gradlew generateCqrsLexer generateCqrsParser` (JDK 21 toolchain, auto-provisioned) to regenerate
  the lexer/parser/PSI, then `./gradlew compileJava` to confirm the support-class edits compile against
  the freshly generated `CqrsTypes`.
- Add a `*.cqrs` sample using a `hint` under `intellij/src/test/resources/examples/` and exercise it with
  the plugin's parsing test (mirrors the Xtext `DslExamplesParsingTest`); or open a file with a `hint`
  block in a `./gradlew runIde` sandbox and confirm it parses without error and highlights.
