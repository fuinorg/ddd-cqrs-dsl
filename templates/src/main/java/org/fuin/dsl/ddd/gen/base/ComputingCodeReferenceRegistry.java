package org.fuin.dsl.ddd.gen.base;

import java.util.HashMap;
import java.util.Map;

import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement;
import org.fuin.dsl.ddd.gen.script.CqrsScripts;
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry;

/**
 * Code reference registry that <em>computes</em> the fully qualified name of a generated type when no
 * factory registered it.
 * <p>
 * A registered name always wins, so a factory that generates a type still decides what it is called.
 * What changes is the answer for everything else. Before, a key nobody had registered fell through
 * {@code SimpleCodeSnippetContext} and came back as the key itself - which turned a missing registration
 * into an import of the model's own unique name, silently and without a single warning.
 * <p>
 * Since a key made by {@link TypeKeys#refKey(AbstractElement, String)} names both the element and the
 * kind of type, the name can simply be worked out instead: the package from the element's own
 * {@code model2JavaPackage} script and the simple name from {@link JavaNames}. That is what makes a
 * reference to a type of a <em>dependency</em> resolve even though the dependency's factories never ran
 * in this build, and it is why a type key that nothing can name at all fails loudly.
 */
public final class ComputingCodeReferenceRegistry implements CodeReferenceRegistry {

    private final Map<String, String> registered = new HashMap<>();

    @Override
    public String getReference(final String uniqueName) {
        final String fqn = registered.get(uniqueName);
        if (fqn != null) {
            return fqn;
        }
        return computed(uniqueName);
    }

    @Override
    public void putReference(final String uniqueName, final String fqn) {
        registered.put(uniqueName, fqn);
    }

    /**
     * Works the fully qualified name out from the key, or returns <code>null</code> when the key does
     * not name a generated type (an external type, for instance, which is registered by
     * {@code CtxExternalTypes} and has no package of its own to compute).
     */
    private String computed(final String key) {
        final AbstractElement element = TypeKeys.elementOf(key);
        if (element == null) {
            return null;
        }
        final String typeKey = TypeKeys.typeKeyOf(key);
        final String simpleName = JavaNames.simpleName(element.getName(), typeKey);
        if (simpleName == null) {
            return null;
        }
        return CqrsScripts.model2JavaPackage(element, typeKey) + "." + simpleName;
    }

}
