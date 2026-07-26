# ddd-templates
Xtend based domain-driven design (DDD/CQRS) code generation templates for use with SrcGen4J.

[![Maven Central](https://img.shields.io/maven-central/v/org.fuin.dsl.ddd/ddd-templates.svg)](https://central.sonatype.com/artifact/org.fuin.dsl.ddd/ddd-templates)

## What is this?
The project provides several artifact factories that can be used to generate code using SrcGen4J: 
[srcgen4j](https://github.com/fuinorg/srcgen4j/) from an Xtext based [DDD DSL](../eclipse) model.   

An [artifact factory](https://github.com/fuinorg/srcgen4j/blob/main/commons/src/main/java/org/fuin/srcgen4j/commons/ArtifactFactory.java) is a piece of code that creates an artifact for a given model element.
The result is Java code based on utility classes defined in the [ddd-4-java](https://github.com/fuinorg/ddd-4-java) and
[cqrs-4-java](https://github.com/fuinorg/cqrs-4-java) projects.

Which factories run, and into which module and folder their artifacts go, is decided by the SrcGen4J
configuration. The built-in preset is [srcgen4j-default.json](src/main/resources/srcgen4j-default.json);
a project may declare the same factories in its own `srcgen4j-config.xml`.

### Generator options

Options are SrcGen4J variables. The ones that change *what* is generated:

| Variable | Values | Effect |
| :------- | :----- | :----- |
| `runtime` | `spring` (default) / `quarkus` | Framework annotations of the generated **server** classes (view, controller/resource, process manager). It does **not** affect the REST contract interfaces - see [View Factories](#view-factories). |
| `jpa` | `true` / `false` | JPA annotations and `AttributeConverter` on the generated value objects / ids. |
| `jsonb`, `jackson`, `jaxb`, `jaxb_elements` | `true` / `false` | Serialization annotations and the `AbstractDomainEvent` base an event extends. |
| `copyrightHeader` | text | Header prepended to every generated file. |

## Aggregate Factories
Factories generating code based on the 'aggregate' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [AbstractAggregateArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/AbstractAggregateArtifactFactory.xtend) | [AbstractAggregateRoot](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/AbstractAggregateRoot.java) | Abstract aggregate root Java class. | [AbstractAggregateA](src/test/expected-java/tst/x/aggregates/AbstractAggregateA.java) |
| [FinalAggregateArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/FinalAggregateArtifactFactory.xtend)   | - | Final aggregate root Java source extending the above abstract class. | [AggregateA](src/test/expected-java/tst/x/aggregates/AggregateA.java) |
| [AggregateDocArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/AggregateDocArtifactFactory.xtend)   | - | HTML file with a description of the aggregate. | - |
| [ESJpaEventArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaEventArtifactFactory.xtend)   | [JpaStreamEvent](https://github.com/fuinorg/event-store-commons/blob/main/jpa/src/main/java/org/fuin/esc/jpa/JpaStreamEvent.java) | JPA entity that stores all events of an aggregate type. | - |
| [ESJpaEventIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaEventIdArtifactFactory.xtend)   | - | JPA key for an event that is based on the aggregate identifier and version number. | - |
| [ESJpaLiquibaseXmlArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaLiquibaseXmlArtifactFactory.xtend)   | - | [Liquibase](http://www.liquibase.org/) XML databaseChangeLog that creates all necessary tables for the aggregates. | - |
| [ESJpaStreamArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaStreamArtifactFactory.xtend)   | [JpaStream](https://github.com/fuinorg/event-store-commons/blob/main/jpa/src/main/java/org/fuin/esc/jpa/JpaStream.java) | JPA entity for storing the aggregate stream itself (not the events). | - |
| [ESRepositoryArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESRepositoryArtifactFactory.xtend)   | [EventStoreRepository](https://github.com/fuinorg/ddd-4-java/blob/main/esc/src/main/java/org/fuin/ddd4j/esc/EventStoreRepository.java) | Event store based repository for a single aggregate type. | [AggregateCRepository](src/test/expected-java/tst2/x/aggregates/AggregateCRepository.java) |
| [ESRepositoryFactoryArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESRepositoryFactoryArtifactFactory.xtend)   | - | Creates a CDI based producer that creates the above event store repository. | [AggregateCRepositoryFactory](src/test/expected-java/tst2/x/aggregates/AggregateCRepositoryFactory.java) |

The `ESJpa*` factories generate a *relational* event store. They are the alternative to an external event
store reached through `org.fuin.esc.api.EventStore`, and are normally not configured together with the
`ESRepository*` pair. For an explanation of the generated event store commons JPA types, see the
[documentation](https://github.com/fuinorg/event-store-commons/tree/main/jpa).

See [source code](src/main/java/org/fuin/dsl/ddd/gen/aggregate).


## Aggregate ID Factories
Factories generating code based on the 'aggregate-id' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [CombinedAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/CombinedAggregateIdArtifactFactory.xtend) | - | **Preferred entry point.** Delegates to `SimpleAggregateId` for a "base UUID" id, otherwise to `AbstractAggregateId` **and** `FinalAggregateId`. Do not configure it together with its delegates, or the id is generated twice. | [MyAggregate5Id](src/test/expected-java/tst2/x/aggregateid/MyAggregate5Id.java) |
| [AbstractAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AbstractAggregateIdArtifactFactory.xtend)   | [AggregateRootId](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/AggregateRootId.java) | Abstract aggregate root identifier Java class. | [AbstractMyAggregateId](src/test/expected-java/tst/x/aggregateid/AbstractMyAggregateId.java) |
| [FinalAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/FinalAggregateIdArtifactFactory.xtend)   | - | Final aggregate root identifier Java source extending the above abstract class. | [MyAggregateId](src/test/expected-java/tst/x/aggregateid/MyAggregateId.java) |
| [AggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdArtifactFactory.xtend)   | [AggregateRootId](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/AggregateRootId.java) | Aggregate root identifier Java class (Not splitted into 'abstract' and 'final' parts). | [MyAggregateId](src/test/expected-java/tst2/x/aggregateid/MyAggregateId.java) |
| [AggregateIdStreamFactoryArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdStreamFactoryArtifactFactory.xtend)   | IdStreamFactory | ![Warning](doc/warning.gif) ~~OUTDATED~~ - The referenced `IdStreamFactory` no longer exists in ddd-4-java. Needs to be reworked. | - |
| [SimpleAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/SimpleAggregateIdArtifactFactory.xtend)   | [AggregateRootUuid](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/AggregateRootUuid.java) | UUID based aggregate root identifier Java class. | [MyAggregate5Id](src/test/expected-java/tst2/x/aggregateid/MyAggregate5Id.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/aggregateid).


## Command Factories
Factories generating code based on the 'command' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [CommandArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/command/CommandArtifactFactory.xtend) | - | Command message Java class, normally routed to a `command.api` module so clients can use it without the domain. | - |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/command).


## Constraint Factories
Factories generating code based on the 'constraint' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [ValidatorAnnotationArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/constr/ValidatorAnnotationArtifactFactory.xtend) | Jakarta Bean Validation | Constraint annotation Java class. | [ConstraintA](src/test/expected-java/tst2/x/constr/ConstraintA.java) |
| [ValidatorArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/constr/ValidatorArtifactFactory.xtend) | `jakarta.validation.ConstraintValidator` | `ConstraintValidator` implementation for the above annotation. | [ConstraintAValidator](src/test/expected-java/tst2/x/constr/ConstraintAValidator.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/constr).


## Entity Factories
Factories generating code based on the 'entity' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [AbstractEntityArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entity/AbstractEntityArtifactFactory.xtend) | [AbstractEntity](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/AbstractEntity.java) | Abstract child entity Java class. | [AbstractEntityA](src/test/expected-java/tst/x/entities/AbstractEntityA.java) |
| [FinalEntityArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entity/FinalEntityArtifactFactory.xtend) | - | Final child entity Java source extending the above abstract class. | [EntityA](src/test/expected-java/tst/x/entities/EntityA.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/entity).


## Entity ID Factories
Factories generating code based on the 'entity-id' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [CombinedEntityIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entityid/CombinedEntityIdArtifactFactory.xtend) | - | **Preferred entry point.** Delegates to `SimpleEntityId` for a "base Integer" id, otherwise to `AbstractEntityId` **and** `FinalEntityId`. Do not configure it together with its delegates. | [MyEntity5Id](src/test/expected-java/tst2/x/entityid/MyEntity5Id.java) |
| [AbstractEntityIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entityid/AbstractEntityIdArtifactFactory.xtend) | [EntityId](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/EntityId.java) | Abstract entity identifier Java class. | [AbstractMyEntityId](src/test/expected-java/tst/x/entityid/AbstractMyEntityId.java) |
| [FinalEntityIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entityid/FinalEntityIdArtifactFactory.xtend) | - | Final entity identifier Java source extending the above abstract class. | [MyEntityId](src/test/expected-java/tst/x/entityid/MyEntityId.java) |
| [EntityIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entityid/EntityIdArtifactFactory.xtend) | [EntityId](https://github.com/fuinorg/ddd-4-java/blob/main/core/src/main/java/org/fuin/ddd4j/core/EntityId.java) | Entity identifier Java class (not splitted into 'abstract' and 'final' parts). | [MyEntityId](src/test/expected-java/tst2/x/entityid/MyEntityId.java) |
| [SimpleEntityIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/entityid/SimpleEntityIdArtifactFactory.xtend) | - | Integer based entity identifier Java class. | [MyEntity5Id](src/test/expected-java/tst2/x/entityid/MyEntity5Id.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/entityid).


## Enum Object Factories
Factories generating code based on the 'enum' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [EnumArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/enumobject/EnumArtifactFactory.xtend) | - | Enum Java class (not splitted into 'abstract' and 'final' parts). | [EnumA](src/test/expected-java/tst/x/enumobject/EnumA.java) |
| [AbstractEnumArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/enumobject/AbstractEnumArtifactFactory.xtend) | - | Abstract enum Java class. | [AbstractEnumB](src/test/expected-java/tst/x/enumobject/AbstractEnumB.java) |
| [FinalEnumArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/enumobject/FinalEnumArtifactFactory.xtend) | - | Final enum Java source extending the above abstract class. | [EnumB](src/test/expected-java/tst/x/enumobject/EnumB.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/enumobject).


## Event Factories
Factories generating code based on the 'event' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [EventArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/event/EventArtifactFactory.xtend) | [AbstractDomainEvent](https://github.com/fuinorg/ddd-4-java/blob/main/jsonb/src/main/java/org/fuin/ddd4j/jsonb/AbstractDomainEvent.java) (the `jsonb` / `jackson` / `jaxb` variant is chosen by the generator options) | Domain event Java class with its `SerializedDataType` constant. | [EventA](src/test/expected-java/tst2/x/ev/EventA.java) |
| [EventTestArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/event/EventTestArtifactFactory.xtend) | - | JUnit test class for the above domain event (serialization round-trip). | [EventATest](src/test/expected-java/tst2/x/ev/EventATest.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/event).


## Exception Factories
Factories generating code based on the 'exception' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [ExceptionArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/except/ExceptionArtifactFactory.xtend) | - | Exception Java class, optionally carrying a CID and model-declared variables. | [ExceptionA](src/test/expected-java/tst2/x/except/ExceptionA.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/except).


## Process Manager Factories
Factories generating code based on the 'process-manager' model element. The `runtime` option selects the
framework annotations of the concrete class.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [AbstractProcessManagerArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/processmanager/AbstractProcessManagerArtifactFactory.xtend) | [ProcessManagerView](https://github.com/fuinorg/cqrs-4-java/blob/develop/core/src/main/java/org/fuin/cqrs4j/core/ProcessManagerView.java) | Regenerated base: the abstract process-manager view plus the state enum from `process-states`. Runtime-neutral - it uses the `CommandOutbox` SPI. | [OrderPaymentProcessManager](src/test/expected-java/tst/x/processmanager/spring) |
| [ProcessManagerArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/processmanager/ProcessManagerArtifactFactory.xtend) | - | Write-once concrete class with one reaction-method stub per input event, annotated for the selected runtime. | [spring](src/test/expected-java/tst/x/processmanager/spring) / [quarkus](src/test/expected-java/tst/x/processmanager/quarkus) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/processmanager).


## Resource Set Factories
Factories that run once per parsed resource set rather than per model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [CtxExternalTypes](src/main/java/org/fuin/dsl/ddd/gen/resourceset/CtxExternalTypes.xtend) | - | Registers external types (Byte, String, Date, UUID, …) so the model can reference them. Creates **no** source code. | - |
| [PackageInfoArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/resourceset/PackageInfoArtifactFactory.xtend) | [@NullMarked](https://jspecify.dev/) | One `package-info.java` per generated package, annotated with JSpecify's `@NullMarked`. | [package-info](src/test/expected-java/tst2/x/resourceset/package-info.java) |
| [SpringBeansArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/resourceset/SpringBeansArtifactFactory.xtend) | - | One Spring `@Configuration` per side that registers every generated bean explicitly, so no application has to component-scan the generated packages. | - |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/resourceset).


## Service Factories
Factories generating code based on the 'service' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [ServiceArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/service/ServiceArtifactFactory.xtend) | - | Service *interface* Java source (the implementation is hand-written). | [ServiceA](src/test/expected-java/tst2/x/services/ServiceA.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/service).


## Value Object Factories
Factories generating code based on the 'value-object' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [CombinedValueObjectArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/valueobject/CombinedValueObjectArtifactFactory.xtend) | - | **Preferred entry point.** Delegates to `SimpleStringValueObject` when possible, otherwise to `AbstractValueObject` **and** `FinalValueObject`. Do not configure it together with its delegates, or those artifacts are generated twice. | [MySimpleStringValueObject](src/test/expected-java/tst2/x/valueobject/MySimpleStringValueObject.java) |
| [AbstractValueObjectArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/valueobject/AbstractValueObjectArtifactFactory.xtend) | [ValueObject](https://github.com/fuinorg/objects4j/blob/main/common/src/main/java/org/fuin/objects4j/common/ValueObject.java) | Abstract value object Java class. | [AbstractMyValueObject](src/test/expected-java/tst/x/valueobject/AbstractMyValueObject.java) |
| [FinalValueObjectArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/valueobject/FinalValueObjectArtifactFactory.xtend) | - | Final value object Java source extending the above abstract class. | [MyValueObject](src/test/expected-java/tst/x/valueobject/MyValueObject.java) |
| [SimpleStringValueObjectArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/valueobject/SimpleStringValueObjectArtifactFactory.xtend) | [AbstractStringValueObject](https://github.com/fuinorg/objects4j/blob/main/core/src/main/java/org/fuin/objects4j/core/AbstractStringValueObject.java) | Self-contained String based value object Java class. | [MySimpleStringValueObject](src/test/expected-java/tst2/x/valueobject/MySimpleStringValueObject.java) |
| [SimpleStringValueObjectTestArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/valueobject/SimpleStringValueObjectTestArtifactFactory.xtend) | - | JUnit test class for the above value object. | [MySimpleStringValueObjectTest](src/test/expected-java/tst2/x/valueobject/MySimpleStringValueObjectTest.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/valueobject).


## View Factories
Factories generating code based on the 'view' model element - the query side of a CQRS application.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [ViewArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/view/ViewArtifactFactory.xtend) | [View](https://github.com/fuinorg/cqrs-4-java/blob/main/core/src/main/java/org/fuin/cqrs4j/core/View.java) | Fully generated `<Base>View` that dispatches the projection's events to the read model. Annotated for the selected `runtime`. | [PersonListView](src/test/expected-java/tst/x/view/spring/PersonListView.java) |
| [ViewSpringApiArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/view/ViewSpringApiArtifactFactory.xtend) | `@HttpExchange` (Spring 6.1+) | REST contract interface `<Base>ControllerApi`. | [PersonListControllerApi](src/test/expected-java/tst/x/view/api/PersonListControllerApi.java) |
| [ViewQuarkusApiArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/view/ViewQuarkusApiArtifactFactory.xtend) | JAX-RS + [MicroProfile REST Client](https://github.com/eclipse/microprofile-rest-client) | REST contract interface `<Base>ResourceApi`, carrying `@RegisterRestClient(configKey = …)`. | [PersonListResourceApi](src/test/expected-java/tst/x/view/api/PersonListResourceApi.java) |
| [FinalViewArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/view/FinalViewArtifactFactory.xtend) | [EventHandler](https://github.com/fuinorg/cqrs-4-java/blob/develop/core/src/main/java/org/fuin/cqrs4j/core/EventHandler.java) | Write-once stubs: one event handler per projection input event, plus the `<Base>Controller` (Spring) / `<Base>Resource` (Quarkus) implementing the contract of the selected runtime. | [PersonListController](src/test/expected-java/tst/x/view/spring/PersonListController.java) / [PersonListResource](src/test/expected-java/tst/x/view/quarkus/PersonListResource.java) |
| [JpaViewTableArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/view/JpaViewTableArtifactFactory.xtend) | - | JPA `@Entity` classes for the read-model tables declared as "JpaHint" inside the view body. Rendering lives in [AbstractJpaTableArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/base/AbstractJpaTableArtifactFactory.xtend). | [Customer](src/test/expected-java/tst/x/jpa/Customer.java) |

### Both REST contracts are always generated

`ViewSpringApiArtifactFactory` and `ViewQuarkusApiArtifactFactory` ignore the `runtime` option: each view
gets **both** interfaces, normally routed to a separate api module (`query.api` in the default preset)
while the view and its implementation stay in `query.core`. Only the class generated by
`FinalViewArtifactFactory` picks a side and implements the contract of the configured runtime.

The module holding the interfaces should declare the three annotation artifacts they need as
**optional**, so nothing propagates to consumers:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.eclipse.microprofile.rest.client</groupId>
    <artifactId>microprofile-rest-client-api</artifactId>
    <optional>true</optional>
</dependency>
```

A consumer then picks the interface that matches its stack and adds only that dependency - a Quarkus
consumer gets both JAX-RS and MicroProfile from `quarkus-rest-client`, a Spring consumer adds
`spring-web`, and a consumer that uses neither interface (for example a desktop client calling the
endpoints with a plain HTTP client) carries nothing at all. Shipping both interfaces in one jar is safe:
classes are loaded lazily and annotations whose type is missing are ignored at runtime.

See [source code](src/main/java/org/fuin/dsl/ddd/gen/view).

- - - - - - - - -

## Using snapshots

Snapshot artifacts are published to the
[Central Portal Snapshots repository](https://central.sonatype.com/repository/maven-snapshots/).
To consume them, add the repository to your `~/.m2/settings.xml` (or project `pom.xml`):

```xml
<repository>
    <id>central.sonatype.snapshots</id>
    <name>Central Portal Snapshots</name>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
