package org.fuin.dsl.ddd.gen.script;

/**
 * Where a generated artifact is written: the Maven module and the folder inside it - exactly the pair
 * SrcGen4J needs to place a file.
 */
public final class Target {

    private final String module;

    private final String folder;

    /**
     * Constructor with all data.
     *
     * @param module Name of the target Maven module, e.g. "shared" or "command.api".
     * @param folder Name of the target folder inside that module, e.g. "mainJava" or "genMainJava".
     */
    public Target(final String module, final String folder) {
        this.module = module;
        this.folder = folder;
    }

    /** @return Name of the target Maven module. */
    public String getModule() {
        return module;
    }

    /** @return Name of the target folder inside the module. */
    public String getFolder() {
        return folder;
    }

    @Override
    public String toString() {
        return module + "/" + folder;
    }

}
