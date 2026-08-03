/*
 * Example of the script a "SrcGen4J" hint points at with "artifact2Target".
 *
 * It is asked for every file that is about to be written and answers with { module, folder } - the
 * Maven module of the multi-module build the file goes into, and the folder inside it. Which folder
 * decides who owns the file: a "gen*" folder is rewritten on every run, a plain one is written once
 * and then belongs to the developer.
 *
 * Unlike "model2JavaPackage", this applies to this project's own output only - a dependency's types
 * were generated in the producing project and are not written again here.
 *
 * The preset shipped with ddd-templates is the real thing to look at; this one keeps a single module
 * and only sorts the artifacts into the four folders.
 */

var MAIN_JAVA = 'mainJava';
var GEN_MAIN_JAVA = 'genMainJava';
var GEN_MAIN_RES = 'genMainRes';
var TEST_JAVA = 'testJava';

function artifact2Target(element, typeKey, artifactFactory) {
    return { module: 'shared', folder: folder(String(artifactFactory), String(typeKey)) };
}

function folder(artifactFactory, typeKey) {

    // A test class - the developer edits it, so it is written once.
    if (endsWith(artifactFactory, 'TestArtifactFactory')) {
        return TEST_JAVA;
    }
    // Not Java at all: documentation, Liquibase XML and the like.
    if (typeKey.indexOf('res-') === 0) {
        return GEN_MAIN_RES;
    }
    // A leaf class meant to be filled in by hand, recognisable by its "Final" prefix.
    if (artifactFactory.indexOf('Final') === 0) {
        return MAIN_JAVA;
    }
    // Everything else is derived from the model and regenerated every run.
    return GEN_MAIN_JAVA;
}

function endsWith(text, suffix) {
    return text.length >= suffix.length && text.lastIndexOf(suffix) === text.length - suffix.length;
}
