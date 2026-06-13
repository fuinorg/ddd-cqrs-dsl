package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.Language;

/**
 * The CQRS DSL language. Files with the {@code .cqrs} extension are associated with it.
 */
public final class CqrsLanguage extends Language {

    public static final CqrsLanguage INSTANCE = new CqrsLanguage();

    private CqrsLanguage() {
        super("CqrsDsl");
    }

    @Override
    public String getDisplayName() {
        return "CQRS DSL";
    }
}
