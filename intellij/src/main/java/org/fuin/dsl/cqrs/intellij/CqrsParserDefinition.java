package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.fuin.dsl.cqrs.intellij.parser.CqrsParser;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.jetbrains.annotations.NotNull;

/** Wires the lexer, parser and PSI factory for the CQRS DSL into the platform. */
public final class CqrsParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE =
            new IFileElementType(CqrsLanguage.INSTANCE);

    private static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);

    public @NotNull TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new CqrsLexerAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new CqrsParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return CqrsTokenSets.COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return CqrsTokenSets.STRINGS;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return CqrsTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new CqrsFile(viewProvider);
    }
}
