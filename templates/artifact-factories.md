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
| AggregateId | [AggregateIdStreamFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdStreamFactoryArtifactFactory.xtend) | Generates the aggregate-id stream factory Java class. |  |
| AggregateId | [FinalAggregateId](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/FinalAggregateIdArtifactFactory.xtend) | Generates the final (concrete) aggregate identifier Java class. |  |
| AggregateId | [SimpleAggregateId](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/SimpleAggregateIdArtifactFactory.xtend) | Generates a simple aggregate identifier Java class. |  |
| Command | [Command](src/main/java/org/fuin/dsl/ddd/gen/command/CommandArtifactFactory.xtend) | Generates a command Java class. | - |
| Constraint | [Validator](src/main/java/org/fuin/dsl/ddd/gen/constr/ValidatorArtifactFactory.xtend) | Generates a constraint validator Java class. |  |
| Constraint | [ValidatorAnnotation](src/main/java/org/fuin/dsl/ddd/gen/constr/ValidatorAnnotationArtifactFactory.xtend) | Generates a constraint validation annotation Java class. |  |
| Entity | [AbstractEntity](src/main/java/org/fuin/dsl/ddd/gen/entity/AbstractEntityArtifactFactory.xtend) | Generates an abstract base entity Java class. |  |
| Entity | [FinalEntity](src/main/java/org/fuin/dsl/ddd/gen/entity/FinalEntityArtifactFactory.xtend) | Generates the final (concrete) entity Java class. |  |
| EntityId | [AbstractEntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/AbstractEntityIdArtifactFactory.xtend) | Generates an abstract base entity identifier Java class. |  |
| EntityId | [EntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/EntityIdArtifactFactory.xtend) | Generates an entity identifier Java class. |  |
| EntityId | [FinalEntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/FinalEntityIdArtifactFactory.xtend) | Generates the final (concrete) entity identifier Java class. |  |
| EntityId | [SimpleEntityId](src/main/java/org/fuin/dsl/ddd/gen/entityid/SimpleEntityIdArtifactFactory.xtend) | Generates a simple entity identifier Java class. |  |
| EnumObject | [AbstractEnum](src/main/java/org/fuin/dsl/ddd/gen/enumobject/AbstractEnumArtifactFactory.xtend) | Generates an abstract base enum Java class. |  |
| EnumObject | [Enum](src/main/java/org/fuin/dsl/ddd/gen/enumobject/EnumArtifactFactory.xtend) | Generates an enum Java class. |  |
| EnumObject | [FinalEnum](src/main/java/org/fuin/dsl/ddd/gen/enumobject/FinalEnumArtifactFactory.xtend) | Generates the final (concrete) enum Java class. |  |
| Event | [Event](src/main/java/org/fuin/dsl/ddd/gen/event/EventArtifactFactory.xtend) | Generates a domain event Java class. |  |
| Event | [EventTest](src/main/java/org/fuin/dsl/ddd/gen/event/EventTestArtifactFactory.xtend) | Generates a JUnit test class for a domain event. |  |
| Exception | [Exception](src/main/java/org/fuin/dsl/ddd/gen/except/ExceptionArtifactFactory.xtend) | Generates an exception Java class. |  |
| ResourceSet | [CtxExternalTypes¹](src/main/java/org/fuin/dsl/ddd/gen/resourceset/CtxExternalTypes.xtend) | Registers a set of external types (Byte, String, Date, UUID, …); does NOT create any source code. |  |
| ResourceSet | [PackageInfo](src/main/java/org/fuin/dsl/ddd/gen/resourceset/PackageInfoArtifactFactory.xtend) | Creates a `package-info.java` annotated with JSpecify's `@NullMarked` once for every generated package. |  |
| Service | [Service](src/main/java/org/fuin/dsl/ddd/gen/service/ServiceArtifactFactory.xtend) | Generates the service interface Java class. | Rename to ServiceInterfaceArtifactFactory |
| ValueObject | [AbstractValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/AbstractValueObjectArtifactFactory.xtend) | Generates an abstract base value object Java class. |  |
| ValueObject | [CombinedAbstractValueObject²](src/main/java/org/fuin/dsl/ddd/gen/valueobject/CombinedAbstractValueObjectArtifactFactory.xtend) | Delegates to `SimpleStringValueObject` when possible, otherwise to `AbstractValueObject`. |  |
| ValueObject | [CombinedValueObject²](src/main/java/org/fuin/dsl/ddd/gen/valueobject/CombinedValueObjectArtifactFactory.xtend) | Delegates to `SimpleStringValueObject` when possible, otherwise to `ValueObject`. |  |
| ValueObject | [FinalValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/FinalValueObjectArtifactFactory.xtend) | Generates the final (concrete) value object Java class. |  |
| ValueObject | [SimpleStringValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/SimpleStringValueObjectArtifactFactory.xtend) | Generates a simple String-based value object Java class. |  |
| ValueObject | [SimpleStringValueObjectTest](src/main/java/org/fuin/dsl/ddd/gen/valueobject/SimpleStringValueObjectTestArtifactFactory.xtend) | Generates a JUnit test class for a simple String-based value object. |  |
| ValueObject | [ValueObject](src/main/java/org/fuin/dsl/ddd/gen/valueobject/ValueObjectArtifactFactory.xtend) | Generates a value object Java class. |  |

**39 types total.**

## Notes

- ¹ `CtxExternalTypes` has no `ArtifactFactory` suffix to strip. It only registers
  external type references and explicitly `return Collections.emptyList()`
  (*"Will never produce anything"*) → **0** artifacts.
- ² `CombinedValueObject` and `CombinedAbstractValueObject` contain no direct
  `newArtifact` call; they delegate to the simple/normal factory, each of which
  yields **1** artifact (the abstract one can also short-circuit to `null` = 0 when
  the value object has no base).
- All other factories return a single-element list (`List.of(newArtifact(...))`),
  i.e. **1** artifact each (or `null`/0 during the preparation run or when
  preconditions aren't met).
- Source of truth is the `.xtend` files; the generated Java copies under `xtend-gen/`
  and `generated-sources/xtend/` are partly stale/inconsistent.


## Thinking

project => org.fuin.examples.cqrskeycloak 
context => household
namespace => person

package => org.fuin.examples.cqrskeycloak.shared.domain.household.person

/** A hint for the SrcGen4J code generation. */
hint SrcGen4J {
  "package": "${project}.${module}.${group}.${context}.${namespace}",
  "types": [
    { 
      "type": "ValueObject",
      "module": "shared",
      "group": "domain",
      "artifacts": [
        { "AbstractValueObject": "genMainJava" },
        { "CombinedAbstractValueObject": "genMainJava" },
        { "CombinedValueObject": "genMainJava" },
        { "FinalValueObject": "genMainJava" },
        { "SimpleStringValueObject": "genMainJava" },
        { "SimpleStringValueObjectTest": "genMainJava" },
        { "ValueObject": "genMainJava" }
      ]
    }
  ]
}
