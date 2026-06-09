# ddd-templates
Xtend based domain-driven design (DDD/CQRS) code generation templates for use with SrcGen4J.

[![Maven Central](https://img.shields.io/maven-central/v/org.fuin.dsl.ddd/ddd-templates.svg)](https://central.sonatype.com/artifact/org.fuin.dsl.ddd/ddd-templates)

## What is this?
The project provides several artifact factories that can be used to generate code using SrcGen4J: 
[srcgen4j](https://github.com/fuinorg/srcgen4j/) from an Xtext based [DDD DSL](../eclipse) model.   

An [artifact factory](https://github.com/fuinorg/srcgen4j/blob/main/commons/src/main/java/org/fuin/srcgen4j/commons/ArtifactFactory.java) is a piece of code that creates an artifact for a given model element.
The result is Java code based on utility classes defined in the [ddd-4-java](https://github.com/fuinorg/ddd-4-java) project.

## Aggregate Factories
Factories generating code based on the 'aggregate' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [AbstractAggregateArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/AbstractAggregateArtifactFactory.xtend) | [AbstractAggregateRoot](https://github.com/fuinorg/ddd-4-java/blob/master/src/main/java/org/fuin/ddd4j/ddd/AbstractAggregateRoot.java) | Abstract aggregate root Java class. | [AbstractAggregateA](src/test/expected-java/tst/x/aggregates/AbstractAggregateA.java) |
| [FinalAggregateArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/FinalAggregateArtifactFactory.xtend)   | - | Final aggregate root Java source extending the above abstract class. | [AggregateA](src/test/expected-java/tst/x/aggregates/AggregateA.java) |
| [AggregateDocArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/AggregateDocArtifactFactory.xtend)   | - | HTML file with a description of the aggregate. | - |
| [ESJpaEventArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaEventArtifactFactory.xtend)   | [JpaStreamEvent](https://github.com/fuinorg/event-store-commons/blob/master/jpa/src/main/java/org/fuin/esc/jpa/JpaStreamEvent.java) | JPA entity that stores all events of an aggregate type. | - |
| [ESJpaEventIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaEventIdArtifactFactory.xtend)   | - | JPA key for an event that is based on the aggregate identifier and version number. | - |
| [ESJpaLiquibaseXmlArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaLiquibaseXmlArtifactFactory.xtend)   | - | [Liquibase](http://www.liquibase.org/) XML databaseChangeLog that creates all necessary tables for the aggregates. | - |
| [ESJpaStreamArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESJpaStreamArtifactFactory.xtend)   | [JpaStream](https://github.com/fuinorg/event-store-commons/blob/master/jpa/src/main/java/org/fuin/esc/jpa/JpaStream.java) | JPA entity for storing the aggregate stream itself (not the events). | - |
| [ESRepositoryArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESRepositoryArtifactFactory.xtend)   | [EventStoreRepository](https://github.com/fuinorg/ddd-4-java/blob/master/src/main/java/org/fuin/ddd4j/esrepo/EventStoreRepository.java) | Event store based repository for a single aggregate type. | [AggregateCRepository](src/test/expected-java/tst2/x/aggregates/AggregateCRepository.java) |
| [ESRepositoryFactoryArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregate/ESRepositoryFactoryArtifactFactory.xtend)   | - | Creates a CDI based producer that creates the above event store repository. | [AggregateCRepositoryFactory](src/test/expected-java/tst2/x/aggregates/AggregateCRepositoryFactory.java) |

For an explanation of the generated event store commons JPA types, see the [documentation](https://github.com/fuinorg/event-store-commons/tree/master/jpa).

See [source code](src/main/java/org/fuin/dsl/ddd/gen/aggregate).


## Aggregate ID Factories
Factories generating code based on the 'aggregate-id' model element.

| Factory Name | Reference | Description | Example |
| :----------- | :-------- | :---------- | :------ |
| [AbstractAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AbstractAggregateIdArtifactFactory.xtend)   | [AggregateRootId](https://github.com/fuinorg/ddd-4-java/blob/master/src/main/java/org/fuin/ddd4j/ddd/AggregateRootId.java) | Abstract aggregate root identifier Java class. | [AbstractMyAggregateId](src/test/expected-java/tst/x/aggregateid/AbstractMyAggregateId.java) |
| [FinalAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/FinalAggregateIdArtifactFactory.xtend)   | - | Final aggregate root identifier Java source extending the above abstract class. | [MyAggregateId](src/test/expected-java/tst/x/aggregateid/MyAggregateId.java) |
| [AggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdArtifactFactory.xtend)   | [AggregateRootId](https://github.com/fuinorg/ddd-4-java/blob/master/src/main/java/org/fuin/ddd4j/ddd/AggregateRootId.java) | Aggregate root identifier Java class (Not splitted into 'abstract' and 'final' parts). | [MyAggregateId](src/test/expected-java/tst2/x/aggregateid/MyAggregateId.java) |
| [AggregateIdStreamFactoryArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/AggregateIdStreamFactoryArtifactFactory.xtend)   | IdStreamFactory | ![Warning](doc/warning.gif) ~~OUTDATED~~ - Needs to be reworked. | - |
| [SimpleAggregateIdArtifactFactory](src/main/java/org/fuin/dsl/ddd/gen/aggregateid/SimpleAggregateIdArtifactFactory.xtend)   | [AggregateRootUuid](https://github.com/fuinorg/ddd-4-java/blob/master/src/main/java/org/fuin/ddd4j/ddd/AggregateRootUuid.java) | UUID based aggregate root identifier Java class. | [MyAggregate5Id](src/test/expected-java/tst2/x/aggregateid/MyAggregate5Id.java) |

See [source code](src/main/java/org/fuin/dsl/ddd/gen/aggregateid).


## Constraint Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/constr).

## Entity Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/entity).

## Entity ID Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/entityid).

## Enum Object Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/enumobject).

## Event Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/event).

## Exception Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/except).

## Resource Set Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/resourceset).

## Service Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/service).

## Value Object Factories
![Work in progress](doc/work-in-progress.png)
See [source code](src/main/java/org/fuin/dsl/ddd/gen/valueobject).

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
