package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.PsiElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAnnotationDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRule;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandHandler;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstraintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsDataProtectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityIdPath;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumInstance;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEventDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExceptionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExternalType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsHintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsKeyDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsModuleDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessManager;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessState;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProjectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;
import org.jetbrains.annotations.NotNull;

/** What each kind of declaration is called when several of them are grouped together. */
public final class CqrsKinds {

    /** What a declaration of an unknown kind is filed under, so nothing ever falls out of the tree. */
    public static final String OTHER = "other";

    private CqrsKinds() {
    }

    /**
     * The name of the group one declaration belongs in: its own keyword, pluralised.
     *
     * <p>The words the model is written in rather than prose titles - a reader who sees
     * <code>value-objects</code> knows what to type to add one, which a "Value Objects" heading would
     * make them translate back. It is the same idea as {@link CqrsIcons#forElement(PsiElement)}: one
     * switch on the kind, in one place, so a tree and a toolbar cannot disagree about what something is.
     *
     * <p>Every named element answers, including the ones only the structure view goes deep enough to
     * show. A kind nobody has taught this about is filed under {@link #OTHER} rather than dropped.
     *
     * @param element Declaration to name the group for.
     *
     * @return Group name, never <code>null</code>.
     */
    public static @NotNull String pluralOf(PsiElement element) {
        if (element instanceof CqrsContextDef) {
            return "contexts";
        }
        if (element instanceof CqrsModuleDef) {
            return "modules";
        }
        if (element instanceof CqrsAggregateDef) {
            return "aggregates";
        }
        if (element instanceof CqrsEntityDef) {
            return "entities";
        }
        if (element instanceof CqrsAggregateId) {
            return "aggregate-ids";
        }
        if (element instanceof CqrsEntityId) {
            return "entity-ids";
        }
        if (element instanceof CqrsEntityIdPath) {
            return "entity-id-paths";
        }
        if (element instanceof CqrsValueObject) {
            return "value-objects";
        }
        if (element instanceof CqrsEnumObject) {
            return "enums";
        }
        if (element instanceof CqrsEnumInstance) {
            return "instances";
        }
        if (element instanceof CqrsEventDef) {
            return "events";
        }
        if (element instanceof CqrsCommandDef) {
            return "commands";
        }
        if (element instanceof CqrsCommandHandler) {
            return "command-handlers";
        }
        if (element instanceof CqrsExceptionDef) {
            return "exceptions";
        }
        if (element instanceof CqrsBusinessRule) {
            return "business-rules";
        }
        if (element instanceof CqrsKeyDef) {
            return "keys";
        }
        if (element instanceof CqrsServiceDef) {
            return "services";
        }
        if (element instanceof CqrsViewDef) {
            return "views";
        }
        if (element instanceof CqrsProjectionDef) {
            return "projections";
        }
        if (element instanceof CqrsProcessManager) {
            return "process-managers";
        }
        if (element instanceof CqrsProcessState) {
            return "states";
        }
        if (element instanceof CqrsConstraintDef) {
            return "constraints";
        }
        if (element instanceof CqrsAnnotationDef) {
            return "annotations";
        }
        if (element instanceof CqrsDataProtectionDef) {
            return "data-protections";
        }
        if (element instanceof CqrsExternalType) {
            return "types";
        }
        if (element instanceof CqrsHintDef) {
            return "hints";
        }
        if (element instanceof CqrsConstructorDef) {
            return "constructors";
        }
        if (element instanceof CqrsMethodDef) {
            return "methods";
        }
        if (element instanceof CqrsParameter) {
            return "parameters";
        }
        if (element instanceof CqrsAttribute) {
            return "attributes";
        }
        return OTHER;
    }

}
