# Artifact Factories extending `AbstractSource`

Types in `src/main/java/.../gen` (Xtend sources) that extend `AbstractSource`,
ordered by model type (the generic type of `AbstractSource`). Each class links
to the factory's source file.

| Model type | Class | Description | TODO |
| --- | --- | --- | --- |
| Aggregate | [AbstractAggregate](src/main/java/org/fuin/dsl/ddd/gen/aggregate/AbstractAggregateArtifactFactory.xtend) | Generates an abstract aggregate Java class. |  |
| Aggregate | [AggregateDoc](src/main/java/org/fuin/dsl/ddd/gen/aggregate/AggregateDocArtifactFactory.xtend) | Generates an HTML documentation file for an aggregate. |  |
| Aggregate | [ESJpaEvent](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaEventArtifactFactory.xtend) | Generates the JPA event entity Java class for event sourcing. |  |
| Aggregate | [ESJpaEventId](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaEventIdArtifactFactory.xtend) | Generates the JPA event-id Java class for event sourcing. |  |
| Aggregate | [ESJpaLiquibaseXml](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaLiquibaseXmlArtifactFactory.xtend) | Generates a Liquibase XML changelog for the event-sourcing JPA schema. |  |
| Aggregate | [ESJpaStream](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaStreamArtifactFactory.xtend) | Generates the JPA stream entity Java class for event sourcing. |  |
| Aggregate | [ESRepository](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESRepositoryArtifactFactory.xtend) | Generates the event-sourced repository Java class for an aggregate. |  |
| Aggregate | [ESRepositoryFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESRepositoryFactoryArtifactFactory.xtend) | Generates the event-sourced repository factory Java class for an aggregate. |  |
| Aggregate | [FinalAggregate](src/main/java/org/fuin/dsl/ddd/gen/aggregate/FinalAggregateArtifactFactory.xtend) | Generates the final (concrete) aggregate Java class. |  |
| AggregateId | [AbstractAggregateId](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AbstractAggregateIdArtifactFactory.xtend) | Generates an abstract base aggregate identifier Java class. |  |
| AggregateId | [AggregateId](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdArtifactFactory.xtend) | Generates an aggregate identifier Java class. |  |
| AggregateId | [AggregateIdStreamFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdStreamFactoryArtifactFactory.xtend) | Generates the aggregate-id stream factory Java class. | Outdated - `IdStreamFactory` is gone from ddd-4-java |
| AggregateId | [CombinedAggregateId³](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/CombinedAggregateIdArtifactFactory.xtend) | Delegates to `SimpleAggregateId` for a "base UUID" id, otherwise to `AbstractAggregateId` **and** `FinalAggregateId`. |  |
| AggregateId | [FinalAggregateId](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/FinalAggregateIdArtifactFactory.xtend) | Generates the final (concrete) aggregate identifier Java class. |  |
| AggregateId | [SimpleAggregateId](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/SimpleAggregateIdArtifactFactory.xtend) | Generates a simple aggregate identifier Java class. |  |
| Command | [Command](src/main/java/org/fuin/dsl/ddd/gen/command/CommandArtifactFactory.xtend) | Generates a command Java class. | - |
| Constraint | [Validator](src/main/java/org/fuin/dsl/ddd/gen/constr/ValidatorArtifactFactory.xtend) | Generates a constraint validator Java class. |  |
| Constraint | [ValidatorAnnotation](src/main/java/org/fuin/dsl/ddd/gen/constr/ValidatorAnnotationArtifactFactory.xtend) | Generates a constraint validation annotation Java class. |  |
| Entity | [AbstractEntity](src/main/java/org/fuin/dsl/ddd/gen/entity/AbstractEntityArtifactFactory.xtend) | Generates an abstract base entity Java class. |  |
| Entity | [FinalEntity](src/main/java/org/fuin/dsl/ddd/gen/entity/FinalEntityArtifactFactory.xtend) | Generates the final (concrete) entity Java class. |  |
| EntityId | [AbstractEntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/AbstractEntityIdArtifactFactory.xtend) | Generates an abstract base entity identifier Java class. |  |
| EntityId | [EntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/EntityIdArtifactFactory.xtend) | Generates an entity identifier Java class. |  |
| EntityId | [CombinedEntityId³](src/main/java/org/fuin/dsl/ddd/gen/entityid/CombinedEntityIdArtifactFactory.xtend) | Delegates to `SimpleEntityId` for a "base Integer" id, otherwise to `AbstractEntityId` **and** `FinalEntityId`. |  |
| EntityId | [FinalEntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/FinalEntityIdArtifactFactory.xtend) | Generates the final (concrete) entity identifier Java class. |  |
| EntityId | [SimpleEntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/SimpleEntityIdArtifactFactory.xtend) | Generates a simple entity identifier Java class. |  |
| EnumObject | [AbstractEnum](src/main/java/org/fuin/dsl/ddd/gen/enumobject/AbstractEnumArtifactFactory.xtend) | Generates an abstract base enum Java class. |  |
| EnumObject | [Enum](src/main/java/org/fuin/dsl/ddd/gen/enumobject/EnumArtifactFactory.xtend) | Generates an enum Java class. |  |
| EnumObject | [FinalEnum](src/main/java/org/fuin/dsl/ddd/gen/enumobject/FinalEnumArtifactFactory.xtend) | Generates the final (concrete) enum Java class. |  |
| Event | [Event](src/main/java/org/fuin/dsl/ddd/gen/event/EventArtifactFactory.xtend) | Generates a domain event Java class. |  |
| Event | [EventTest](src/main/java/org/fuin/dsl/ddd/gen/event/EventTestArtifactFactory.xtend) | Generates a JUnit test class for a domain event. |  |
| Exception | [Exception](src/main/java/org/fuin/dsl/ddd/gen/except/ExceptionArtifactFactory.xtend) | Generates an exception Java class. |  |
| ProcessManager | [AbstractProcessManager](src/main/java/org/fuin/dsl/ddd/gen/processmanager/AbstractProcessManagerArtifactFactory.xtend) | Generates the abstract process-manager view plus the state enum. Runtime-neutral (uses the `CommandOutbox` SPI). |  |
| ProcessManager | [ProcessManager](src/main/java/org/fuin/dsl/ddd/gen/processmanager/ProcessManagerArtifactFactory.xtend) | Generates the write-once concrete process-manager view with one reaction stub per input event, annotated for the selected `runtime`. |  |
| ResourceSet | [CtxExternalTypes¹](src/main/java/org/fuin/dsl/ddd/gen/resourceset/CtxExternalTypes.xtend) | Registers a set of external types (Byte, String, Date, UUID, …); does NOT create any source code. |  |
| ResourceSet | [PackageInfo](src/main/java/org/fuin/dsl/ddd/gen/resourceset/PackageInfoArtifactFactory.xtend) | Creates a `package-info.java` annotated with JSpecify's `@NullMarked` once for every generated package. |  |
| ResourceSet | [PermissionCatalogue⁷](src/main/java/org/fuin/dsl/ddd/gen/resourceset/PermissionCatalogueArtifactFactory.xtend) | Creates the permission catalogue - one entry per `command` and one per `method` of a `view` - as `PERMISSIONS.md` plus a `PermissionIds` constants class. |  |
| ResourceSet | [SpringBeans](src/main/java/org/fuin/dsl/ddd/gen/resourceset/SpringBeansArtifactFactory.xtend) | Creates one Spring `@Configuration` per side registering every generated bean explicitly, so nothing has to component-scan the generated packages. |  |
| Service | [Service](src/main/java/org/fuin/dsl/ddd/gen/service/ServiceArtifactFactory.xtend) | Generates the service interface Java class. | Rename to ServiceInterfaceArtifactFactory |
| ValueObject | [AbstractValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/AbstractValueObjectArtifactFactory.xtend) | Generates an abstract base value object Java class. |  |
| ValueObject | [CombinedValueObject²](src/main/java/org/fuin/dsl/ddd/gen/valueobject/CombinedValueObjectArtifactFactory.xtend) | Delegates to `SimpleStringValueObject` when possible, otherwise to `AbstractValueObject` **and** `FinalValueObject`. |  |
| ValueObject | [FinalValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/FinalValueObjectArtifactFactory.xtend) | Generates the final (concrete) value object Java class. |  |
| ValueObject | [SimpleStringValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/SimpleStringValueObjectArtifactFactory.xtend) | Generates a simple String-based value object Java class. |  |
| ValueObject | [SimpleStringValueObjectTest](src/main/java/org/fuin/dsl/ddd/gen/valueobject/SimpleStringValueObjectTestArtifactFactory.xtend) | Generates a JUnit test class for a simple String-based value object. |  |
| View | [View](src/main/java/org/fuin/dsl/ddd/gen/view/ViewArtifactFactory.xtend) | Generates the fully generated `<Base>View` (event set, dispatcher wiring, cron), annotated for the selected `runtime`. |  |
| View | [ViewSpringApi⁴](src/main/java/org/fuin/dsl/ddd/gen/view/ViewSpringApiArtifactFactory.xtend) | Generates the Spring `@HttpExchange` REST contract interface `<Base>ControllerApi`. |  |
| View | [ViewQuarkusApi⁴](src/main/java/org/fuin/dsl/ddd/gen/view/ViewQuarkusApiArtifactFactory.xtend) | Generates the JAX-RS + MicroProfile `@RegisterRestClient` REST contract interface `<Base>ResourceApi`. |  |
| View | [ViewServiceApi⁴](src/main/java/org/fuin/dsl/ddd/gen/view/ViewServiceApiArtifactFactory.xtend) | Generates the framework-free service contract `<Base>Service` - the read model's operations as plain Java, and the contract to depend on from inside the same application. |  |
| View | [ViewServiceRestClient⁴](src/main/java/org/fuin/dsl/ddd/gen/view/ViewServiceRestClientArtifactFactory.xtend) | Generates `<Base>ServiceRestClient`, which satisfies `<Base>Service` over HTTP by wrapping the Spring REST contract and unwrapping its `ResponseEntity`. |  |
| View | [ViewRestDelegate](src/main/java/org/fuin/dsl/ddd/gen/view/ViewRestDelegateArtifactFactory.xtend) | Generates the `<Base>Controller`/`<Base>Resource` implementing the contract of the selected runtime. Pure delegation to `<Base>Service`, so it is regenerated rather than written once. |  |
| View | [FinalView⁵](src/main/java/org/fuin/dsl/ddd/gen/view/FinalViewArtifactFactory.xtend) | Generates the write-once `<Base>ServiceImpl` implementing the service contract - where the queries are written - plus one event handler stub per projection event. |  |
| View | [JpaViewTable⁶](src/main/java/org/fuin/dsl/ddd/gen/view/JpaViewTableArtifactFactory.xtend) | Generates the read-model JPA `@Entity` classes from the "JpaHint" hints in the view body. |  |

**51 types total.**

## Notes

- ¹ `CtxExternalTypes` has no `ArtifactFactory` suffix to strip. It only registers
  external type references and explicitly `return Collections.emptyList()`
  (*"Will never produce anything"*) → **0** artifacts.
- ² `CombinedValueObject` contains no direct `newArtifact` call; it delegates to
  `SimpleStringValueObject` (**1** artifact) or to `AbstractValueObject` plus
  `FinalValueObject` (**2** artifacts), and yields `null` = 0 during the preparation
  run. Each delegate resolves its own hint entry, so the abstract class lands in the
  regenerated folder while the final class goes to the non-generated sources. Do not
  configure it together with `AbstractValueObject`/`FinalValueObject`, or those
  artifacts are generated twice.
- ³ `CombinedAggregateId` / `CombinedEntityId` work exactly like `CombinedValueObject`: they delegate
  rather than emit, and must not be configured together with their delegates.
- ⁴ The two view API factories ignore the `runtime` option - **both** contract interfaces are generated
  for every view, normally into a separate api module whose framework dependencies are `<optional>`.
- ⁵ `FinalView` emits **1 + n** artifacts: the `<Base>ServiceImpl` plus one handler per projection event.
- ⁶ `JpaViewTable` emits **one artifact per declared table** and only binds the `View` model type; the
  rendering lives in `base/AbstractJpaTableArtifactFactory` (a base class, not configurable on its own,
  and therefore not listed above).
- ⁷ `PermissionCatalogue` emits **2** artifacts for the whole model (not per element): `PERMISSIONS.md`
  into `shared`/`genMainRes` and `PermissionIds.java` into `shared`/`genMainJava`. Both targets are
  passed explicitly, so `artifact2Target.js` is not consulted for them. It emits **0** when the model
  declares no command and no view at all.
- All other factories return a single-element list (`List.of(newArtifact(...))`),
  i.e. **1** artifact each (or `null`/0 during the preparation run or when
  preconditions aren't met).
- Source of truth is the `.xtend` files; the generated Java copies under `xtend-gen/`
  and `generated-sources/xtend/` are partly stale/inconsistent.

