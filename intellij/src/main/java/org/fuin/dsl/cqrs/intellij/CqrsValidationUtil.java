package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;
import org.fuin.dsl.cqrs.intellij.reference.CqrsResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpers shared by {@link CqrsValidationAnnotator}. They reproduce small pieces of behaviour that
 * the Eclipse/Xtext validator gets for free from the EMF model (type identity, positional access to
 * keyword-introduced cross references, message-variable scanning) on top of the Grammar-Kit PSI.
 */
final class CqrsValidationUtil {

    private CqrsValidationUtil() {
    }

    /**
     * Returns the type references that directly follow {@code keyword} inside {@code parent}, in
     * source order. Grammar-Kit collapses several same-typed references (e.g. {@code identifies} vs
     * {@code base}, or {@code input} vs {@code exception}) into one undifferentiated
     * {@code getTypeRefList()}, so the keyword token is the only thing that tells them apart. A run
     * continues across {@code ,} and {@code |} separators and ends at the next significant token.
     */
    static @NotNull List<CqrsTypeRef> typeRefsAfter(@NotNull PsiElement parent, @NotNull IElementType keyword) {
        List<CqrsTypeRef> result = new ArrayList<>();
        boolean active = false;
        for (PsiElement child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiWhiteSpace || child instanceof PsiComment) {
                continue;
            }
            IElementType type = child.getNode().getElementType();
            if (type == keyword) {
                active = true;
            } else if (child instanceof CqrsTypeRef) {
                if (active) {
                    result.add((CqrsTypeRef) child);
                }
            } else if (type != CqrsTypes.COMMA && type != CqrsTypes.PIPE) {
                // Any other significant token (next keyword, '{', ...) ends the run.
                active = false;
            }
        }
        return result;
    }

    /** The first type reference following {@code keyword} inside {@code parent}, or {@code null}. */
    static @Nullable CqrsTypeRef firstTypeRefAfter(@NotNull PsiElement parent, @NotNull IElementType keyword) {
        List<CqrsTypeRef> refs = typeRefsAfter(parent, keyword);
        return refs.isEmpty() ? null : refs.get(0);
    }

    /** First declaration {@code ref} resolves to, or {@code null} if it is unresolved. */
    static @Nullable CqrsNamedElement resolve(@Nullable CqrsTypeRef ref) {
        if (ref == null) {
            return null;
        }
        List<CqrsNamedElement> targets = CqrsResolveUtil.resolve(ref, ref.getReferencedName());
        return targets.isEmpty() ? null : targets.get(0);
    }

    /** Simple (last segment) name of a possibly qualified type reference. */
    static @NotNull String simpleName(@NotNull CqrsTypeRef ref) {
        String name = ref.getReferencedName();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(dot + 1);
    }

    /**
     * Whether two type references denote the same type. Resolved declarations are compared by
     * identity; when either side cannot be resolved (e.g. a not-yet-cached remote model) the simple
     * names are compared instead, so incomplete resolution never yields a false "mismatch".
     */
    static boolean sameType(@Nullable CqrsTypeRef a, @Nullable CqrsTypeRef b) {
        if (a == null || b == null) {
            return false;
        }
        CqrsNamedElement ra = resolve(a);
        CqrsNamedElement rb = resolve(b);
        if (ra != null && rb != null) {
            return ra == rb;
        }
        return simpleName(a).equals(simpleName(b));
    }

    /** Comma separated list of the (as-written) names of the given type references. */
    static @NotNull String typeNames(@NotNull List<CqrsTypeRef> refs) {
        StringBuilder sb = new StringBuilder();
        for (CqrsTypeRef ref : refs) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(ref.getReferencedName());
        }
        return sb.toString();
    }

    /** Unquoted content of a {@code STRING} token, or {@code null}. */
    static @Nullable String stringValue(@Nullable PsiElement stringToken) {
        if (stringToken == null) {
            return null;
        }
        String text = stringToken.getText();
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    /**
     * Returns the first {@code ${name}} placeholder in {@code msg} whose {@code name} is not in
     * {@code vars} (names starting with {@code #} are ignored), or {@code null} if all are known.
     * Ported verbatim from the Xtext validator's {@code findUnknownVar}.
     */
    static @Nullable String findUnknownVar(@NotNull List<String> vars, @Nullable String msg) {
        if (msg == null) {
            return null;
        }
        int from = 0;
        int start;
        while ((start = msg.indexOf("${", from)) > -1) {
            int end = msg.indexOf('}', start + 1);
            if (end == -1) {
                break;
            }
            String name = msg.substring(start + 2, end);
            if (!vars.contains(name) && !name.startsWith("#")) {
                return name;
            }
            from = end + 1;
        }
        return null;
    }

    /** Names of the given attributes (skips attributes without a name). */
    static @NotNull List<String> attributeNames(@NotNull List<CqrsAttribute> attributes) {
        List<String> names = new ArrayList<>(attributes.size());
        for (CqrsAttribute attribute : attributes) {
            String name = attribute.getName();
            if (name != null) {
                names.add(name);
            }
        }
        return names;
    }

    /** Parameter names of a constructor or method declaration; empty for anything else. */
    static @NotNull List<String> parameterNames(@NotNull CqrsNamedElement methodOrConstructor) {
        List<CqrsParameter> parameters;
        if (methodOrConstructor instanceof CqrsConstructorDef) {
            parameters = ((CqrsConstructorDef) methodOrConstructor).getParameterList();
        } else if (methodOrConstructor instanceof CqrsMethodDef) {
            parameters = ((CqrsMethodDef) methodOrConstructor).getParameterList();
        } else {
            return new ArrayList<>();
        }
        List<String> names = new ArrayList<>(parameters.size());
        for (CqrsParameter parameter : parameters) {
            String name = parameter.getName();
            if (name != null) {
                names.add(name);
            }
        }
        return names;
    }

    /** Name identifier of a declaration if present, otherwise the declaration itself (for ranges). */
    static @NotNull PsiElement nameRange(@NotNull CqrsNamedElement element) {
        PsiElement id = element.getNameIdentifier();
        return id != null ? id : element;
    }

    /** The concrete declaration wrapped by an {@code element} node (for annotation ranges). */
    static @NotNull PsiElement concrete(@NotNull PsiElement elementNode) {
        PsiElement first = elementNode.getFirstChild();
        return first != null ? first : elementNode;
    }

    static int parseIntOrZero(@Nullable PsiElement numberToken) {
        if (numberToken == null) {
            return 0;
        }
        try {
            return Integer.parseInt(numberToken.getText().trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
