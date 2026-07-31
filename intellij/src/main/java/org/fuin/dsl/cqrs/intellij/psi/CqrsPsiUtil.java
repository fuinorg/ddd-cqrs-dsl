package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.PsiElement;
import org.fuin.dsl.cqrs.intellij.CqrsValidationUtil;
import org.jetbrains.annotations.Nullable;

/** Small PSI helpers that are not tied to a specific generated element. */
public final class CqrsPsiUtil {

    private CqrsPsiUtil() {
    }

    /**
     * The Maven coordinate of a dependency declaration, e.g. {@code org.acme:acme-model:1.0.0}.
     * A {@code dependency_decl} holds two bare {@code STRING}s, so Grammar-Kit generates no named
     * accessor and the tokens have to be reached by their preceding keyword.
     */
    public static @Nullable String getDependencyCoordinate(CqrsDependencyDecl element) {
        return unquote(CqrsValidationUtil.firstTokenAfter(element, CqrsTypes.KW_DEPENDENCY, CqrsTypes.STRING));
    }

    /** The {@code local} directory override of a dependency declaration, or {@code null}. */
    public static @Nullable String getDependencyLocal(CqrsDependencyDecl element) {
        return unquote(CqrsValidationUtil.firstTokenAfter(element, CqrsTypes.KW_LOCAL, CqrsTypes.STRING));
    }

    /** Strips the surrounding double quotes of a STRING token. */
    private static @Nullable String unquote(@Nullable PsiElement token) {
        if (token == null) {
            return null;
        }
        String text = token.getText();
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
