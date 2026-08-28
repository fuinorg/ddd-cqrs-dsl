/*
 * Preset package mapping, used when a model declares no "model2JavaPackage" of its own.
 *
 * Reproduces the layout the deleted "package" pattern produced:
 *     ${context}.${mvnModule}[.${group}].${module}
 * with the module/group that the deleted "types" table assigned to each artifact kind.
 *
 * An unknown key throws: there is no declarative mapping left to fall back to, and placing a type in a
 * silently wrong package is worse than failing the build.
 */

function model2JavaPackage(element, typeKey) {

    var ctx = contextName(element);
    var mod = moduleName(element);

    switch (typeKey) {

        case 'java-command':
            return join(ctx, 'command.api', mod);

        case 'java-aggregate':
        case 'java-aggregate-abstract':
        case 'java-aggregate-repository':
        case 'java-aggregate-repository-factory':
        case 'java-aggregate-jpa-event':
        case 'java-aggregate-jpa-event-id':
        case 'java-aggregate-jpa-stream':
        case 'java-entity':
        case 'java-entity-abstract':
        // The documentation and the Liquibase changelog have no Java package, but their path is built
        // from one, so they follow the aggregate they describe.
        case 'res-aggregate-doc':
        case 'res-aggregate-liquibase':
        case 'java-service':
        case 'java-business-rule':
        case 'java-business-rules':
            return join(ctx, 'command.core.domain', mod);

        case 'java-view':
        case 'java-view-rest-impl':
        case 'java-view-jpa-table':
        case 'java-view-service-impl':
        case 'java-view-event-handler':
            return join(ctx, 'query.core.view', mod);

        case 'java-view-rest-api-spring':
        case 'java-view-rest-api-quarkus':
        case 'java-view-service':
        case 'java-view-service-rest-client':
            return join(ctx, 'query.api.view', mod);

        case 'java-process-manager':
        case 'java-process-manager-abstract':
            return join(ctx, 'process.core', mod);

        case 'java-value-object':
        case 'java-value-object-abstract':
        case 'java-value-object-test':
        case 'java-aggregate-id':
        case 'java-aggregate-id-abstract':
        case 'java-aggregate-id-stream-factory':
        case 'java-entity-id':
        case 'java-entity-id-abstract':
        case 'java-entity-id-path':
        case 'java-enum':
        case 'java-enum-abstract':
        case 'java-event':
        case 'java-event-test':
        case 'java-exception':
        case 'java-constraint':
        case 'java-constraint-validator':
        case 'java-package-info':
        case 'java-spring-config':
            return join(ctx, 'shared.domain', mod);

        default:
            throw new Error('Unknown typeKey: ' + typeKey);
    }
}

/** Name of the context the element belongs to. */
function contextName(element) {
    var ctx = enclosing(element, 'Context');
    return ctx === null ? '' : String(ctx.getName());
}

/** Name of the module the element belongs to. */
function moduleName(element) {
    var mod = enclosing(element, 'Module');
    return mod === null ? '' : String(mod.getName());
}

/** Closest container of the element whose meta class has the given name, or null. */
function enclosing(element, className) {
    var current = element;
    while (current !== null) {
        // String(...) matters: a Java string wrapped by the engine is never === a JS string.
        if (String(current.eClass().getName()) === className) {
            return current;
        }
        current = current.eContainer();
    }
    return null;
}

/** Joins the non-empty segments with a dot. */
function join() {
    var parts = [];
    for (var i = 0; i < arguments.length; i++) {
        var part = arguments[i];
        if (part !== null && part !== undefined && part !== '') {
            parts.push(String(part));
        }
    }
    return parts.join('.');
}
