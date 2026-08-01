package org.fuin.dsl.ddd.gen.entity

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ConstructorData
import org.fuin.dsl.ddd.gen.base.ConstructorParameter
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAbstractChildEntityLocatorMethods
import org.fuin.dsl.ddd.gen.base.SrcAbstractHandleEventMethods
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcConstructorsWithParamsAssignment
import org.fuin.dsl.ddd.gen.base.SrcGetters
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcSetters
import org.fuin.dsl.ddd.gen.base.SrcVarDecl
import org.fuin.dsl.ddd.gen.base.SrcVarsDecl
import org.fuin.dsl.ddd.gen.service.SrcServices
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAggregateExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEntityExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.dsl.ddd.gen.base.SrcMethods

class AbstractEntityArtifactFactory extends AbstractSource<Entity> {

    override getModelType() {
        typeof(Entity)
    }

    override getTypeKey() {
        TypeKeys.JAVA_ENTITY_ABSTRACT
    }

    override create(Entity entity, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = entity.abstractName
        val pkg = entity.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(entity, TypeKeys.JAVA_ENTITY_ABSTRACT), fqn)
        // A service declared inline in a constructor or method is generated as a nested interface of
        // this class (see SrcServices below), and ServiceArtifactFactory deliberately creates no
        // top-level file for it. An operation referencing such a service takes it as a parameter, and
        // the only classes that do so are this one and the final subclass - both have the nested type
        // in scope by its simple name. It is therefore registered unqualified: no import is possible
        // (nor needed), and SrcImports drops a reference without a package.
        for (service : entity.services.nullSafe) {
            refReg.putReference(TypeKeys.refKey(service), service.name)
        }

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(entity)

        val idVar = eINSTANCE.createAttribute(null, entity.idTypeNullsafe, "id", false)

        return List.of(newArtifact(filename,
            create(ctx, entity, pkg, className, idVar).toString().getBytes("UTF-8"), entity));
    }

    def addImports(CodeSnippetContext ctx) {
        ctx.requiresImport("org.fuin.ddd4j.core.AbstractEntity")
        ctx.requiresImport("org.fuin.ddd4j.core.EntityType")
    }

    def addReferences(CodeSnippetContext ctx, Entity entity) {
        ctx.requiresReference(TypeKeys.refKey(entity.idTypeNullsafe))
        ctx.requiresReference(TypeKeys.refKey(entity.rootNullsafe))
        ctx.requiresReference(TypeKeys.refKey(entity.rootNullsafe.idTypeNullsafe))
    }

    def create(SimpleCodeSnippetContext ctx, Entity entity, String pkg, String className, Attribute idVar) {
        val String src = ''' 
            «new SrcJavaDocType(entity)»
            public abstract class «className» extends AbstractEntity<«entity.rootNullsafe.idTypeNullsafe.name», «entity.rootNullsafe.name», «entity.
                idTypeNullsafe.name»> {
            
                «new SrcVarDecl(ctx, "private", GenerateOptions.empty(), idVar)»

                «new SrcConstructorsWithParamsAssignment(ctx, GenerateOptions.empty(), constructorData(entity, className))»
                @Override
                public final EntityType getType() {
                    return «entity.idTypeNullsafe.name».TYPE;
                }

                @Override
                public final «entity.idTypeNullsafe.name» getId() {
                    return id;
                }

                «new SrcAbstractChildEntityLocatorMethods(ctx, GenerateOptions.empty(), entity)»
                «new SrcAbstractHandleEventMethods(ctx, entity.allEvents)»
                «new SrcServices(ctx, entity.services)»
                «new SrcMethods(ctx, GenerateOptions.empty(), entity, true)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

    /**
     * The abstract entity holds the identity only, so it needs exactly one constructor taking the
     * root aggregate and the entity id - regardless of what the model declares. The modelled
     * constructors are generated on the final class, which owns whatever state it decides to keep.
     */
    def constructorData(Entity entity, String className) {
        val List<ConstructorData> constructors = new ArrayList<ConstructorData>()
        val rootParam = new ConstructorParameter(eINSTANCE.createParameter("The root aggregate of this entity.", entity.rootNullsafe, "rootAggregate", false), true)
        val idParam = new ConstructorParameter(eINSTANCE.createParameter("Unique entity identifier.", entity.idTypeNullsafe, "id", false))
        val List<ConstructorParameter> parameters = new ArrayList<ConstructorParameter>()
        parameters.add(rootParam)
        parameters.add(idParam)
        constructors.add(new ConstructorData("/** Constructor with mandatory data. */", null, "protected", className, parameters, null))
        return constructors
    }

}
