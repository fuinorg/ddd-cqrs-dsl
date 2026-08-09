package org.fuin.dsl.ddd.gen.view

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ArtifactNames
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.dsl.ddd.gen.service.SrcService
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the framework-free service contract of a view ({@code <Base>Service}): the read model's
 * operations as plain Java, with no annotations, no {@code ResponseEntity} and no persistence
 * assumptions.
 *
 * <p>This is the interface everything else on the read side is arranged around. The generated
 * {@code <Base>Controller} / {@code <Base>Resource} delegate to it and hold no logic of their own, the
 * hand-written {@code <Base>ServiceImpl} implements it, and {@code <Base>ServiceRestClient} satisfies
 * it over HTTP for a caller in another process. A caller in the same JVM therefore reaches the read
 * model through a plain method call rather than a REST round trip.
 *
 * <p>It is rendered by {@link SrcService}, the same snippet that renders a module level
 * {@code service} of the model, so the two cannot drift into different shapes.
 */
class ViewServiceApiArtifactFactory extends AbstractSource<View> {

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.JAVA_VIEW_SERVICE
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val baseName = ArtifactNames.viewBaseName(view.name)
        val serviceName = baseName + "Service"
        val pkg = view.asPackage
        val fqn = pkg + "." + serviceName

        // Register the FQN so the delegate, the implementation stub and the REST client - all of them in
        // another package, two of them in another module - can import it.
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_SERVICE), fqn)

        if (preparationRun) {
            return null
        }

        val filename = fqn.replace('.', '/') + ".java"
        return List.of(newArtifact(filename, createService(refReg, view, pkg, baseName, serviceName).getBytes("UTF-8"), view))
    }

    private def String createService(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String serviceName) {
        val ctx = new SimpleCodeSnippetContext(refReg)
        val javaDoc = '''
            /**
             * The "«baseName»" read model as plain Java: the operations that MUST be provided, with no
             * implementation, no persistence assumptions and no framework types. Regenerated on every
             * build.
             *
             * <p>This is the contract to depend on from inside the same application. The REST contracts
             * {@link «baseName»ControllerApi} and {@link «baseName»ResourceApi} declare the same
             * operations for a caller in another process; the generated REST classes implementing them
             * do nothing but delegate here, so going through HTTP inside one JVM would buy nothing.
             *
             * <p>An operation the model declares {@code optional} returns an {@link java.util.Optional}.
             * Over HTTP that same absence is a 404, which the generated delegates translate in both
             * directions.
             */
        '''
        val src = new SrcService(ctx, serviceName, javaDoc, view.methods).toString
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}
