package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAnnotationDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAnnotationInstance;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConsistency;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConsistencyLevel;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstraintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstraintInstance;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEventDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExceptionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExternalType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsGenericArgs;
import org.fuin.dsl.cqrs.intellij.psi.CqrsInvariants;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessManager;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessReaction;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessState;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;
import org.fuin.dsl.cqrs.intellij.psi.CqrsPreconditions;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.attributeNames;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.concrete;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.findUnknownVar;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.firstTokenAfter;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.firstTypeRefAfter;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.nameRange;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.parameterNames;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.parseIntOrZero;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.resolve;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.sameType;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.stringValue;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.typeNames;
import static org.fuin.dsl.cqrs.intellij.CqrsValidationUtil.typeRefsAfter;

/**
 * Semantic validation for the CQRS DSL, ported from the Eclipse/Xtext {@code CqrsDslValidator}. It
 * reproduces the self-contained (single-file) {@code @Check} rules on top of the Grammar-Kit PSI.
 *
 * <p>Cross-file / project-wide rules (duplicate exception CID, aggregate/entity direct-reference
 * containment, the {@code identifies}/{@code root} placement rules) and the constraint-message and
 * internal-type-invariant target checks are intentionally not ported here — see the README/plan.
 *
 * <p>Resolution-dependent checks are lenient: when a referenced declaration cannot be resolved (for
 * example a not-yet-cached remote model) the check is skipped rather than reporting a false error.
 */
public final class CqrsValidationAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof CqrsValueObject) {
            checkValueObjectBase((CqrsValueObject) element, holder);
        } else if (element instanceof CqrsAttribute) {
            CqrsAttribute attribute = (CqrsAttribute) element;
            checkVariableNameLowerCase(attribute, holder);
            checkGenericArgs(attribute.getTypeRef(), attribute.getGenericArgs(), holder);
            checkConstraintInstancesTargetType(attribute.getInvariants(), attribute.getTypeRef(),
                    "The allowed input types of the constraint", "do not match the attribute type", holder);
        } else if (element instanceof CqrsParameter) {
            CqrsParameter parameter = (CqrsParameter) element;
            checkVariableNameLowerCase(parameter, holder);
            checkGenericArgs(parameter.getTypeRef(), parameter.getGenericArgs(), holder);
            CqrsPreconditions preconditions = parameter.getPreconditions();
            checkConstraintInstancesTargetType(preconditions == null ? null : preconditions.getConstraintInstanceList(),
                    parameter.getTypeRef(), "The input type of the constraint", "does not match the parameter type", holder);
        } else if (element instanceof CqrsExceptionDef) {
            checkExceptionMessage((CqrsExceptionDef) element, holder);
        } else if (element instanceof CqrsEventDef) {
            checkEventMessage((CqrsEventDef) element, holder);
        } else if (element instanceof CqrsConsistency) {
            checkConsistency((CqrsConsistency) element, holder);
        } else if (element instanceof CqrsAggregateDef) {
            checkAggregate((CqrsAggregateDef) element, holder);
        } else if (element instanceof CqrsEntityDef) {
            checkEntity((CqrsEntityDef) element, holder);
        } else if (element instanceof CqrsMethodDef) {
            checkNoEventsInServiceMethod((CqrsMethodDef) element, holder);
        } else if (element instanceof CqrsAnnotationInstance) {
            checkAnnotationInstanceArgs((CqrsAnnotationInstance) element, holder);
        } else if (element instanceof CqrsProcessManager) {
            CqrsProcessManager pm = (CqrsProcessManager) element;
            checkProcessManager(pm, holder);
            checkProcessManagerCronSchedule(pm, holder);
        } else if (element instanceof CqrsProcessReaction) {
            checkProcessReaction((CqrsProcessReaction) element, holder);
        } else if (element instanceof CqrsViewDef) {
            checkViewCronSchedule((CqrsViewDef) element, holder);
        }
    }

    // --- process manager ------------------------------------------------------------------------

    /** Reports duplicate state names within a process manager. */
    private void checkProcessManager(@NotNull CqrsProcessManager pm, @NotNull AnnotationHolder holder) {
        Set<String> seen = new HashSet<>();
        for (CqrsProcessState state : pm.getProcessStateList()) {
            String name = state.getId().getText();
            if (!seen.add(name)) {
                error(holder, state, "Duplicate process state '" + name + "'");
            }
        }
    }

    /** Reports a process-manager 'cron-schedule' that is not a valid Spring cron expression. */
    private void checkProcessManagerCronSchedule(@NotNull CqrsProcessManager pm, @NotNull AnnotationHolder holder) {
        checkCronSchedule(pm.getString(), holder);
    }

    // --- view -----------------------------------------------------------------------------------

    /** Reports a view 'cron-schedule' that is not a valid Spring cron expression. */
    private void checkViewCronSchedule(@NotNull CqrsViewDef view, @NotNull AnnotationHolder holder) {
        // 'view' has two STRING tokens (rest-path, cron-schedule), so there is no named accessor;
        // reach the cron STRING by scanning for the token after the 'cron-schedule' keyword.
        checkCronSchedule(firstTokenAfter(view, CqrsTypes.KW_CRON_SCHEDULE, CqrsTypes.STRING), holder);
    }

    private void checkCronSchedule(@Nullable PsiElement cronToken, @NotNull AnnotationHolder holder) {
        if (cronToken == null) {
            return;
        }
        String value = stringValue(cronToken);
        if (value != null && !SpringCronExpression.isValid(value)) {
            error(holder, cronToken, "Invalid Spring cron expression: '" + value + "'");
        }
    }

    /** Reports a 'correlate-by' key that is not an attribute of the reacted event. */
    private void checkProcessReaction(@NotNull CqrsProcessReaction reaction, @NotNull AnnotationHolder holder) {
        PsiElement key = reaction.getId(); // the 'correlate-by' identifier (optional)
        if (key == null) {
            return;
        }
        List<CqrsTypeRef> refs = reaction.getTypeRefList();
        if (refs.isEmpty()) {
            return;
        }
        CqrsNamedElement target = resolve(refs.get(0)); // the 'reacts-to' event
        if (!(target instanceof CqrsEventDef)) {
            return; // unresolved or not an event: be lenient and skip the check
        }
        CqrsEventDef event = (CqrsEventDef) target;
        List<String> vars = new ArrayList<>(attributeNames(event.getAttributeList()));
        CqrsTypeRef origin = event.getTypeRef(); // 'copies-attributes-of'
        if (origin != null) {
            CqrsNamedElement originTarget = resolve(origin);
            if (originTarget == null) {
                return; // origin present but unresolved: be lenient
            }
            vars.addAll(parameterNames(originTarget));
        }
        String keyText = key.getText();
        if (!vars.contains(keyText)) {
            error(holder, key,
                    "The correlation key '" + keyText + "' is not an attribute of event '" + event.getId().getText() + "'");
        }
    }

    // --- value object 'base' --------------------------------------------------------------------

    private void checkValueObjectBase(@NotNull CqrsValueObject vo, @NotNull AnnotationHolder holder) {
        CqrsTypeRef base = vo.getTypeRef();
        if (base == null) {
            return;
        }
        for (CqrsConstructorDef constructor : vo.getConstructorDefList()) {
            error(holder, nameRange(constructor), "A value object with a 'base' is not allowed to have constructors");
        }
        for (CqrsMethodDef method : vo.getMethodDefList()) {
            error(holder, nameRange(method), "A value object with a 'base' is not allowed to have methods");
        }
    }

    // --- variable naming ------------------------------------------------------------------------

    private void checkVariableNameLowerCase(@NotNull CqrsNamedElement variable, @NotNull AnnotationHolder holder) {
        String name = variable.getName();
        PsiElement id = variable.getNameIdentifier();
        if (name != null && !name.isEmpty() && id != null && !Character.isLowerCase(name.charAt(0))) {
            holder.newAnnotation(HighlightSeverity.WARNING, "Variable names should start with a lower case")
                    .range(id)
                    .create();
        }
    }

    // --- message variables ----------------------------------------------------------------------

    private void checkExceptionMessage(@NotNull CqrsExceptionDef exception, @NotNull AnnotationHolder holder) {
        PsiElement message = exception.getString();
        if (message == null) {
            return;
        }
        String unknown = findUnknownVar(attributeNames(exception.getAttributeList()), stringValue(message));
        if (unknown != null) {
            error(holder, message, "A variable '" + unknown + "' is not defined in the exception");
        }
    }

    private void checkEventMessage(@NotNull CqrsEventDef event, @NotNull AnnotationHolder holder) {
        PsiElement message = event.getString();
        if (message == null) {
            return;
        }
        List<String> vars = new ArrayList<>(attributeNames(event.getAttributeList()));
        CqrsTypeRef origin = event.getTypeRef(); // 'copies-attributes-of'
        if (origin != null) {
            CqrsNamedElement target = resolve(origin);
            if (target == null) {
                return; // origin present but unresolved: be lenient and skip the check
            }
            vars.addAll(parameterNames(target));
        }
        String unknown = findUnknownVar(vars, stringValue(message));
        if (unknown != null) {
            error(holder, message, "A variable with the name '" + unknown + "' is unknown");
        }
    }

    // --- consistency ----------------------------------------------------------------------------

    private void checkConsistency(@NotNull CqrsConsistency consistency, @NotNull AnnotationHolder holder) {
        CqrsConsistencyLevel level = consistency.getConsistencyLevel();
        if (level == null) {
            return;
        }
        String value = level.getText();
        if ("weak".equals(value) && consistency.getWeakConsistency() == null) {
            error(holder, consistency, "You must define the details for weak consistency");
        } else if ("strong".equals(value) && consistency.getWeakConsistency() != null) {
            error(holder, consistency.getWeakConsistency(), "No details required for strong consistency");
        }
    }

    // --- aggregate / entity structure -----------------------------------------------------------

    private void checkAggregate(@NotNull CqrsAggregateDef aggregate, @NotNull AnnotationHolder holder) {
        List<CqrsAggregateId> ids = new ArrayList<>();
        for (CqrsElement element : aggregate.getElementList()) {
            if (element.getAggregateId() != null) {
                ids.add(element.getAggregateId());
            } else if (!isAllowedInAggregate(element)) {
                error(holder, concrete(element), "Allowed elements in an aggregate are: 'aggregate-id', 'entity',"
                        + " 'event', 'command' and 'value-object'");
            }
        }
        for (int i = 1; i < ids.size(); i++) {
            error(holder, nameRange(ids.get(i)), "Only one 'aggregate-id' can be defined inside an aggregate");
        }

        boolean hasIdentifier = aggregate.getTypeRef() != null; // 'identifier' reference
        boolean hasNestedId = !ids.isEmpty();
        if (!hasIdentifier && !hasNestedId) {
            error(holder, nameRange(aggregate), "Aggregate does not define an ID");
        } else if (hasIdentifier && hasNestedId) {
            error(holder, nameRange(aggregate),
                    "Aggregate cannot reference an ID using 'identifier' and define another inside the aggregate");
        }
    }

    private static boolean isAllowedInAggregate(@NotNull CqrsElement element) {
        return element.getAggregateId() != null
                || element.getEntityDef() != null
                || element.getEventDef() != null
                || element.getCommandDef() != null
                || element.getValueObject() != null;
    }

    private void checkEntity(@NotNull CqrsEntityDef entity, @NotNull AnnotationHolder holder) {
        List<CqrsEntityId> ids = new ArrayList<>();
        for (CqrsElement element : entity.getElementList()) {
            if (element.getEntityId() != null) {
                ids.add(element.getEntityId());
            } else if (!isAllowedInEntity(element)) {
                error(holder, concrete(element),
                        "Allowed elements in an entity are: 'entity-id', 'event' and 'value-object'");
            }
        }
        for (int i = 1; i < ids.size(); i++) {
            error(holder, nameRange(ids.get(i)), "Only one 'entity-id' can be defined inside an entity");
        }

        boolean hasIdentifier = firstTypeRefAfter(entity, CqrsTypes.KW_IDENTIFIER) != null;
        boolean hasNestedId = !ids.isEmpty();
        if (!hasIdentifier && !hasNestedId) {
            error(holder, nameRange(entity), "Entity does not define an ID");
        } else if (hasIdentifier && hasNestedId) {
            error(holder, nameRange(entity),
                    "Entity cannot reference an ID using 'identifier' and define another inside the aggregate");
        }
    }

    private static boolean isAllowedInEntity(@NotNull CqrsElement element) {
        return element.getEntityId() != null
                || element.getEventDef() != null
                || element.getValueObject() != null;
    }

    // --- service methods ------------------------------------------------------------------------

    private void checkNoEventsInServiceMethod(@NotNull CqrsMethodDef method, @NotNull AnnotationHolder holder) {
        if (PsiTreeUtil.getParentOfType(method, CqrsServiceDef.class) == null) {
            return;
        }
        for (CqrsTypeRef fired : typeRefsAfter(method, CqrsTypes.KW_FIRES)) {
            error(holder, fired, "A service method cannot fire events");
        }
        for (CqrsEventDef declared : method.getEventDefList()) {
            error(holder, nameRange(declared), "A service method cannot declare events");
        }
    }

    // --- annotation instance argument count -----------------------------------------------------

    private void checkAnnotationInstanceArgs(@NotNull CqrsAnnotationInstance instance, @NotNull AnnotationHolder holder) {
        CqrsNamedElement declaration = resolve(instance.getTypeRef());
        if (!(declaration instanceof CqrsAnnotationDef)) {
            return; // unresolved, or not an annotation: skip
        }
        int required = ((CqrsAnnotationDef) declaration).getAttributeList().size();
        int actual = instance.getLiteralList().size();
        if (required != actual) {
            error(holder, instance, "The number of arguments does not match the number required by the type: "
                    + ((CqrsAnnotationDef) declaration).getName());
        }
    }

    // --- generic argument count -----------------------------------------------------------------

    private void checkGenericArgs(@Nullable CqrsTypeRef typeRef, @Nullable CqrsGenericArgs generics,
                                  @NotNull AnnotationHolder holder) {
        CqrsNamedElement declaration = resolve(typeRef);
        if (!(declaration instanceof CqrsExternalType)) {
            return; // generics count only applies to external types
        }
        int required = parseIntOrZero(((CqrsExternalType) declaration).getNumber());
        int actual = generics == null ? 0 : generics.getTypeRefList().size();
        if (required != actual) {
            PsiElement anchor = generics != null ? generics : typeRef;
            error(holder, anchor, "The number of arguments does not match the number required by the type: " + required);
        }
    }

    // --- constraint target-type (attribute invariants / parameter preconditions) ----------------

    private void checkConstraintInstancesTargetType(@Nullable CqrsInvariants invariants, @Nullable CqrsTypeRef targetType,
                                                    @NotNull String prefix, @NotNull String suffix,
                                                    @NotNull AnnotationHolder holder) {
        checkConstraintInstancesTargetType(invariants == null ? null : invariants.getConstraintInstanceList(),
                targetType, prefix, suffix, holder);
    }

    private void checkConstraintInstancesTargetType(@Nullable List<CqrsConstraintInstance> instances,
                                                    @Nullable CqrsTypeRef targetType, @NotNull String prefix,
                                                    @NotNull String suffix, @NotNull AnnotationHolder holder) {
        if (instances == null || targetType == null) {
            return;
        }
        for (CqrsConstraintInstance instance : instances) {
            CqrsNamedElement declaration = resolve(instance.getTypeRef());
            if (!(declaration instanceof CqrsConstraintDef)) {
                continue; // unresolved constraint: skip
            }
            List<CqrsTypeRef> inputs = typeRefsAfter(declaration, CqrsTypes.KW_INPUT);
            if (inputs.isEmpty()) {
                continue; // a constraint without 'input' applies to any type
            }
            boolean matches = false;
            for (CqrsTypeRef input : inputs) {
                if (sameType(input, targetType)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                error(holder, instance, prefix + " (" + typeNames(inputs) + ") " + suffix);
            }
        }
    }

    // --- helper ---------------------------------------------------------------------------------

    private static void error(@NotNull AnnotationHolder holder, @NotNull PsiElement range, @NotNull String message) {
        holder.newAnnotation(HighlightSeverity.ERROR, message).range(range).create();
    }
}
