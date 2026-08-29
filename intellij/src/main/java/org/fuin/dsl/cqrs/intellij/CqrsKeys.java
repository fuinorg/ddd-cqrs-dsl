package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRuleInstance;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsKeyDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * What a business key stands for, read off the PSI.
 *
 * <p>The port of {@code CqrsKeyExtensions} on the Xtext side, kept to the same answers so the editor
 * refuses the shapes the build refuses. The names are the ones the generator emits: a key called
 * {@code Iban} derives {@code IbanMustBeUnique} over a {@code boolean ibanTaken}, answered by
 * {@code existsIban}.</p>
 */
public final class CqrsKeys {

    private CqrsKeys() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** The name of the uniqueness rule the key derives. */
    public static @NotNull String ruleName(@NotNull CqrsKeyDef key) {
        String name = key.getName();
        if (name == null || name.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1) + "MustBeUnique";
    }

    /** Whether a collision refuses the operation, which is the only case that derives a rule. */
    public static boolean refuses(@NotNull CqrsKeyDef key) {
        return key.getCollisionStrategy() != null && "refuse".equals(key.getCollisionStrategy().getText().trim());
    }

    /**
     * The names the key is made of: the identifiers between {@code attributes} and
     * {@code on-collision}. They are bare tokens rather than nodes of their own, so they are read off
     * the token stream.
     */
    public static @NotNull List<String> attributeNames(@NotNull CqrsKeyDef key) {
        List<String> out = new ArrayList<>();
        boolean collecting = false;
        for (PsiElement child = key.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode() == null) {
                continue;
            }
            if (child.getNode().getElementType() == CqrsTypes.KW_ATTRIBUTES) {
                collecting = true;
            } else if (child.getNode().getElementType() == CqrsTypes.KW_ON_COLLISION) {
                break;
            } else if (collecting && child.getNode().getElementType() == CqrsTypes.ID) {
                out.add(child.getText());
            }
        }
        return out;
    }

    /** The attribute of the declaring type a key attribute names, or {@code null} where there is none. */
    public static @Nullable CqrsAttribute declaredAttribute(@NotNull CqrsKeyDef key, @NotNull String name) {
        for (CqrsAttribute attribute : PsiTreeUtil.getChildrenOfTypeAsList(key.getParent(), CqrsAttribute.class)) {
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        return null;
    }

    /** The operation carrying a rule or key usage. */
    public static @Nullable PsiElement operationOf(@NotNull CqrsBusinessRuleInstance instance) {
        PsiElement constructor = PsiTreeUtil.getParentOfType(instance, CqrsConstructorDef.class);
        if (constructor != null) {
            return constructor;
        }
        return PsiTreeUtil.getParentOfType(instance, CqrsMethodDef.class);
    }

    /** Whether the operation creates, which is what leaves it no prior state to read. */
    public static boolean creates(@NotNull PsiElement operation) {
        return operation instanceof CqrsConstructorDef;
    }

    /** The parameters of an operation. */
    public static @NotNull List<CqrsParameter> parametersOf(@NotNull PsiElement operation) {
        if (operation instanceof CqrsConstructorDef) {
            return ((CqrsConstructorDef) operation).getParameterList();
        }
        if (operation instanceof CqrsMethodDef) {
            return ((CqrsMethodDef) operation).getParameterList();
        }
        return List.of();
    }

    /**
     * Whether the operation declares an {@code operation-context} of its own.
     *
     * <p>Of its own on purpose: the method that answers the key is derived onto that service, and a
     * service declared at module level is shared by operations that check nothing.</p>
     */
    public static boolean hasOwnOperationContext(@NotNull PsiElement operation) {
        CqrsTypeRef context = contextRef(operation);
        if (context == null) {
            return false;
        }
        for (CqrsServiceDef service : servicesOf(operation)) {
            if (CqrsValidationUtil.resolve(context) == service) {
                return true;
            }
        }
        return false;
    }

    private static @Nullable CqrsTypeRef contextRef(@NotNull PsiElement operation) {
        if (operation instanceof CqrsConstructorDef && ((CqrsConstructorDef) operation).getOperationContext() != null) {
            return ((CqrsConstructorDef) operation).getOperationContext().getTypeRef();
        }
        if (operation instanceof CqrsMethodDef && ((CqrsMethodDef) operation).getOperationContext() != null) {
            return ((CqrsMethodDef) operation).getOperationContext().getTypeRef();
        }
        return null;
    }

    private static @NotNull List<CqrsServiceDef> servicesOf(@NotNull PsiElement operation) {
        if (operation instanceof CqrsConstructorDef) {
            return ((CqrsConstructorDef) operation).getServiceDefList();
        }
        if (operation instanceof CqrsMethodDef) {
            return ((CqrsMethodDef) operation).getServiceDefList();
        }
        return List.of();
    }

    /**
     * The key attributes an operation cannot be paired up with one to one.
     *
     * <p>Two parameters of the attribute's type leave nothing to choose between them, and two key
     * attributes sharing a type would both be read off the same parameter - checking half of a
     * composite key against the wrong value, which still compiles and still passes.</p>
     */
    public static @NotNull List<String> ambiguousAttributes(@NotNull CqrsKeyDef key, @NotNull PsiElement operation) {
        List<String> out = new ArrayList<>();
        List<String> names = attributeNames(key);
        for (String name : names) {
            CqrsAttribute attribute = declaredAttribute(key, name);
            if (attribute == null) {
                continue;
            }
            int parameters = 0;
            for (CqrsParameter parameter : parametersOf(operation)) {
                if (CqrsValidationUtil.sameType(parameter.getTypeRef(), attribute.getTypeRef())) {
                    parameters++;
                }
            }
            int siblings = 0;
            for (String other : names) {
                CqrsAttribute sibling = declaredAttribute(key, other);
                if (sibling != null && CqrsValidationUtil.sameType(sibling.getTypeRef(), attribute.getTypeRef())) {
                    siblings++;
                }
            }
            if (parameters > 1 || (siblings > 1 && parameters > 0)) {
                out.add(name);
            }
        }
        return out;
    }

    /** Whether a key attribute binds to a parameter of the operation. */
    public static boolean bindsToAParameter(@NotNull CqrsKeyDef key, @NotNull String name,
            @NotNull PsiElement operation) {
        CqrsAttribute attribute = declaredAttribute(key, name);
        if (attribute == null) {
            return true; // an unresolved name is reported as a linking error, not here
        }
        for (CqrsParameter parameter : parametersOf(operation)) {
            if (CqrsValidationUtil.sameType(parameter.getTypeRef(), attribute.getTypeRef())) {
                return true;
            }
        }
        return false;
    }

}
