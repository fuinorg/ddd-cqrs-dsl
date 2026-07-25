package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.Collections
import java.util.List
import java.util.Set
import org.fuin.srcgen4j.core.emf.CodeSnippet

import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

/**
 * Creates the import statements source code.
 */
class SrcImports implements CodeSnippet {

    val List<String> imports

    new(CodeSnippetContext ctx, String currentPkg, Set<String> importSet) {
        imports = new ArrayList<String>()
        for (imp : importSet) {
            if (!javaLang(imp) && (imp.trim.length > 0) && !currentPkg.equals(imp.onlyPackage)
                && !simpleName(imp)) {
                imports.add(imp)
            }
        }
        Collections.sort(imports)
    }

    /**
     * Determines whether the reference is a bare simple name rather than a qualified one. Java has no
     * import for such a name - it is already in scope where it is used - so it must not be emitted.
     * A nested type that the generated class declares or inherits is registered this way (see the
     * inline services in AbstractAggregateArtifactFactory).
     *
     * @param imp Reference to check.
     *
     * @return TRUE if the reference carries no package.
     */
    def boolean simpleName(String imp) {
        return imp.indexOf(".") == -1
    }
    
    def boolean javaLang(String imp) {
        if (imp.equals("byte[]")) {
        	return true
        }
        if (!imp.startsWith("java.lang.")) {
            return false
        }
        val p = imp.indexOf(".", 10)
        if (p == -1) {
            return true
        } 
        return false
    } 

    override toString() {
        if ((imports === null) || (imports.length == 0)) {
            return "";
        }
        '''
            «FOR imp : imports»
                import «imp»;
            «ENDFOR»
        '''
    }

}
