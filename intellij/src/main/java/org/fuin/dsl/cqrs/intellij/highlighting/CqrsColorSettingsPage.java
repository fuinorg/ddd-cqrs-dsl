package org.fuin.dsl.cqrs.intellij.highlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Map;

/** Lets users customize CQRS DSL colors under Settings | Editor | Color Scheme. */
public final class CqrsColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = {
            new AttributesDescriptor("Keyword", CqrsSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Identifier", CqrsSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("String", CqrsSyntaxHighlighter.STRING),
            new AttributesDescriptor("Number", CqrsSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Line comment", CqrsSyntaxHighlighter.LINE_COMMENT),
            new AttributesDescriptor("Block comment", CqrsSyntaxHighlighter.BLOCK_COMMENT),
            new AttributesDescriptor("Doc comment", CqrsSyntaxHighlighter.DOC_COMMENT),
            new AttributesDescriptor("Braces", CqrsSyntaxHighlighter.BRACES),
            new AttributesDescriptor("Parentheses", CqrsSyntaxHighlighter.PARENTHESES),
            new AttributesDescriptor("Operator", CqrsSyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("Bad character", CqrsSyntaxHighlighter.BAD_CHARACTER),
    };

    private static final String DEMO =
            "/** A customer aggregate. */\n"
            + "context com.acme {\n"
            + "\tmodule types {\n"
            + "\t\ttype String\n"
            + "\t\ttype UUID\n"
            + "\t}\n\n"
            + "\tmodule sales {\n"
            + "\t\timport com.acme.types.*\n\n"
            + "\t\t// A unique customer identifier\n"
            + "\t\taggregate-id CustomerId identifies Customer base UUID {\n"
            + "\t\t\texamples \"42705de0-91a1-11e4-b4a9\"\n"
            + "\t\t}\n\n"
            + "\t\taggregate Customer identifier CustomerId {\n"
            + "\t\t\tString name\n"
            + "\t\t\tconstructor create fires CustomerCreated {\n"
            + "\t\t\t\tString name\n"
            + "\t\t\t\tevent CustomerCreated {\n"
            + "\t\t\t\t\tString name\n"
            + "\t\t\t\t\tmessage \"Customer created: ${name}\"\n"
            + "\t\t\t\t}\n"
            + "\t\t\t}\n"
            + "\t\t}\n"
            + "\t}\n"
            + "}\n";

    @Override
    public @Nullable Icon getIcon() {
        return CqrsIcons.FILE;
    }

    @Override
    public @NotNull SyntaxHighlighter getHighlighter() {
        return new CqrsSyntaxHighlighter();
    }

    @Override
    public @NotNull String getDemoText() {
        return DEMO;
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @Override
    public @NotNull String getDisplayName() {
        return "CQRS DSL";
    }
}
