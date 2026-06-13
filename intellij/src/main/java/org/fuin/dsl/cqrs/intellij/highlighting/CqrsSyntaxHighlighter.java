package org.fuin.dsl.cqrs.intellij.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.fuin.dsl.cqrs.intellij.CqrsLexerAdapter;
import org.fuin.dsl.cqrs.intellij.CqrsTokenSets;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/** Maps lexer tokens to highlighting attributes. */
public final class CqrsSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("CQRS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("CQRS_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("CQRS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey LINE_COMMENT =
            createTextAttributesKey("CQRS_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            createTextAttributesKey("CQRS_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey DOC_COMMENT =
            createTextAttributesKey("CQRS_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("CQRS_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey BRACES =
            createTextAttributesKey("CQRS_BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey PARENTHESES =
            createTextAttributesKey("CQRS_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey OPERATOR =
            createTextAttributesKey("CQRS_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("CQRS_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] KEYWORD_KEYS = {KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = {STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = {NUMBER};
    private static final TextAttributesKey[] LINE_COMMENT_KEYS = {LINE_COMMENT};
    private static final TextAttributesKey[] BLOCK_COMMENT_KEYS = {BLOCK_COMMENT};
    private static final TextAttributesKey[] DOC_COMMENT_KEYS = {DOC_COMMENT};
    private static final TextAttributesKey[] IDENTIFIER_KEYS = {IDENTIFIER};
    private static final TextAttributesKey[] BRACES_KEYS = {BRACES};
    private static final TextAttributesKey[] PARENTHESES_KEYS = {PARENTHESES};
    private static final TextAttributesKey[] OPERATOR_KEYS = {OPERATOR};
    private static final TextAttributesKey[] BAD_CHARACTER_KEYS = {BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new CqrsLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (CqrsTokenSets.KEYWORDS.contains(tokenType)) {
            return KEYWORD_KEYS;
        }
        if (tokenType.equals(CqrsTypes.STRING)) {
            return STRING_KEYS;
        }
        if (tokenType.equals(CqrsTypes.NUMBER)) {
            return NUMBER_KEYS;
        }
        if (tokenType.equals(CqrsTypes.LINE_COMMENT)) {
            return LINE_COMMENT_KEYS;
        }
        if (tokenType.equals(CqrsTypes.BLOCK_COMMENT)) {
            return BLOCK_COMMENT_KEYS;
        }
        if (tokenType.equals(CqrsTypes.DOC_COMMENT)) {
            return DOC_COMMENT_KEYS;
        }
        if (tokenType.equals(CqrsTypes.ID)) {
            return IDENTIFIER_KEYS;
        }
        if (CqrsTokenSets.BRACES.contains(tokenType)) {
            return BRACES_KEYS;
        }
        if (CqrsTokenSets.PARENS.contains(tokenType)) {
            return PARENTHESES_KEYS;
        }
        if (CqrsTokenSets.OPERATORS.contains(tokenType)) {
            return OPERATOR_KEYS;
        }
        if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHARACTER_KEYS;
        }
        return EMPTY_KEYS;
    }
}
