package org.fuin.dsl.ddd.gen.service

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import java.util.List

class ServiceArtifactFactory extends AbstractSource<Service> {

    override getModelType() {
        typeof(Service)
    }

    override getTypeKey() {
        TypeKeys.JAVA_SERVICE
    }

    override create(Service service, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (!(service.eContainer instanceof Module)) {
            // Do not create separate interface file 
            // for services in constructors or methods
            return null
        }

        val className = service.name
        val pkg = service.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(service), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(service)
        ctx.addReferences(service)

        return List.of(newArtifact(filename,
            create(ctx, service, pkg, className).toString().getBytes("UTF-8"), service));
    }

    def addImports(CodeSnippetContext ctx, Service service) {
        // Nothing to do
    }

    def addReferences(CodeSnippetContext ctx, Service service) {
        // Nothing to do
    }

    def create(SimpleCodeSnippetContext ctx, Service service, String pkg, String className) {
        val String src = ''' 
            «new SrcService(ctx, service)»
            '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}
