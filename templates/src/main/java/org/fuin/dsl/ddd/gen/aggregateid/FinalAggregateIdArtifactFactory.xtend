package org.fuin.dsl.ddd.gen.aggregateid

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcConstructorsWithParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcIdStringMethods
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcVoBaseMethods
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext
import java.util.List

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

class FinalAggregateIdArtifactFactory extends AbstractSource<AggregateId> {

    override getModelType() {
        typeof(AggregateId)
    }

    override create(AggregateId aggregateId, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = aggregateId.name
        val abstractClassName = aggregateId.abstractName
        val pkg = aggregateId.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(aggregateId.uniqueName, fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(aggregateId)
        ctx.addReferences(aggregateId)

        return List.of(newArtifact(filename,
            create(ctx, aggregateId, pkg, className, abstractClassName).toString().getBytes("UTF-8"), aggregateId));
    }

    def addImports(CodeSnippetContext ctx, AggregateId aggregateId) {
        ctx.requiresImport("java.io.Serial")
        if (aggregateId.base !== null && options.jaxb) {
            // Only when JAXB was actually asked for - the annotation would otherwise drag
            // jakarta.xml.bind onto the classpath of a project that never enabled it.
            ctx.requiresImport("jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter")
        }
        ctx.requiresImport("javax.annotation.concurrent.Immutable")
    }

    def addReferences(CodeSnippetContext ctx, AggregateId aggregateId) {
        if (aggregateId.base !== null) {
            ctx.requiresReference(aggregateId.uniqueName + "Converter")
        }
        ctx.requiresReference(aggregateId.uniqueAbstractName)
    }

    def create(SimpleCodeSnippetContext ctx, AggregateId id, String pkg, String className, String abstractClassName) {
        val String src = ''' 
            «val idStrings = new SrcIdStringMethods(ctx, className, abstractClassName, id.attributes)»
            «new SrcJavaDocType(id)»
            @Immutable«IF id.base === null && idStrings.supported»
            «idStrings.annotations»«ENDIF»
            «IF id.base !== null && options.jaxb»
                @XmlJavaTypeAdapter(«id.name»Converter.class)
            «ENDIF»
            public final class «className» extends «abstractClassName» {
            
                @Serial
                private static final long serialVersionUID = 1000L;
                «IF id.base === null»
                    «idStrings.separatorConstant»
                «ENDIF»
                
                «new SrcConstructorsWithParamsAssignment(ctx, GenerateOptions.empty(), id, false, true)»
                «IF id.base === null»
                @Override
                public final String asString() {
                    «IF (id.attributes.nullSafe.size == 1)»
                        return "" + get«id.attributes.first.name.toFirstUpper»();
                    «ELSE»
                        // Default: the id parts joined by SEPARATOR. Override - together with valueOf
                        // below - if a different string form is required.
                        return «FOR a : id.attributes SEPARATOR ' + SEPARATOR + '»get«a.name.toFirstUpper»()«ENDFOR»;
                    «ENDIF»
                }

                «ENDIF»
                «new SrcVoBaseMethods(ctx, id)»
                «IF id.base === null»
                    «idStrings»
                «ENDIF»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}
