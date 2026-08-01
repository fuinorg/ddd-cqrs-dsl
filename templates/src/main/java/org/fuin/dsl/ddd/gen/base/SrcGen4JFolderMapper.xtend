package org.fuin.dsl.ddd.gen.base

/**
 * Maps an artifact factory class to the SrcGen4J target folder its output is written to. This is the
 * rule behind the "folder" values in "srcgen4j-default.json" expressed as a function:
 * <ol>
 * <li>"*TestArtifactFactory" writes a test class to {@link #TEST_JAVA}.</li>
 * <li>The factories in {@link #MAIN_RESOURCE_ARTIFACTS} do not emit Java, so they write to
 * {@link #GEN_MAIN_RES}.</li>
 * <li>"Final*" and the factories in {@link #MAIN_JAVA_ARTIFACTS} create the concrete leaf class a
 * developer owns and extends by hand. It is generated once into {@link #MAIN_JAVA} and never
 * overwritten.</li>
 * <li>Everything else ("Abstract*", "Combined*", "Simple*", ...) is pure derived code that is
 * rewritten on every run and goes to {@link #GEN_MAIN_JAVA}.</li>
 * </ol>
 * Rules 1-3 are checked in that order, the last one is the default.
 */
class SrcGen4JFolderMapper {

    /** Hand written main sources ("src/main/java") - Generated once, never overwritten. */
    public static val String MAIN_JAVA = "mainJava"

    /** Generated main sources ("src-gen/main/java") - Cleaned and rewritten on every run. */
    public static val String GEN_MAIN_JAVA = "genMainJava"

    /** Generated main resources ("src-gen/main/resources") - Cleaned and rewritten on every run. */
    public static val String GEN_MAIN_RES = "genMainRes"

    /** Hand written test sources ("src/test/java") - Generated once, never overwritten. */
    public static val String TEST_JAVA = "testJava"

    /** Suffix of all factories that create a test class. */
    public static val String TEST_SUFFIX = "TestArtifactFactory"

    /** Prefix of the factories that create the concrete leaf class of a base/leaf pair. */
    public static val String FINAL_PREFIX = "Final"

    /** Factories that create a non-Java artifact (documentation, Liquibase XML, ...). */
    public static val MAIN_RESOURCE_ARTIFACTS = #{
        "AggregateDocArtifactFactory",
        "ESJpaLiquibaseXmlArtifactFactory"
    }

    /** Leaf classes owned by the developer that are not named "Final*". */
    public static val MAIN_JAVA_ARTIFACTS = #{
        "AggregateIdArtifactFactory",
        "EntityIdArtifactFactory",
        "ValidatorArtifactFactory",
        "ESRepositoryArtifactFactory",
        "ProcessManagerArtifactFactory"
    }

    private new() {
        throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class")
    }

    /**
     * Determines the target folder for an artifact factory.
     *
     * @param artifactFactory Simple or fully qualified artifact factory class name - Cannot be
     *            <code>null</code>.
     *
     * @return One of {@link #TEST_JAVA}, {@link #GEN_MAIN_RES}, {@link #MAIN_JAVA} or
     *         {@link #GEN_MAIN_JAVA} - Never <code>null</code>.
     */
    def static String folder(String artifactFactory) {
        if (artifactFactory === null) {
            throw new IllegalArgumentException("Argument 'artifactFactory' cannot be null")
        }
        val name = artifactFactory.simpleName
        if (name.endsWith(TEST_SUFFIX)) {
            TEST_JAVA
        } else if (MAIN_RESOURCE_ARTIFACTS.contains(name)) {
            GEN_MAIN_RES
        } else if (name.startsWith(FINAL_PREFIX) || MAIN_JAVA_ARTIFACTS.contains(name)) {
            MAIN_JAVA
        } else {
            GEN_MAIN_JAVA
        }
    }

    /**
     * Determines if the folder returned by {@link #folder(String)} is generated code that is cleaned
     * and rewritten on every run.
     *
     * @param folder Folder name - Cannot be <code>null</code>.
     *
     * @return <code>true</code> if the folder is overwritten, <code>false</code> if it is only
     *         written once.
     */
    def static boolean generated(String folder) {
        folder == GEN_MAIN_JAVA || folder == GEN_MAIN_RES
    }

    def private static String simpleName(String className) {
        val idx = className.lastIndexOf('.')
        if (idx < 0) className else className.substring(idx + 1)
    }

}
