package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.CqrsFileType;
import org.jetbrains.annotations.Nullable;

/** Creates throw-away PSI fragments used for rename / reference rewriting. */
public final class CqrsElementFactory {

    private CqrsElementFactory() {
    }

    public static CqrsFile createFile(Project project, String text) {
        return (CqrsFile) PsiFileFactory.getInstance(project)
                .createFileFromText("_dummy_.cqrs", CqrsFileType.INSTANCE, text);
    }

    /** A bare identifier leaf, extracted from a synthetic {@code type} declaration. */
    public static @Nullable PsiElement createIdentifier(Project project, String name) {
        CqrsFile file = createFile(project, "context c{namespace n{type " + name + "}}");
        CqrsExternalType type = PsiTreeUtil.findChildOfType(file, CqrsExternalType.class);
        return type != null ? type.getNameIdentifier() : null;
    }

    /** A {@code qualified_name} node for renaming a context or namespace. */
    public static @Nullable PsiElement createQualifiedName(Project project, String name) {
        CqrsFile file = createFile(project, "context " + name + "{}");
        CqrsContextDef ctx = PsiTreeUtil.findChildOfType(file, CqrsContextDef.class);
        return ctx != null ? PsiTreeUtil.getChildOfType(ctx, CqrsQualifiedName.class) : null;
    }

    /** A {@code type_ref} node carrying the given (possibly qualified) name. */
    public static @Nullable CqrsTypeRef createTypeRef(Project project, String name) {
        CqrsFile file = createFile(project, "context c{namespace n{value-object v{" + name + " x}}}");
        return PsiTreeUtil.findChildOfType(file, CqrsTypeRef.class);
    }
}
