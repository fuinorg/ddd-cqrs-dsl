package org.fuin.dsl.ddd.gen.service

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcMethodSignature
import org.fuin.dsl.ddd.gen.rule.KeyDerivation
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Creates source code for a plain Java service interface: a name, a JavaDoc comment and one method
 * declaration per operation, with no annotations and no framework types.
 *
 * <p>Two things are rendered this way and must stay identical in shape: a module level
 * {@code service} of the model, and the service contract of a {@code view} (whose name is derived from
 * the view rather than taken from the element, which is why the name is a separate argument).
 */
class SrcService implements CodeSnippet {

    val CodeSnippetContext ctx
    val String name
    val CharSequence javaDoc
    val List<Method> methods

    /**
     * Constructor for a module level service of the model. Its JavaDoc is the one the model declares,
     * rendered as a single sentence.
     *
     * @param ctx Context.
     * @param service Service to create the source for.
     */
    new(CodeSnippetContext ctx, Service service) {
        this(ctx, service.name, new SrcJavaDocType(service.doc).toString, declaredAndDerived(service))
    }

    /**
     * What the interface declares: the model's own methods, then whatever a business key adds.
     *
     * <p>A key checked by the operation this service serves needs an answer to "is it taken", and the
     * key derives that method rather than the model writing it out. Appended rather than mixed in, so
     * the interface reads in the order the model does.</p>
     */
    def private static List<Method> declaredAndDerived(Service service) {
        val out = new ArrayList<Method>(service.methods.nullSafe.toList)
        out.addAll(KeyDerivation.derivedMethods(service))
        return out
    }

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param name Name of the interface.
     * @param javaDoc Fully rendered JavaDoc block of the interface, including the comment characters.
     *            A generated contract explains a role the model does not state, which takes more than
     *            the one line a model element's doc becomes.
     * @param methods Operations the interface declares.
     */
    new(CodeSnippetContext ctx, String name, CharSequence javaDoc, List<Method> methods) {
        this.ctx = ctx
        this.name = name
        this.javaDoc = javaDoc
        this.methods = methods
    }

    override toString() {
        '''    
        «javaDoc»
        public interface «name» {
            
            «FOR method : methods.nullSafe»
                «new SrcJavaDocMethod(ctx, method).toString»
                «new SrcMethodSignature(ctx, "public", false, GenerateOptions.empty(), method).toString»;
                
            «ENDFOR»
        }
        '''
    }

}
