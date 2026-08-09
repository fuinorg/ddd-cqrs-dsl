# `View` / `ProcessManager` generators — status & remaining work

The generators are implemented; the code, tests and goldens are the source of truth:
`templates/src/main/java/org/fuin/dsl/ddd/gen/view/` and `.../gen/processmanager/`, registered in
`src/main/resources/srcgen4j-default.json`, with round-trip tests + goldens under
`src/test/.../gen/{view,processmanager}/*GeneratorTest.xtend` and
`src/test/expected-java/tst/x/{view,processmanager}/<runtime>/`.

## Implemented

- **Runtime** chosen by the `runtime` generator option (`spring` default | `quarkus`), read via
  `getOptions()`/`getVar`. Hybrid split: shared where runtime-neutral, dedicated where it diverges.
- **Modules** follow a dot-separated `<concern>.<layer>` convention (drops straight into a Java package,
  matching the melkheftken layout): `shared` (VO/ids/enum/exception/constraint/event), `command.api`
  (Command), `command.core` (Aggregate/Entity/Service), `query.core` (View), `query.api` (the view's REST
  contract interface, via a per-artifact `module` override), `process.core` (ProcessManager). Set in
  `srcgen4j-default.json`; `group` is left as-is.
- **View** (`ViewArtifactFactory` + `ViewSpringApiArtifactFactory` + `ViewQuarkusApiArtifactFactory` +
  `ViewServiceApiArtifactFactory` + `ViewServiceRestClientArtifactFactory` +
  `ViewRestDelegateArtifactFactory` → genMainJava, `FinalViewArtifactFactory` → mainJava):
  - `<Base>View` — single fully-generated class (`implements core.View`, event set, dispatcher wiring,
    cron, DI header inlined). No abstract/final split: the view has no hand-written code. → `query.core`.
  - `<Base>ControllerApi` **and** `<Base>ResourceApi` — regenerated **REST contract interfaces**, one per
    dedicated factory, both emitted into `query.api` **regardless of the `runtime` option**: Spring
    `@HttpExchange`/`@GetExchange`, and JAX-RS + MicroProfile `@RegisterRestClient`. No JPA. The api
    module declares `spring-web`, `jakarta.ws.rs-api` and `microprofile-rest-client-api` as *optional*,
    so a consumer picks one interface and adds only its dependency.
  - `<Base>Service` — regenerated **framework-free service contract** (`query.api`): the same operations
    as plain Java, no annotations and no `ResponseEntity`. This is what an in-process caller depends on,
    so the combined deployable reaches the read model by a method call rather than an HTTP round trip.
    An `optional` result is an `Optional<X>` here and a 404 over the wire.
  - `<Base>ServiceRestClient` — regenerated adapter (`query.api`) implementing `<Base>Service` over the
    Spring contract, for a caller in another process. Translates 404 back into an empty result.
  - `<Base>Controller` / `<Base>Resource` — regenerated server class (`query.core`) implementing the
    contract; imports the interface across modules, adds the **non-inherited** class annotation
    (`@RestController` / re-declared `@Path`) and forwards every operation to `<Base>Service`. It holds
    no logic, which is why it is derived rather than write-once.
  - `<Base>ServiceImpl` — write-once (`query.core`), implements `<Base>Service` and carries the
    `EntityManager` queries and the read transaction. The one file a developer works in.
  - `<Event>Handler` — write-once, one per projection event (`getEventType()` + `handle()` stub).
- **ProcessManager** (`AbstractProcessManagerArtifactFactory` → genMainJava,
  `ProcessManagerArtifactFactory` → mainJava, module `process.core`): `Abstract<Base>ProcessManagerView`
  (`implements core.ProcessManagerView`, event set, dispatch → abstract `on<Event>`), `<Base>State`
  enum, and the write-once concrete view (DI header + reaction stubs).
- **Naming** (`gen/base/ArtifactNames`): strips a trailing `View` / `ProcessManager|Process`.
- Entity + DTO are hand-written (out of scope). Bodies are inlined in the factories (no separate
  `Src*` classes).

## Key rationale (non-obvious)

- Controller/resource base is a **pure interface** (no JPA) because JPA/query code is app-specific.
  It works for client **and** server; the server class must add the annotation that does NOT inherit —
  Jakarta REST §3.6 (class-level `@Path` not inherited) and Spring 6.1+ `@HttpExchange` server support.
  Verified against the example's versions (Spring Boot 3.4.4 / Framework 6.2.18, Quarkus 3.21.1).
- Abstract/final split is used **only** where a class carries hand-written code (handlers, controller
  impl, PM reactions); the fully-derivable `<Base>View` is a single regenerated class.
- The contract interfaces live in their own module (`query.api`) via a **per-artifact `module` override**
  in the hint, so they sit in a different package than the `query.core` view/impl. Each api factory
  registers its interface FQN under a runtime-qualified key (`ArtifactNames.restApiRefKey(view, runtime)`)
  so the concrete controller imports the right one across modules — the `*GeneratorTest` runs all
  factories over one shared two-pass context to reproduce this.
- Generating **both** contracts (rather than switching on `runtime`) keeps the api module usable by
  clients on either stack. It is safe in one jar: classes load lazily and annotations whose type is
  absent are ignored, and Quarkus raises nothing for an interface that is both `@RegisterRestClient` and
  implemented by a resource (checked against `quarkus-rest-client-deployment` 3.23.4).

## Not yet generated (remaining/optional work)

- **Quarkus service REST client** — `ViewServiceRestClientArtifactFactory` emits the Spring flavour only.
  A Quarkus consumer injects `@RestClient <Base>ResourceApi` directly, which already returns the value
  rather than a `ResponseEntity`, so the adapter would only have to map a 404 to an empty `Optional`.

- **PM wiring artifact** — currently the concrete PM view relies on `@Component`/`@Dependent`
  auto-discovery; no Spring `@Bean`+`@Import(ProcessManagerConfig)` / Quarkus CDI-producer stub.
- **PM reaction bodies** — `on<Event>` methods are empty `TODO` stubs; `correlate-by` / `issues-commands`
  / `transition-to` / `arm-timeout` / `cancel-timeout` are not code-generated.
- **Timeouts** — `arm-timeout`/`cancel-timeout` do not inject a `ProcessTimeoutService` into the PM view.
- **`correlation-id`** is not wired into a state key; **`business-rule` / `method`** are not mapped to
  extra contract endpoints (only default list + get-by-id).
