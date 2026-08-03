/*
 * Example of the script a "SrcGen4J" hint points at with "model2JavaPackage".
 *
 * It is asked for every generated type and answers with the Java package that type goes into. The
 * arguments are the model element itself and a "type key" naming the kind of artifact, so the same
 * element can be spread over several packages - a command into the API, the aggregate handling it
 * into the domain.
 *
 * This one is deliberately the simplest thing that works: everything of a module lands in
 * "<context>.<module>", split only into an API and a domain half. The preset shipped with
 * ddd-templates is the real thing to look at; it is what applies when a model declares no script.
 *
 * An unknown key throws rather than guessing: a type in a silently wrong package is worse than a
 * failed build.
 */

/** Type keys whose artifacts belong to the published API rather than the domain. */
var API_TYPE_KEYS = [
    'java-command',
    'java-event',
    'java-view-rest-api-spring',
    'java-view-rest-api-quarkus'
];

function model2JavaPackage(element, typeKey) {

    var pkg = join(contextName(element), moduleName(element));

    if (contains(API_TYPE_KEYS, typeKey)) {
        return join(pkg, 'api');
    }
    if (String(typeKey).indexOf('java-') === 0) {
        return join(pkg, 'domain');
    }
    throw 'No package defined for type key "' + typeKey + '"';
}

// ---- helpers ---------------------------------------------------------------------------------

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

function contains(array, value) {
    for (var i = 0; i < array.length; i++) {
        if (array[i] === String(value)) {
            return true;
        }
    }
    return false;
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
