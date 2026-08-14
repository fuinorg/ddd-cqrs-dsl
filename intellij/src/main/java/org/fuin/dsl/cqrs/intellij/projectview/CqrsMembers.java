package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.psi.PsiElement;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumInstance;
import org.fuin.dsl.cqrs.intellij.psi.CqrsHintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsModuleDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The shape of the CQRS tree in the Project view: which declarations show up directly beneath which
 * other declaration.
 *
 * <p>This is deliberately <b>not</b> the same tree as the structure view (Alt+7). That one shows every
 * named element there is, down to a method's parameters and the services and events declared inside it.
 * Useful in a file outline; too deep for the Project view, where the tree is a way of finding a
 * declaration rather than reading one. The floor here is the <b>method</b>, matching what "show members"
 * means for Java.</p>
 *
 * <p><b>The rule is per parent, not per kind</b>, and one element makes that unavoidable: a
 * {@code service} is a declaration of its own at module level, and is <em>also</em> what an operation
 * declares inline as its {@code operation-context}. Same for an {@code event}. Asking "is this element a
 * service?" therefore cannot answer "does it belong in the tree?" - only "what may appear beneath the
 * thing I am expanding?" can.</p>
 *
 * <p>Traversal is over <b>direct</b> children only. The structure view walks the whole subtree with
 * {@code PsiTreeUtil.findChildrenOfType} and then keeps the direct children, which costs the same for a
 * single open outline but not in a tree where many files may be expanded at once.</p>
 */
public final class CqrsMembers {

    private CqrsMembers() {
    }

    /**
     * The declarations shown directly beneath the given element.
     *
     * @param parent Element being expanded - a {@link CqrsFile} or any declaration.
     *
     * @return Direct child declarations in source order, empty when the element is a leaf of this tree.
     */
    public static @NotNull List<CqrsNamedElement> childrenOf(PsiElement parent) {
        if (parent == null || !parent.isValid() || isLeaf(parent)) {
            return Collections.emptyList();
        }
        final List<CqrsNamedElement> result = new ArrayList<>();
        collect(parent, parent, result);
        return result;
    }

    /**
     * Whether the given element has anything beneath it, which is what decides if the tree draws an
     * expand arrow.
     *
     * @param parent Element to check.
     *
     * @return TRUE if expanding it would show at least one declaration.
     */
    public static boolean hasChildren(PsiElement parent) {
        return !childrenOf(parent).isEmpty();
    }

    /**
     * Walks the direct children, stepping through the wrapper rules the grammar puts around a nested
     * declaration ({@code element} inside a module, {@code entity_element} inside an aggregate or
     * entity). Those wrappers are plain alternative holders with no name of their own, so they are
     * transparent here rather than nodes.
     */
    private static void collect(PsiElement owner, PsiElement current, List<CqrsNamedElement> result) {
        for (PsiElement child = current.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof CqrsElement || child instanceof CqrsEntityElement) {
                collect(owner, child, result);
            } else if (child instanceof CqrsNamedElement named && isShownUnder(owner, named)) {
                result.add(named);
            }
        }
    }

    /**
     * Whether the element gets a row of its own somewhere in the tree.
     *
     * <p>Independent of position, so it answers "could this ever be selected?" - which is what
     * autoscroll-from-source needs when it walks outwards from the caret.</p>
     *
     * @param element Element to check.
     *
     * @return TRUE if the tree draws this kind of declaration.
     */
    public static boolean isShownAnywhere(PsiElement element) {
        return element instanceof CqrsNamedElement named && !isValueOfItsOwner(named);
    }

    /** An element the tree never expands, whatever it happens to contain. */
    static boolean isLeaf(PsiElement element) {
        // A method is the floor: its parameters, its operation-context service and the events it
        // declares inline are all part of reading the operation, not of finding it.
        return element instanceof CqrsMethodDef || element instanceof CqrsConstructorDef;
    }

    /**
     * Whether a declaration is shown directly beneath the element being expanded.
     *
     * @param owner Element being expanded.
     * @param child Candidate declaration found among its direct children.
     *
     * @return TRUE if the child belongs in the tree at that position.
     */
    private static boolean isShownUnder(PsiElement owner, CqrsNamedElement child) {
        if (isValueOfItsOwner(child)) {
            return false;
        }
        if (owner instanceof CqrsFile) {
            return child instanceof CqrsContextDef;
        }
        if (owner instanceof CqrsContextDef) {
            return child instanceof CqrsModuleDef;
        }
        // Inside a module every declaration is a top level one; inside a declaration the members are
        // whatever the grammar allows there - business rules, constructors, methods, and the entities
        // and value objects nested in an aggregate. Everything that survived the two filters above is
        // wanted, because the wrappers were already stepped through and anything too deep sits under a
        // method, which never gets here.
        return true;
    }

    /**
     * Whether the element spells out its owner rather than being something to navigate to.
     *
     * <p>These are named, so they are nodes in the structure view, and they are direct children of the
     * declaration that holds them - an attribute of an aggregate, an instance of an enum. But they are
     * what the declaration <em>is</em>, not a place in the file worth steering to, and listing them
     * doubles the height of the tree for no gain.</p>
     */
    private static boolean isValueOfItsOwner(CqrsNamedElement element) {
        return element instanceof CqrsAttribute
                || element instanceof CqrsParameter
                || element instanceof CqrsEnumInstance
                || element instanceof CqrsProcessState
                // A hint directs the generator; it says nothing about the model.
                || element instanceof CqrsHintDef;
    }
}
