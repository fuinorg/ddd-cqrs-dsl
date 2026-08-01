package org.fuin.dsl.ddd.gen.base;

/**
 * Stable keys that name the <em>kind</em> of artifact generated for a model element.
 * <p>
 * A key is passed to the model's <code>model2JavaPackage</code> and <code>artifact2Target</code>
 * scripts, and it is the artifact-kind half of a {@link org.fuin.srcgen4j.core.emf.CodeReferenceRegistry}
 * key (see {@link #refKey(String, String)}). One vocabulary therefore answers all three questions:
 * which package a type lands in, where its file is written, and how a template refers to it.
 * <p>
 * The keys are <b>public API</b>: a script published inside a model jar switches on them, so a rename
 * breaks every consumer that has already shipped. They are named after the domain role of the generated
 * type, never after the factory class that happens to produce it, and the vocabulary is additive only.
 * <p>
 * Naming: lowercase kebab-case, <code>java-</code> for a Java type and <code>res-</code> for a non-Java
 * resource. <code>java-&lt;element&gt;</code> without a suffix is the concrete, referenceable type - the
 * one a consumer of a published model names; a suffix marks a supporting artifact of that element.
 */
public final class TypeKeys {

    // ---- Referenceable types -------------------------------------------------------------------

    /** A <code>value-object</code>: <code>«Name»</code>. */
    public static final String JAVA_VALUE_OBJECT = "java-value-object";

    /** An <code>aggregate-id</code>: <code>«Name»</code>. */
    public static final String JAVA_AGGREGATE_ID = "java-aggregate-id";

    /** An <code>entity-id</code>: <code>«Name»</code>. */
    public static final String JAVA_ENTITY_ID = "java-entity-id";

    /** An <code>enum</code>: <code>«Name»</code>. */
    public static final String JAVA_ENUM = "java-enum";

    /** An <code>event</code>: <code>«Name»</code>. */
    public static final String JAVA_EVENT = "java-event";

    /** An <code>exception</code>: <code>«Name»</code>. */
    public static final String JAVA_EXCEPTION = "java-exception";

    /** A <code>command</code>: <code>«Name»</code>. */
    public static final String JAVA_COMMAND = "java-command";

    /** A <code>constraint</code>: the <code>«Name»</code> annotation. */
    public static final String JAVA_CONSTRAINT = "java-constraint";

    /** A <code>service</code>: <code>«Name»</code>. */
    public static final String JAVA_SERVICE = "java-service";

    /** An <code>aggregate</code>: <code>«Name»</code>. */
    public static final String JAVA_AGGREGATE = "java-aggregate";

    /** An <code>entity</code>: <code>«Name»</code>. */
    public static final String JAVA_ENTITY = "java-entity";

    /** A <code>view</code>: <code>«Base»View</code>. */
    public static final String JAVA_VIEW = "java-view";

    /** A <code>process-manager</code>: <code>«Base»ProcessManager</code>. */
    public static final String JAVA_PROCESS_MANAGER = "java-process-manager";

    // ---- Supporting types ----------------------------------------------------------------------

    /** Abstract base of a value object: <code>Abstract«Name»</code>. */
    public static final String JAVA_VALUE_OBJECT_ABSTRACT = "java-value-object-abstract";

    /** Abstract base of an aggregate id: <code>Abstract«Name»</code>. */
    public static final String JAVA_AGGREGATE_ID_ABSTRACT = "java-aggregate-id-abstract";

    /** Abstract base of an entity id: <code>Abstract«Name»</code>. */
    public static final String JAVA_ENTITY_ID_ABSTRACT = "java-entity-id-abstract";

    /** Abstract base of an enum: <code>Abstract«Name»</code>. */
    public static final String JAVA_ENUM_ABSTRACT = "java-enum-abstract";

    /** Abstract base of an aggregate: <code>Abstract«Name»</code>. */
    public static final String JAVA_AGGREGATE_ABSTRACT = "java-aggregate-abstract";

    /** Abstract base of an entity: <code>Abstract«Name»</code>. */
    public static final String JAVA_ENTITY_ABSTRACT = "java-entity-abstract";

    /** Abstract base of a process manager: <code>Abstract«Base»ProcessManager</code>. */
    public static final String JAVA_PROCESS_MANAGER_ABSTRACT = "java-process-manager-abstract";

    /** Test class of a value object: <code>«Name»Test</code>. */
    public static final String JAVA_VALUE_OBJECT_TEST = "java-value-object-test";

    /** Test class of an event: <code>«Name»Test</code>. */
    public static final String JAVA_EVENT_TEST = "java-event-test";

    /** Bean-validation validator of a constraint: <code>«Name»Validator</code>. */
    public static final String JAVA_CONSTRAINT_VALIDATOR = "java-constraint-validator";

    /** Event-store stream factory of an aggregate id: <code>«Name»StreamFactory</code>. */
    public static final String JAVA_AGGREGATE_ID_STREAM_FACTORY = "java-aggregate-id-stream-factory";

    /** Event-store repository of an aggregate: <code>«Name»Repository</code>. */
    public static final String JAVA_AGGREGATE_REPOSITORY = "java-aggregate-repository";

    /** Factory for the event-store repository: <code>«Name»RepositoryFactory</code>. */
    public static final String JAVA_AGGREGATE_REPOSITORY_FACTORY = "java-aggregate-repository-factory";

    /** JPA event entity of an aggregate: <code>«Name»Event</code>. */
    public static final String JAVA_AGGREGATE_JPA_EVENT = "java-aggregate-jpa-event";

    /** JPA event id of an aggregate: <code>«Name»EventId</code>. */
    public static final String JAVA_AGGREGATE_JPA_EVENT_ID = "java-aggregate-jpa-event-id";

    /** JPA stream entity of an aggregate: <code>«Name»Stream</code>. */
    public static final String JAVA_AGGREGATE_JPA_STREAM = "java-aggregate-jpa-stream";

    /** Spring REST contract of a view: <code>«Base»ControllerApi</code>. */
    public static final String JAVA_VIEW_REST_API_SPRING = "java-view-rest-api-spring";

    /** Quarkus REST contract of a view: <code>«Base»ResourceApi</code>. */
    public static final String JAVA_VIEW_REST_API_QUARKUS = "java-view-rest-api-quarkus";

    /** Class implementing a view's REST contract: <code>«Base»Controller</code> / <code>«Base»Resource</code>. */
    public static final String JAVA_VIEW_REST_IMPL = "java-view-rest-impl";

    /** JPA entity of a <code>JpaHint</code> table of a view. */
    public static final String JAVA_VIEW_JPA_TABLE = "java-view-jpa-table";

    /** A <code>package-info</code>. */
    public static final String JAVA_PACKAGE_INFO = "java-package-info";

    /** The generated Spring <code>@Configuration</code>. */
    public static final String JAVA_SPRING_CONFIG = "java-spring-config";

    // ---- Non-Java artifacts --------------------------------------------------------------------

    /** Aggregate documentation: <code>«Name».html</code>. */
    public static final String RES_AGGREGATE_DOC = "res-aggregate-doc";

    /** Liquibase changelog of the JPA event store of an aggregate. */
    public static final String RES_AGGREGATE_LIQUIBASE = "res-aggregate-liquibase";

    /** Separates the element identity from the type key in a code reference key. */
    private static final String SEPARATOR = "#";

    /**
     * Element every key was built for. Both sides of a reference build their key through
     * {@link #refKey(org.fuin.dsl.cqrs.cqrsDsl.AbstractElement, String)}, so a key that is looked up has
     * always been built at least once and the element behind it is known.
     */
    private static final java.util.Map<String, org.fuin.dsl.cqrs.cqrsDsl.AbstractElement> ELEMENTS =
            new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * The key of the type a model element is primarily generated as - the concrete, referenceable one.
     * Used where an element has to be mapped to a package or a target without a factory at hand, for
     * example to collect the packages a module generates into.
     *
     * @param element Model element - may be <code>null</code>.
     *
     * @return Type key, or <code>null</code> for an element that generates no type of its own.
     */
    public static String primaryTypeKey(final org.eclipse.emf.ecore.EObject element) {
        if (element == null) {
            return null;
        }
        switch (element.eClass().getName()) {
        case "ValueObject":
            return JAVA_VALUE_OBJECT;
        case "AggregateId":
            return JAVA_AGGREGATE_ID;
        case "EntityId":
            return JAVA_ENTITY_ID;
        case "EnumObject":
            return JAVA_ENUM;
        case "Event":
            return JAVA_EVENT;
        case "Exception":
            return JAVA_EXCEPTION;
        case "Command":
            return JAVA_COMMAND;
        case "Constraint":
            return JAVA_CONSTRAINT;
        case "Service":
            return JAVA_SERVICE;
        case "Aggregate":
            return JAVA_AGGREGATE;
        case "Entity":
            return JAVA_ENTITY;
        case "View":
            return JAVA_VIEW;
        case "ProcessManager":
            return JAVA_PROCESS_MANAGER;
        default:
            // ExternalType, Projection, CommandHandler, Annotation, DataProtection, ... - nothing of
            // their own is generated, so they have no package to ask for.
            return null;
        }
    }

    private TypeKeys() {
        throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class");
    }

    /**
     * Key a generated type is registered under in the code reference registry: the unique name of the
     * model element it is generated for, plus the kind of artifact it is.
     * <p>
     * Both halves are needed: a single element produces several types (an aggregate produces its class,
     * its abstract base, its repository, ...) and each of them has its own fully qualified name.
     *
     * @param uniqueName Unique name of the model element - see
     *            {@code CqrsAbstractElementExtensions.uniqueName}. Cannot be <code>null</code>.
     * @param typeKey One of the keys of this class. Cannot be <code>null</code>.
     *
     * @return Registry key, never <code>null</code>.
     */
    /**
     * Key the type an element is primarily generated as is registered under.
     * <p>
     * An element that generates no type of its own - an <code>type</code> declaration standing for a
     * class that exists outside the model, above all - keeps its plain unique name as the key, which is
     * what {@code CtxExternalTypes} registers it under.
     *
     * @param element Model element - cannot be <code>null</code>.
     *
     * @return Registry key, never <code>null</code>.
     */
    public static String refKey(final org.fuin.dsl.cqrs.cqrsDsl.AbstractElement element) {
        return refKey(element, primaryTypeKey(element));
    }

    /**
     * Key a given kind of type generated for an element is registered under.
     * <p>
     * The element is remembered for the key, so {@link ComputingCodeReferenceRegistry} can work the
     * fully qualified name out when no factory registered one - both sides of a reference build their
     * key here, so the element is always known by the time it is looked up.
     *
     * @param element Model element - cannot be <code>null</code>.
     * @param typeKey One of the keys of this class, or <code>null</code> for an element that generates
     *            no type of its own (its plain unique name is then the key).
     *
     * @return Registry key, never <code>null</code>.
     */
    public static String refKey(final org.fuin.dsl.cqrs.cqrsDsl.AbstractElement element, final String typeKey) {
        if (element == null) {
            throw new IllegalArgumentException("Argument 'element' cannot be null");
        }
        final String uniqueName = org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.uniqueName(element);
        if (typeKey == null) {
            return uniqueName;
        }
        final String key = refKey(uniqueName, typeKey);
        ELEMENTS.put(key, element);
        return key;
    }

    /** Element a key was built for, or <code>null</code> when the key names no generated type. */
    static org.fuin.dsl.cqrs.cqrsDsl.AbstractElement elementOf(final String key) {
        return ELEMENTS.get(key);
    }

    /** Artifact kind half of a key, or <code>null</code> when it carries none. */
    static String typeKeyOf(final String key) {
        final int idx = key == null ? -1 : key.lastIndexOf(SEPARATOR);
        return idx < 0 ? null : key.substring(idx + SEPARATOR.length());
    }

    public static String refKey(final String uniqueName, final String typeKey) {
        if (uniqueName == null) {
            throw new IllegalArgumentException("Argument 'uniqueName' cannot be null");
        }
        if (typeKey == null) {
            throw new IllegalArgumentException("Argument 'typeKey' cannot be null");
        }
        return uniqueName + SEPARATOR + typeKey;
    }

}
