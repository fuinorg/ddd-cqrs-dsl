package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

/** Small PSI helpers that are not tied to a specific generated element. */
public final class CqrsPsiUtil {

    private CqrsPsiUtil() {
    }

    /** The imported namespace of an import declaration, e.g. {@code com.acme.billing.*}. */
    public static @Nullable String getImportedNamespace(CqrsImportDecl element) {
        CqrsImportFqn fqn = PsiTreeUtil.getChildOfType(element, CqrsImportFqn.class);
        // Strip whitespace and the caret keyword-escape from each segment.
        return fqn != null ? CqrsNames.unescapeQualified(fqn.getText().replaceAll("\\s+", "")) : null;
    }
}
