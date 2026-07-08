# Add a `Hint` element (with simplified JSON) to the CqrsDsl grammar

## Context
Add a new `Hint` element to the CqrsDsl grammar so a `namespace` can carry named, JSON-structured
generator hints. Syntax: `hint <FQN> <json>`, positioned in the `Namespace` block **after `imports`,
before `elements`**. The JSON is a "simplified" but standard-shaped JSON value (double-quoted object
keys). Scope is **grammar + model only** — no validator/scope/generator/test changes. Regeneration and
compilation are done manually.

## Change — grammar only
Edit the grammar source `eclipse/org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDsl.xtext` (the Eclipse
project is the source of truth) and the mirrored Maven copy
`maven/org.fuin.dsl.cqrs/src/main/java/org/fuin/dsl/cqrs/CqrsDsl.xtext` (identical edit, so a `mvn`
build of the Maven module also picks it up regardless of whether the mirror script is run first). Do
**not** touch `bin/` or `src-gen/` copies — those are regenerated.

### 1. `Namespace` rule — insert `hints+=Hint*` between imports and elements
```
Namespace:
    'namespace' name=FQN '{'
        imports+=Import*
        hints+=Hint*
        elements+=AbstractElement*
    '}';
```

### 2. New `Hint` rule (placed right after the `Import` rule)
```
/** A named, JSON-structured generator hint. */
Hint:
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
namespace com.example.shop {
    import com.example.common.*

    hint com.example.shop.Order {
        "ui": { "icon": "cart", "color": "green" },
        "tags": ["aggregate", "sales"],
        "priority": 1,
        "experimental": true,
        "owner": null
    }

    // ... elements ...
}
```

## Verification
- Regenerate the Eclipse artifacts via the `GenerateCqrsDsl` MWE2 launch; build the Maven module
  (`mvn` re-runs MWE2 + parse tests). Optionally add a `dsl-examples/*.cqrs` using a `hint` — it is
  auto-covered by `DslExamplesParsingTest`.
