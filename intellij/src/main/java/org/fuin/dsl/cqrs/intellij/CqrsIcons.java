package org.fuin.dsl.cqrs.intellij;

import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

/** Icons used by the CQRS DSL plugin. */
public final class CqrsIcons {

    /** Icon for {@code .cqrs} files. */
    public static final Icon FILE = IconLoader.getIcon("/icons/cqrs.svg", CqrsIcons.class);

    private CqrsIcons() {
    }
}
