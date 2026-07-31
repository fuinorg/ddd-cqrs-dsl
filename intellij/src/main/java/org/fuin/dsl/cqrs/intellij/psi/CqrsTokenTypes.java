package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.tree.IElementType;

/**
 * Token types the lexer produces that the grammar itself does not use.
 *
 * <p>They are declared here rather than in <code>CqrsDsl.bnf</code> because the parser never accepts
 * them - they exist so the editor can tell a half-written construct from plain garbage.</p>
 */
public final class CqrsTokenTypes {

    /**
     * A string that was opened but not closed before the end of the line.
     *
     * <p>The lexer used to have no rule for it, so the opening quote became a bad character and its
     * content lexed as ordinary identifiers and dots - which made a dot inside a Maven coordinate
     * look like the start of a qualified name and popped up code completion. A string is unterminated
     * for as long as it is being typed, so this is the normal state, not an exotic one.</p>
     *
     * <p>A closed string still wins: the lexer takes the longest match, so a
     * {@code CqrsTypes#STRING} - even one spanning several lines - is always preferred over this.
     * The parser does not accept it, so a genuinely unterminated string stays a syntax error.</p>
     */
    public static final IElementType UNCLOSED_STRING = new CqrsTokenType("UNCLOSED_STRING");

    private CqrsTokenTypes() {
    }
}
