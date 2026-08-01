package org.fuin.dsl.ddd.gen.base

import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Creates source code for a single method.
 */
class SrcMethodSignature implements CodeSnippet {

    val CodeSnippetContext ctx
    val GenerateOptions options
    val MethodData methodData
    val String returnType

    /**
     * Constructor with method.
     * 
     * @param ctx Context.
     * @param modifiers Modifiers (Don't include "abstract" - Use next argument instead).
     * @param makeAbstract TRUE for an abstract method or FALSE for a non-abstract method with "// TODO Implement!".
     * @param options Options to use.
     * @param method Method to create the source for.
     */
    new(CodeSnippetContext ctx, String modifiers, boolean makeAbstract, GenerateOptions options, Method method) {
        this(ctx, options, new MethodData(modifiers, makeAbstract, method));
    }

    /**
     * Constructor with all mandatory data.
     * 
     * @param ctx Context.
     * @param options Options to use.
     * @param methodData data.
     */
    new(CodeSnippetContext ctx, GenerateOptions options, MethodData methodData) {
        this.ctx = ctx
        this.options = options
        this.methodData = methodData
        if (methodData.returnType === null) {
            this.returnType = "void"
        } else {
            ctx.requiresReference(TypeKeys.refKey(methodData.returnType.type))
            val generics = methodData.returnType.generics
            var String type
            if (generics === null) {
                type = methodData.returnType.type.name
            } else {
                val StringBuilder sb = new StringBuilder()
                for (arg : generics.args) {
                    if (sb.length > 0) {
                        sb.append(", ")
                    }
                    sb.append(arg.name)
                    ctx.requiresReference(TypeKeys.refKey(arg))
                }
                type = methodData.returnType.type.name + "<" + sb + ">"
            }
            if (methodData.returnType.optional === null) {
                this.returnType = type
            } else {
                // An absent result is expressed as an empty Optional of the declared type
                ctx.requiresImport("java.util.Optional")
                this.returnType = "Optional<" + type + ">"
            }
        }
    }

    override toString() {
        '''
            «FOR annotation : methodData.annotations.nullSafe»
                «annotation»
            «ENDFOR»
            «methodData.modifiers» «IF methodData.makeAbstract»abstract «ENDIF»«returnType» «methodData.name»(«new SrcParamsDecl(ctx,
                options, methodData.parameters)»)«new SrcThrowsExceptions(ctx, methodData.exceptions)»'''
    }

}
