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

    /** An <code>entity-id-path</code>: <code>«Name»</code>. */
    public static final String JAVA_ENTITY_ID_PATH = "java-entity-id-path";

    /** An <code>enum</code>: <code>«Name»</code>. */
    public static final String JAVA_ENUM = "java-enum";

    /** An <code>event</code>: <code>«Name»</code>. */
    public static final String JAVA_EVENT = "java-event";

    /** An <code>exception</code>: <code>«Name»</code>. */
    public static final String JAVA_EXCEPTION = "java-exception";

    /** Class carrying an exception's data to a client, so a refusal can say what it was about. */
    public static final String JAVA_EXCEPTION_DATA = "java-exception-data";

    /** A <code>command</code>: <code>«Name»</code>. */
    public static final String JAVA_COMMAND = "java-command";

    /** A <code>constraint</code>: the <code>«Name»</code> annotation. */
    public static final String JAVA_CONSTRAINT = "java-constraint";

    /** A <code>service</code>: <code>«Name»</code>. */
    public static final String JAVA_SERVICE = "java-service";

    /** A <code>business-rule</code>: <code>«Name»</code>. */
    public static final String JAVA_BUSINESS_RULE = "java-business-rule";

    /** Everything one aggregate or entity declares, verified: <code>«Name»Rules</code>. */
    public static final String JAVA_BUSINESS_RULES = "java-business-rules";

    /** An <code>aggregate</code>: <code>«Name»</code>. */
    public static final String JAVA_AGGREGATE = "java-aggregate";

    /** An <code>entity</code>: <code>«Name»</code>. */
    public static final String JAVA_ENTITY = "java-entity";

    /** A <code>view</code>: <code>«Base»View</code>. */
    public static final String JAVA_VIEW = "java-view";

    /** Framework-free service contract of a <code>view</code>: <code>«Base»Service</code>. */
    public static final String JAVA_VIEW_SERVICE = "java-view-service";

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

    /**
     * Class implementing a view's REST contract: <code>«Base»Controller</code> /
     * <code>«Base»Resource</code>. It delegates to {@link #JAVA_VIEW_SERVICE} and holds no logic of its
     * own, which is why it is regenerated rather than written once.
     */
    public static final String JAVA_VIEW_REST_IMPL = "java-view-rest-impl";

    /** Hand-written implementation of a view's service: <code>«Base»ServiceImpl</code>. */
    public static final String JAVA_VIEW_SERVICE_IMPL = "java-view-service-impl";

    /**
     * Adapter satisfying a view's service over its Spring REST contract:
     * <code>«Base»ServiceRestClient</code>.
     */
    public static final String JAVA_VIEW_SERVICE_REST_CLIENT = "java-view-service-rest-client";

    /** Projection event handler of a view: <code>«Event»Handler</code>. */
    public static final String JAVA_VIEW_EVENT_HANDLER = "java-view-event-handler";

    /** JPA entity of a <code>JpaHint</code> table of a view. */
    public static final String JAVA_VIEW_JPA_TABLE = "java-view-jpa-table";

    /** A <code>package-info</code>. */
    public static final String JAVA_PACKAGE_INFO = "java-package-info";

    /** The generated Spring <code>@Configuration</code>. */
    public static final String JAVA_SPRING_CONFIG = "java-spring-config";

    /** The permission catalogue as Java constants: <code>PermissionIds</code>. */
    public static final String JAVA_PERMISSION_IDS = "java-permission-ids";

    /** The UI catalogue as Java constants: <code>UiCatalogue</code>. */
    public static final String JAVA_UI_CATALOGUE = "java-ui-catalogue";

    // ---- Dart -------------------------------------------------------------------------------------
    //
    // The Flutter client's contract. A second target for the same model rather than a variant of the
    // Java one: what it emits is Dart, and the type key is what tells a mapping script where an
    // artifact belongs, so the two must not collide.

    /** An <code>enum</code> as a Dart enum: <code>«name».dart</code>. */
    public static final String DART_ENUM = "dart-enum";

    /** An <code>aggregate-id</code> or <code>entity-id</code> as a Dart class carrying its type. */
    public static final String DART_ENTITY_ID = "dart-entity-id";

    /** A single-value <code>value-object</code> as a Dart class carrying its invariants. */
    /** An <code>entity-id-path</code> on the client: <code>«Name»</code>. */
    public static final String DART_ENTITY_ID_PATH = "dart-entity-id-path";

    public static final String DART_VALUE_OBJECT = "dart-value-object";

    /** A multi-attribute <code>value-object</code> - a read-model row, with its descriptor. */
    public static final String DART_VALUE_OBJECT_ROW = "dart-value-object-row";

    /** A <code>command</code> as a Dart class, with its descriptor. */
    public static final String DART_COMMAND = "dart-command";

    /** A typed client for one <code>view</code>. */
    public static final String DART_VIEW_CLIENT = "dart-view-client";

    /** The const descriptor of one <code>view</code> - what a generic renderer draws it from. */
    public static final String DART_VIEW_DESCRIPTOR = "dart-view-descriptor";

    /** The whole model's modules, views, methods and commands, as one const value. */
    public static final String DART_MODULE_CATALOGUE = "dart-module-catalogue";

    /** The one file a consumer imports: everything the package offers, re-exported. */
    public static final String DART_LIBRARY = "dart-library";

    /** Types the model uses but does not own, written into the client's own package. */
    public static final String DART_SHARED_TYPES = "dart-shared-types";

    /** Every permission id in the model, as Dart constants. */
    public static final String DART_PERMISSION_IDS = "dart-permission-ids";

    /** The model's wording as an ARB file, keyed the way the annotations are. */
    public static final String RES_DART_ARB = "res-dart-arb";

    // ---- Non-Java artifacts --------------------------------------------------------------------

    /** Aggregate documentation: <code>«Name».html</code>. */
    public static final String RES_AGGREGATE_DOC = "res-aggregate-doc";

    /** The permission catalogue as documentation: <code>PERMISSIONS.md</code>. */
    public static final String RES_PERMISSION_CATALOGUE = "res-permission-catalogue";

    /** The module dependency graph: <code>MODULES.json</code>. */
    public static final String RES_MODULE_DEPENDENCIES = "res-module-dependencies";

    /** The model's wording as JVM resource bundles: one <code>«Bundle».properties</code> per bundle. */
    public static final String RES_WORDING_PROPERTIES = "res-wording-properties";

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
        case "EntityIdPathType":
            return JAVA_ENTITY_ID_PATH;
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
        case "BusinessRule":
            return JAVA_BUSINESS_RULE;
        // A key derives a uniqueness rule, which is the same kind of artifact under the same key - so
        // whatever constructs a rule reaches a derived one without knowing which it got.
        case "Key":
            return JAVA_BUSINESS_RULE;
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
