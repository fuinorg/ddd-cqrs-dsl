package org.fuin.dsl.ddd.gen.view

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ArtifactNames
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates {@code <Base>ServiceRestClient}: an implementation of a view's service contract that
 * reaches the read model over HTTP, for a deployment where the query side is a separate process.
 *
 * <p>It is what lets a caller depend on {@code <Base>Service} without knowing where the read model
 * runs. In an application that contains the query side, the hand-written {@code <Base>ServiceImpl} is
 * injected and no HTTP is involved; in one that does not, this adapter is wired over the generated
 * {@code @HttpExchange} proxy instead. Neither caller changes.
 *
 * <p>Only the Spring flavour is generated, because it is the only one that needs an adapter: the
 * Quarkus contract already returns the value itself rather than a {@code ResponseEntity}, so a
 * {@code @RestClient <Base>ResourceApi} is nearly the service contract already.
 */
class ViewServiceRestClientArtifactFactory extends AbstractSource<View> {

    /** Name of the field the generated class forwards to. */
    static val TARGET = "api"

    override getModelType() {
        typeof(View)
    }

    override getTypeKey() {
        TypeKeys.JAVA_VIEW_SERVICE_REST_CLIENT
    }

    override create(View view, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val baseName = ArtifactNames.viewBaseName(view.name)
        val className = baseName + "ServiceRestClient"
        val pkg = view.asPackage
        val fqn = pkg + "." + className

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_SERVICE_REST_CLIENT), fqn)

        if (preparationRun) {
            return null
        }

        val filename = fqn.replace('.', '/') + ".java"
        return List.of(newArtifact(filename, createRestClient(refReg, view, pkg, baseName, className).getBytes("UTF-8"), view))
    }

    private def String createRestClient(CodeReferenceRegistry refReg, View view, String pkg, String baseName,
        String className) {

        val serviceName = baseName + "Service"
        val apiName = baseName + "ControllerApi"
        val ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("java.util.Objects")
        ctx.requiresImport("org.springframework.http.ResponseEntity")
        // Both contracts are generated into this same package, but go through the registry anyway so a
        // model that routes them elsewhere still resolves.
        ctx.requiresReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_SERVICE))
        ctx.requiresReference(TypeKeys.refKey(view, TypeKeys.JAVA_VIEW_REST_API_SPRING))

        val src = '''
            /**
             * Satisfies {@link «serviceName»} over HTTP, by wrapping the generated
             * {@code @HttpExchange} proxy for {@link «apiName»} and unwrapping its
             * {@link ResponseEntity}. Regenerated on every build.
             *
             * <p>A 404 is an answer here, not a failure: for an operation the model declares
             * {@code optional} it becomes an empty result, which is the same thing the service reports
             * when it runs in this JVM. Both a thrown {@code HttpClientErrorException.NotFound} and a
             * returned 404 status are handled, because which of the two a caller sees depends on how
             * the underlying client was configured.
             *
             * <p>Carries no bean-defining annotation on purpose: it is wired explicitly by whatever
             * application needs it, and must stay inert on a classpath that is scanned for beans.
             */
            public class «className» implements «serviceName» {

                private final «apiName» «TARGET»;

                /**
                 * Constructor with all mandatory dependencies.
                 *
                 * @param «TARGET» Proxy for the read model of another process.
                 */
                public «className»(final «apiName» «TARGET») {
                    this.«TARGET» = Objects.requireNonNull(«TARGET», "«TARGET»==null");
                }

                «FOR method : view.methods»
                    «new SrcViewMethod(ctx, method, "spring", ViewMethodShape.REST_CLIENT_DELEGATE, TARGET).toString»

                «ENDFOR»
            }
        '''
        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}
