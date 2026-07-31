package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTokenTypes;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;

/**
 * Closes a quote as soon as it is opened, and tells the platform where a string literal begins and
 * ends.
 *
 * <p>Without this, a string was unterminated for as long as it was being typed. The lexer needs both
 * quotes to produce a {@link CqrsTypes#STRING}, so the opening quote stayed a bad character and its
 * content lexed as ordinary identifiers and dots - which made a dot inside a Maven coordinate look
 * like the start of a qualified name and popped up code completion.</p>
 */
public final class CqrsQuoteHandler extends SimpleTokenSetQuoteHandler {

    public CqrsQuoteHandler() {
        // The unclosed one has to be in the set as well: right after the opening quote is typed there
        // is no closed string yet, and that is exactly the moment the closing quote must be inserted.
        super(CqrsTypes.STRING, CqrsTokenTypes.UNCLOSED_STRING);
    }
}
