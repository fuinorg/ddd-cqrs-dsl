package org.fuin.dsl.ddd.gen.base

/**
 * Test utilities.
 */
class TestExtensions {
    
    /** Folder with expected example classes that are splitted into an abstract and a concrete part. */
    public static final String EXAMPLES_ABSTRACT = "tst";

    /** Folder with expected example classes that are not splitted. */
    public static final String EXAMPLES_CONCRETE = "tst2";
    
    /**
     * Returns the content of an example file in the EXAMPLES_ABSTRACT package.
     * 
     * @return File content as text.
     */
    def static loadAbstractExample(String filename) {
        Utils.readAsString("src/test/expected-java/" + EXAMPLES_ABSTRACT + "/" + filename)
    }

    /**
     * Returns the content of an example file in the EXAMPLES_CONCRETE package.
     * 
     * @return File content as text.
     */
    def static loadConcreteExample(String filename) {
        Utils.readAsString("src/test/expected-java/" + EXAMPLES_CONCRETE + "/" + filename)
    }

    /**
     * Returns the content of an expected Dart file.
     *
     * <p>Under its own root rather than beside the Java ones, and - unlike them - <b>not</b> compiled
     * by the test sources. What keeps these honest instead is that they are the files the Flutter
     * client was written against and runs on: they were hand-written first, against a real backend,
     * and copied here once they worked. See melkheftken's
     * <code>frontend/flutter/packages/contract</code>.
     *
     * @param filename Path below the expected-dart root.
     *
     * @return File content as text.
     */
    def static loadDartExample(String filename) {
        Utils.readAsString("src/test/expected-dart/" + filename)
    }
    
}