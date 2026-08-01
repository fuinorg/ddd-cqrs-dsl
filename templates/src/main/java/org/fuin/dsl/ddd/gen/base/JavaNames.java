package org.fuin.dsl.ddd.gen.base;

/**
 * The Java simple name a generated type gets, per artifact kind. It is the naming half of what
 * {@code TypeKeys} identifies: the key says <em>which</em> type, this says what it is called.
 * <p>
 * Together with {@code CqrsScripts.model2JavaPackage} it makes a generated type's fully qualified name
 * <em>computable</em> from the model alone, which is what lets a reference resolve even when the factory
 * that would have registered it did not run.
 */
public final class JavaNames {

    private JavaNames() {
        throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class");
    }

    /**
     * Java simple name of the given kind of artifact for an element of the given name.
     *
     * @param elementName Name of the model element, e.g. "CategoryId".
     * @param typeKey One of the keys of {@link TypeKeys}.
     *
     * @return Simple class name, or <code>null</code> when the kind has no class of its own.
     */
    public static String simpleName(final String elementName, final String typeKey) {
        if (elementName == null || typeKey == null) {
            return null;
        }
        switch (typeKey) {
        case TypeKeys.JAVA_VALUE_OBJECT:
        case TypeKeys.JAVA_AGGREGATE_ID:
        case TypeKeys.JAVA_ENTITY_ID:
        case TypeKeys.JAVA_ENUM:
        case TypeKeys.JAVA_EVENT:
        case TypeKeys.JAVA_EXCEPTION:
        case TypeKeys.JAVA_COMMAND:
        case TypeKeys.JAVA_CONSTRAINT:
        case TypeKeys.JAVA_SERVICE:
        case TypeKeys.JAVA_AGGREGATE:
        case TypeKeys.JAVA_ENTITY:
        case TypeKeys.JAVA_VIEW:
        case TypeKeys.JAVA_PROCESS_MANAGER:
            return elementName;

        case TypeKeys.JAVA_VALUE_OBJECT_ABSTRACT:
        case TypeKeys.JAVA_AGGREGATE_ID_ABSTRACT:
        case TypeKeys.JAVA_ENTITY_ID_ABSTRACT:
        case TypeKeys.JAVA_ENUM_ABSTRACT:
        case TypeKeys.JAVA_AGGREGATE_ABSTRACT:
        case TypeKeys.JAVA_ENTITY_ABSTRACT:
            return "Abstract" + elementName;

        case TypeKeys.JAVA_PROCESS_MANAGER_ABSTRACT:
            return "Abstract" + ArtifactNames.processManagerBaseName(elementName) + "ProcessManager";

        case TypeKeys.JAVA_VALUE_OBJECT_TEST:
        case TypeKeys.JAVA_EVENT_TEST:
            return elementName + "Test";

        case TypeKeys.JAVA_CONSTRAINT_VALIDATOR:
            return elementName + "Validator";

        case TypeKeys.JAVA_AGGREGATE_ID_STREAM_FACTORY:
            return elementName + "StreamFactory";

        case TypeKeys.JAVA_AGGREGATE_REPOSITORY:
            return elementName + "Repository";

        case TypeKeys.JAVA_AGGREGATE_REPOSITORY_FACTORY:
            return elementName + "RepositoryFactory";

        case TypeKeys.JAVA_AGGREGATE_JPA_EVENT:
            return elementName + "Event";

        case TypeKeys.JAVA_AGGREGATE_JPA_EVENT_ID:
            return elementName + "EventId";

        case TypeKeys.JAVA_AGGREGATE_JPA_STREAM:
            return elementName + "Stream";

        case TypeKeys.JAVA_VIEW_REST_API_SPRING:
            return ArtifactNames.viewBaseName(elementName) + "ControllerApi";

        case TypeKeys.JAVA_VIEW_REST_API_QUARKUS:
            return ArtifactNames.viewBaseName(elementName) + "ResourceApi";

        default:
            // package-info, the Spring configuration, the JPA tables and the non-Java artifacts have no
            // name derivable from the element alone.
            return null;
        }
    }

}
