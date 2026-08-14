package org.fuin.dsl.cqrs.intellij;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAnnotationDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRule;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandHandler;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstraintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsDataProtectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEventDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExceptionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExternalType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsModuleDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessManager;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProjectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;

import javax.swing.Icon;

/** Icons used by the CQRS DSL plugin. */
public final class CqrsIcons {

    /** Icon for {@code .cqrs} files. */
    public static final Icon FILE = IconLoader.getIcon("/icons/cqrs.svg", CqrsIcons.class);

    private CqrsIcons() {
    }

    /**
     * The icon for one declaration, so a tree of them can be read by shape rather than by spelling out
     * every name.
     *
     * <p>These are the platform's own node icons rather than artwork of this plugin: a reader already
     * knows what a class, an interface and a method look like in this IDE, and the mapping below is
     * chosen so that knowledge transfers - an aggregate holds state and behaviour like a class, a view
     * is a contract like an interface, an operation is a method. Nothing here is a claim that the
     * generated Java uses that exact construct.</p>
     *
     * @param element Declaration to pick an icon for - may be <code>null</code>.
     *
     * @return Icon, falling back to the file icon for anything unmapped.
     */
    public static Icon forElement(PsiElement element) {
        if (element instanceof CqrsContextDef || element instanceof CqrsModuleDef) {
            return AllIcons.Nodes.Package;
        }
        if (element instanceof CqrsAggregateDef || element instanceof CqrsEntityDef) {
            return AllIcons.Nodes.Class;
        }
        if (element instanceof CqrsValueObject) {
            return AllIcons.Nodes.Record;
        }
        if (element instanceof CqrsAggregateId || element instanceof CqrsEntityId) {
            return AllIcons.Nodes.Alias;
        }
        if (element instanceof CqrsEnumObject) {
            return AllIcons.Nodes.Enum;
        }
        if (element instanceof CqrsEventDef) {
            return AllIcons.Nodes.Static;
        }
        if (element instanceof CqrsCommandDef || element instanceof CqrsCommandHandler) {
            return AllIcons.Nodes.Function;
        }
        if (element instanceof CqrsViewDef || element instanceof CqrsProjectionDef
                || element instanceof CqrsServiceDef) {
            return AllIcons.Nodes.Interface;
        }
        if (element instanceof CqrsExceptionDef) {
            return AllIcons.Nodes.ExceptionClass;
        }
        if (element instanceof CqrsConstraintDef || element instanceof CqrsAnnotationDef
                || element instanceof CqrsDataProtectionDef) {
            return AllIcons.Nodes.Annotationtype;
        }
        if (element instanceof CqrsProcessManager) {
            return AllIcons.Nodes.Controller;
        }
        if (element instanceof CqrsMethodDef || element instanceof CqrsConstructorDef) {
            return AllIcons.Nodes.Method;
        }
        if (element instanceof CqrsBusinessRule) {
            return AllIcons.Nodes.Test;
        }
        if (element instanceof CqrsExternalType) {
            return AllIcons.Nodes.Type;
        }
        return FILE;
    }
}
