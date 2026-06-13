package org.fuin.dsl.cqrs.intellij;

import com.intellij.lexer.FlexAdapter;
import org.fuin.dsl.cqrs.intellij.lexer._CqrsDslLexer;

/** Adapts the JFlex-generated {@link _CqrsDslLexer} to the IntelliJ {@code Lexer} API. */
public final class CqrsLexerAdapter extends FlexAdapter {

    public CqrsLexerAdapter() {
        super(new _CqrsDslLexer());
    }
}
