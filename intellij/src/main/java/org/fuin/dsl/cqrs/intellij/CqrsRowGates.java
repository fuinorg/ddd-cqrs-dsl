package org.fuin.dsl.cqrs.intellij;

import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRule;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRuleInstance;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRules;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsIdentifiedBy;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;
import org.fuin.dsl.cqrs.intellij.psi.CqrsReturnType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsRuleArgument;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceArgument;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.firstTypeRefAfter;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.resolve;

/**
 * Which rows offer a command whose client-answerable gates they cannot answer.
 *
 * <p>A menu is drawn on a row, and a command gated by a rule over the aggregate's own state can be left
 * out of it rather than offered and refused. The client decides that from what the row publishes, so a
 * row that offers the command and omits what the gate reads makes the gate work on one screen and
 * quietly do nothing on another - and a gate that silently does not work is indistinguishable from one
 * nobody wrote.
 *
 * <p>The port of the Xtext validator's {@code checkRowAnswersTheGatesItOffers}, so the editor says what
 * the build says. Whether the row should publish it is a modelling decision with real costs on the other
 * side, which is why both surfaces warn rather than refuse.
 *
 * <p>Project wide and cached per PSI modification, like the module graph: a row and the command it
 * offers are commonly in different files, and rebuilding this per keystroke would not pay.
 */
public final class CqrsRowGates {

    private CqrsRowGates() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** One gate a row offers and cannot answer. */
    public record Unanswered(@NotNull String command, @NotNull String rule, @NotNull List<String> missing) {
    }

    /**
     * The gates the given row offers and cannot answer.
     *
     * @param row Value object that may be a row.
     *
     * @return What it would have to publish, empty when it answers everything it offers - and empty
     *         while indexes are still building, which is the only honest answer then.
     */
    public static @NotNull List<Unanswered> of(@NotNull CqrsValueObject row) {
        return byRow(row.getProject()).getOrDefault(row, List.of());
    }

    private static Map<PsiElement, List<Unanswered>> byRow(Project project) {
        return CachedValuesManager.getManager(project).getCachedValue(project,
                () -> CachedValueProvider.Result.create(build(project), PsiModificationTracker.MODIFICATION_COUNT));
    }

    private static Map<PsiElement, List<Unanswered>> build(Project project) {
        Map<PsiElement, List<Unanswered>> result = new HashMap<>();
        Collection<VirtualFile> files;
        try {
            files = FileTypeIndex.getFiles(CqrsFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        } catch (IndexNotReadyException notReady) {
            return result;
        }
        List<CqrsCommandDef> commands = new ArrayList<>();
        List<CqrsViewDef> views = new ArrayList<>();
        PsiManager psiManager = PsiManager.getInstance(project);
        for (VirtualFile vf : files) {
            PsiFile psiFile = psiManager.findFile(vf);
            if (!(psiFile instanceof CqrsFile)) {
                continue;
            }
            commands.addAll(PsiTreeUtil.findChildrenOfType(psiFile, CqrsCommandDef.class));
            views.addAll(PsiTreeUtil.findChildrenOfType(psiFile, CqrsViewDef.class));
        }
        for (CqrsValueObject row : rowsReturnedBy(views)) {
            List<Unanswered> unanswered = unansweredOf(row, commands);
            if (!unanswered.isEmpty()) {
                result.put(row, unanswered);
            }
        }
        return result;
    }

    /**
     * Every value object some view method hands back.
     *
     * <p>That is what makes it a row a menu is drawn on. A value object merely holding a reference to
     * something looks identified in exactly the same way and no menu is ever drawn on it.
     */
    private static Set<CqrsValueObject> rowsReturnedBy(List<CqrsViewDef> views) {
        Set<CqrsValueObject> rows = new HashSet<>();
        for (CqrsViewDef view : views) {
            for (CqrsMethodDef method : view.getMethodDefList()) {
                CqrsReturnType returns = method.getReturnType();
                if (returns == null) {
                    continue;
                }
                // A list method returns 'List<Row>', so the row is the generic argument rather than the
                // declared type.
                CqrsTypeRef returned = returns.getGenericArgs() == null
                        || returns.getGenericArgs().getTypeRefList().isEmpty()
                                ? returns.getTypeRef()
                                : returns.getGenericArgs().getTypeRefList().get(0);
                CqrsNamedElement declaration = resolve(returned);
                if (declaration instanceof CqrsValueObject) {
                    rows.add((CqrsValueObject) declaration);
                }
            }
        }
        return rows;
    }

    private static List<Unanswered> unansweredOf(CqrsValueObject row, List<CqrsCommandDef> commands) {
        List<Unanswered> result = new ArrayList<>();
        CqrsAttribute identity = rowIdentity(row);
        if (identity == null) {
            return result;
        }
        CqrsNamedElement identityType = resolve(identity.getTypeRef());
        if (identityType == null) {
            return result;
        }
        Set<String> published = new HashSet<>();
        for (CqrsAttribute attribute : row.getAttributeList()) {
            published.add(attribute.getName());
        }
        for (CqrsCommandDef command : commands) {
            CqrsMethodDef operation = targetOf(command);
            if (operation == null || resolve(ownerIdentifier(operation)) != identityType) {
                continue;
            }
            CqrsBusinessRules gates = operation.getBusinessRules();
            if (gates == null) {
                continue;
            }
            for (CqrsBusinessRuleInstance instance : gates.getBusinessRuleInstanceList()) {
                List<String> missing = missingFor(instance, operation, published);
                if (!missing.isEmpty()) {
                    result.add(new Unanswered(nameOf(command), nameOf(resolve(instance.getTypeRef())), missing));
                }
            }
        }
        return result;
    }

    /**
     * The attribute that identifies a row, whether the row says so or the type does.
     *
     * <p>Mirrors the DSL's own rule: a declared {@code identified-by} wins, and without one a single
     * attribute typed as an aggregate id or an entity id is unambiguous. A path is never inferred - rows
     * carry paths to other things far more often than as their own identity.
     */
    private static @Nullable CqrsAttribute rowIdentity(CqrsValueObject row) {
        CqrsIdentifiedBy declared = row.getIdentifiedBy();
        if (declared != null) {
            String name = declared.getId().getText();
            for (CqrsAttribute attribute : row.getAttributeList()) {
                if (name.equals(attribute.getName())) {
                    return attribute;
                }
            }
            return null;
        }
        CqrsAttribute found = null;
        for (CqrsAttribute attribute : row.getAttributeList()) {
            CqrsNamedElement type = resolve(attribute.getTypeRef());
            if (type instanceof CqrsAggregateId || type instanceof CqrsEntityId) {
                if (found != null) {
                    return null; // two of them, and nothing says which
                }
                found = attribute;
            }
        }
        return found;
    }

    /** The operation a command addresses, or {@code null} when it addresses none. */
    private static @Nullable CqrsMethodDef targetOf(CqrsCommandDef command) {
        CqrsNamedElement target = resolve(firstTypeRefAfter(command, CqrsTypes.KW_TARGET));
        return target instanceof CqrsMethodDef ? (CqrsMethodDef) target : null;
    }

    /** The {@code identifier} of the aggregate or entity declaring the operation. */
    private static @Nullable CqrsTypeRef ownerIdentifier(CqrsMethodDef operation) {
        CqrsEntityDef entity = PsiTreeUtil.getParentOfType(operation, CqrsEntityDef.class);
        if (entity != null) {
            return firstTypeRefAfter(entity, CqrsTypes.KW_IDENTIFIER);
        }
        CqrsAggregateDef aggregate = PsiTreeUtil.getParentOfType(operation, CqrsAggregateDef.class);
        return aggregate == null ? null : firstTypeRefAfter(aggregate, CqrsTypes.KW_IDENTIFIER);
    }

    /**
     * What the row would have to publish for a client to answer this usage, and does not.
     *
     * <p>Empty for a usage no client could answer however much the row published: a service call is a
     * question only the server can ask, a parameter of the operation has not been typed yet when a menu
     * decides, and a rule with no condition is written by hand and ships no predicate.
     */
    private static List<String> missingFor(CqrsBusinessRuleInstance instance, CqrsMethodDef operation,
            Set<String> published) {
        List<String> missing = new ArrayList<>();
        CqrsNamedElement declaration = resolve(instance.getTypeRef());
        if (!(declaration instanceof CqrsBusinessRule)) {
            return missing;
        }
        CqrsBusinessRule rule = (CqrsBusinessRule) declaration;
        if (rule.getRuleRequires() == null
                || rule.getAttributeList().size() != instance.getRuleArgumentList().size()) {
            return missing;
        }
        Set<String> parameters = new HashSet<>();
        for (CqrsParameter parameter : PsiTreeUtil.findChildrenOfType(operation, CqrsParameter.class)) {
            parameters.add(parameter.getName());
        }
        List<String> reads = new ArrayList<>();
        for (CqrsRuleArgument actual : instance.getRuleArgumentList()) {
            CqrsServiceArgument bound = actual.getServiceArgument();
            if (bound == null) {
                return List.of(); // a service call or a literal: not on the client at all
            }
            if (bound.getIdentityArgument() != null) {
                continue; // the row knows its own identity
            }
            String name = nameOfArgument(bound);
            if (name == null || parameters.contains(name)) {
                return List.of(); // nobody has typed it yet when the menu decides
            }
            reads.add(name);
        }
        for (String name : reads) {
            if (!published.contains(name)) {
                missing.add(name);
            }
        }
        return missing;
    }

    private static @Nullable String nameOfArgument(CqrsServiceArgument argument) {
        if (argument.getCarrierAttributeArgument() != null) {
            PsiElement id = argument.getCarrierAttributeArgument().getId();
            return id == null ? null : id.getText();
        }
        if (argument.getVariableArgument() != null) {
            return argument.getVariableArgument().getText();
        }
        return null;
    }

    private static String nameOf(@Nullable PsiElement element) {
        if (element instanceof CqrsNamedElement) {
            String name = ((CqrsNamedElement) element).getName();
            if (name != null) {
                return name;
            }
        }
        return "?";
    }

}
