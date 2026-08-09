/*
 * Preset target mapping, used when a model declares no "artifact2Target" of its own.
 *
 * Reproduces what the deleted "types" table assigned to each artifact: the Maven module from the type
 * entry, the folder from its artifact entry.
 *
 *   module: from the type key alone
 *   folder: four rules on the factory, first match wins
 *     1. "*TestArtifactFactory"          -> testJava     (a test class)
 *     2. the two non-Java factories      -> genMainRes   (documentation, Liquibase XML)
 *     3. "Final*" and the leaf factories -> mainJava     (developer owned, written once)
 *     4. everything else                 -> genMainJava  (derived, rewritten every run)
 */

var MAIN_JAVA = 'mainJava';
var GEN_MAIN_JAVA = 'genMainJava';
var GEN_MAIN_RES = 'genMainRes';
var TEST_JAVA = 'testJava';

var TEST_SUFFIX = 'TestArtifactFactory';
var FINAL_PREFIX = 'Final';

/** Factories that create a non-Java artifact. */
var MAIN_RESOURCE_ARTIFACTS = [
    'AggregateDocArtifactFactory',
    'ESJpaLiquibaseXmlArtifactFactory'
];

/** Leaf classes owned by the developer that are not named "Final*". */
var MAIN_JAVA_ARTIFACTS = [
    'AggregateIdArtifactFactory',
    'EntityIdArtifactFactory',
    'ValidatorArtifactFactory',
    'ESRepositoryArtifactFactory',
    'ProcessManagerArtifactFactory'
];

function artifact2Target(element, typeKey, artifactFactory) {
    return { module: module(typeKey), folder: folder(artifactFactory) };
}

/** Maven module an artifact of the given kind is generated into. */
function module(typeKey) {
    switch (typeKey) {

        case 'java-command':
            return 'command.api';

        case 'java-aggregate':
        case 'java-aggregate-abstract':
        case 'java-aggregate-repository':
        case 'java-aggregate-repository-factory':
        case 'java-aggregate-jpa-event':
        case 'java-aggregate-jpa-event-id':
        case 'java-aggregate-jpa-stream':
        case 'java-entity':
        case 'java-entity-abstract':
        case 'java-service':
        case 'res-aggregate-doc':
        case 'res-aggregate-liquibase':
            return 'command.core';

        case 'java-view':
        case 'java-view-rest-impl':
        case 'java-view-jpa-table':
        case 'java-view-service-impl':
        case 'java-view-event-handler':
            return 'query.core';

        case 'java-view-rest-api-spring':
        case 'java-view-rest-api-quarkus':
        case 'java-view-service':
        case 'java-view-service-rest-client':
            return 'query.api';

        case 'java-process-manager':
        case 'java-process-manager-abstract':
            return 'process.core';

        case 'java-value-object':
        case 'java-value-object-abstract':
        case 'java-value-object-test':
        case 'java-aggregate-id':
        case 'java-aggregate-id-abstract':
        case 'java-aggregate-id-stream-factory':
        case 'java-entity-id':
        case 'java-entity-id-abstract':
        case 'java-enum':
        case 'java-enum-abstract':
        case 'java-event':
        case 'java-event-test':
        case 'java-exception':
        case 'java-constraint':
        case 'java-constraint-validator':
        case 'java-package-info':
        case 'java-spring-config':
            return 'shared';

        default:
            throw new Error('Unknown typeKey: ' + typeKey);
    }
}

/** Simple or fully qualified factory class name -> one of the four folders. */
function folder(artifactFactory) {
    if (artifactFactory === null || artifactFactory === undefined) {
        throw new Error("Argument 'artifactFactory' cannot be null");
    }
    var name = simpleName(artifactFactory);
    if (name.endsWith(TEST_SUFFIX)) {
        return TEST_JAVA;
    }
    if (MAIN_RESOURCE_ARTIFACTS.indexOf(name) >= 0) {
        return GEN_MAIN_RES;
    }
    if (name.startsWith(FINAL_PREFIX) || MAIN_JAVA_ARTIFACTS.indexOf(name) >= 0) {
        return MAIN_JAVA;
    }
    return GEN_MAIN_JAVA;
}

function simpleName(className) {
    var idx = className.lastIndexOf('.');
    return idx < 0 ? className : className.substring(idx + 1);
}
