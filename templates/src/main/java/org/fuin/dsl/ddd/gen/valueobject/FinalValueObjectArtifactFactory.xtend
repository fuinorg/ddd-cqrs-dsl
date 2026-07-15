package org.fuin.dsl.ddd.gen.valueobject

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Namespace
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcConstructorsWithParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcMetaAnnotations
import org.fuin.dsl.ddd.gen.base.SrcVoBaseMethods
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import java.util.List
import org.fuin.dsl.ddd.gen.base.SrcXmlRootElement

class FinalValueObjectArtifactFactory extends AbstractSource<ValueObject> {

    override getModelType() {
        typeof(ValueObject)
    }

    override create(ValueObject vo, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = vo.name
        val abstractClassName = vo.abstractName
        val Namespace ns = vo.namespace;
        val pkg = vo.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(vo.uniqueName, fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }
        
        val active = context.get(CombinedAbstractValueObjectArtifactFactory.ACTIVE) === null ? false : context.get(CombinedAbstractValueObjectArtifactFactory.ACTIVE) as Boolean 
        
        if (active && vo.base !== null && vo.base.name == "String" && vo.attributes.size > 0) {
        	// In case CombinedAbstractValueObjectArtifactFactory is there an it's a simple string value object
        	// the SimpleStringValueObjectArtifactFactory will do the work
        	return List.of()
        }            
        

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addReferences(vo)
        ctx.requiresImport("java.io.Serial")

        return List.of(newArtifact(filename,
            create(ctx, ns, vo, pkg, className, abstractClassName).toString().getBytes("UTF-8"), vo));
    }

    def addReferences(CodeSnippetContext ctx, ValueObject vo) {
        ctx.requiresReference(vo.uniqueAbstractName)
    }

    def create(SimpleCodeSnippetContext ctx, Namespace ns, ValueObject vo, String pkg, String className, String abstractClassName) {
        val String src = ''' 
            «new SrcJavaDocType(vo)»
            «new SrcMetaAnnotations(ctx, vo.metaInfo, vo.context.name, (if (ns === null) className else ns.name + "." + className))»
            «IF vo.base === null && options.jaxb»
                «new SrcXmlRootElement(ctx, vo)»
            «ENDIF»
            public final class «className» extends «abstractClassName» {
            
                @Serial
                private static final long serialVersionUID = 1000L;
                
                «new SrcConstructorsWithParamsAssignment(ctx, GenerateOptions.empty(), vo, false, true)»
                «new SrcVoBaseMethods(ctx, vo)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}
