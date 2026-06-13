package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.jetbrains.annotations.Nullable;

/** Matches {@code { }}, {@code ( )} and {@code < >} pairs. */
public final class CqrsBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = {
            new BracePair(CqrsTypes.LBRACE, CqrsTypes.RBRACE, true),
            new BracePair(CqrsTypes.LPAREN, CqrsTypes.RPAREN, false),
            new BracePair(CqrsTypes.LT, CqrsTypes.GT, false),
    };

    @Override
    public BracePair[] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(IElementType lbraceType,
                                                   @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}
