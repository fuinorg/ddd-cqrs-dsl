package org.fuin.dsl.ddd.gen.entity

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ConstructorData
import org.fuin.dsl.ddd.gen.base.ConstructorParameter
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcChildEntityLocatorMethods
import org.fuin.dsl.ddd.gen.base.SrcConstructorWithDomainBody
import org.fuin.dsl.ddd.gen.base.SrcConstructorsWithParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcHandleEventMethods
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcMethods
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEntityExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import static extension org.fuin.dsl.ddd.gen.extensions.OperationContextExtensions.*

class FinalEntityArtifactFactory extends AbstractSource<Entity> {

    override getModelType() {
        typeof(Entity)
    }

    override getTypeKey() {
        TypeKeys.JAVA_ENTITY
    }

    override create(Entity entity, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = entity.getName()
        val pkg = entity.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(entity), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(entity)

        return List.of(newArtifact(filename,
            create(ctx, entity, pkg, className).toString().getBytes("UTF-8"), entity));
    }

    def addImports(CodeSnippetContext ctx) {
    }

    def addReferences(CodeSnippetContext ctx, Entity entity) {
        ctx.requiresReference(TypeKeys.refKey(entity, TypeKeys.JAVA_ENTITY_ABSTRACT))
    }

    def create(SimpleCodeSnippetContext ctx, Entity entity, String pkg, String className) {
        val String src = ''' 
            «new SrcJavaDocType(entity)»
            public final class «entity.name» extends Abstract«entity.name» {
            
                «IF entity.constructors.nullSafe.size == 0»
                    «new SrcConstructorsWithParamsAssignment(ctx, GenerateOptions.empty(), constructorData(entity, className))»
                «ELSE»
                    «FOR cd : constructorData(entity, className).indexed»
                        «new SrcConstructorWithDomainBody(ctx, GenerateOptions.empty(), cd.value, entity.constructors.get(cd.key))»

                    «ENDFOR»
                «ENDIF»
                «new SrcChildEntityLocatorMethods(ctx, GenerateOptions.empty(), entity)»
                «new SrcMethods(ctx, GenerateOptions.empty(), entity, false)»
                «new SrcHandleEventMethods(ctx, entity.allEvents)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

    def constructorData(Entity entity, String className) {
        val List<ConstructorData> constructors = new ArrayList<ConstructorData>()
        val rootParam = new ConstructorParameter(eINSTANCE.createParameter("The root aggregate of this entity.", entity.rootNullsafe, "rootAggregate", false), true)
        val idParam = new ConstructorParameter(eINSTANCE.createParameter("Unique entity identifier.", entity.idTypeNullsafe, "id", false), true)
        if (entity.constructors === null || entity.constructors.size == 0) {
            val List<ConstructorParameter> parameters = new ArrayList<ConstructorParameter>()
            parameters.add(rootParam)
            parameters.add(idParam)
            val ConstructorData cd = new ConstructorData("/** Constructor with mandatory data. */", null, "public", className, parameters, null)
            constructors.add(cd)
        } else {
            for (constructor : entity.constructors) {
                // Only the root aggregate and the id go to the abstract super class - it holds the
                // identity and nothing else. The modelled parameters stay with the final class.
                val ConstructorData cd = new ConstructorData("public", className, constructor, false)
                cd.prepend(idParam)
                cd.prepend(rootParam)
                // The constructor's operation context is the collaborator its body needs to verify a
                // business rule or fetch data, and goes last (see OperationContextExtensions).
                for (param : constructor.operationContextParameters) {
                    cd.append(new ConstructorParameter(param))
                }
                constructors.add(cd)
            }
        }
        return constructors
    }

}
