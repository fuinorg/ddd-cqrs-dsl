package org.fuin.dsl.ddd.gen.aggregate

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.ConstructorData
import org.fuin.dsl.ddd.gen.base.ConstructorParameter
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcChildEntityLocatorMethods
import org.fuin.dsl.ddd.gen.base.SrcConstructorSignature
import org.fuin.dsl.ddd.gen.base.SrcDomainMethodBody
import org.fuin.dsl.ddd.gen.base.SrcHandleEventMethods
import org.fuin.dsl.ddd.gen.base.SrcJavaDocMethod
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.SrcMethods
import org.fuin.dsl.ddd.gen.base.SrcParamsSuperCall
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAggregateExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.OperationContextExtensions.*
import java.util.ArrayList
import org.fuin.dsl.ddd.gen.base.TypeKeys
import java.util.List

class FinalAggregateArtifactFactory extends AbstractSource<Aggregate> {

    override getModelType() {
        return typeof(Aggregate)
    }

    override getTypeKey() {
        TypeKeys.JAVA_AGGREGATE
    }

    override create(Aggregate aggregate, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val className = aggregate.getName()
        val pkg = aggregate.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(aggregate), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports
        ctx.addReferences(aggregate)

        return List.of(newArtifact(filename,
            create(ctx, aggregate, pkg, className).toString().getBytes("UTF-8"), aggregate));
    }

    def addImports(CodeSnippetContext ctx) {
    }

    def addReferences(CodeSnippetContext ctx, Aggregate aggregate) {
        ctx.requiresReference(TypeKeys.refKey(aggregate, TypeKeys.JAVA_AGGREGATE_ABSTRACT))
    }

    def create(SimpleCodeSnippetContext ctx, Aggregate aggregate, String pkg, String className) {
        val String src = ''' 
            «new SrcJavaDocType(aggregate)»
            public final class «className» extends Abstract«aggregate.name» {
            
                /**
                 * Default constructor for loading the aggregate root from history. 
                 */
                public «aggregate.name»() {
                    super();
                }
            
                «FOR cd : constructorData(aggregate, className).indexed»
                    «new SrcJavaDocMethod(ctx, cd.value)»
                    «new SrcConstructorSignature(ctx, GenerateOptions.empty(), cd.value)» {
                        «new SrcParamsSuperCall(ctx, cd.value.superCallParameters)»

                        «new SrcDomainMethodBody(ctx, aggregate.constructors.get(cd.key))»
                    }

                «ENDFOR»
                «new SrcChildEntityLocatorMethods(ctx, GenerateOptions.empty(), aggregate)»
                «new SrcMethods(ctx, GenerateOptions.empty(), aggregate, false)»
                «new SrcHandleEventMethods(ctx, aggregate.allEvents)»
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

    /**
     * Creates the data for every modelled constructor, with the constructor's operation context
     * appended to its parameters - the collaborator the body needs to verify a business rule or fetch
     * data (see {@link OperationContextExtensions}).
     *
     * @param aggregate Aggregate to create the constructor data for.
     * @param className Name of the final class.
     *
     * @return One entry per modelled constructor, in declaration order.
     */
    def constructorData(Aggregate aggregate, String className) {
        val List<ConstructorData> constructors = new ArrayList<ConstructorData>()
        for (constructor : aggregate.constructors.nullSafe) {
            val ConstructorData cd = new ConstructorData("public", className, constructor)
            for (param : constructor.operationContextParameters) {
                cd.append(new ConstructorParameter(param))
            }
            // The aggregate's identifier comes first, exactly as a child entity's constructor takes the
            // root and its own id. It is not modelled as a parameter because it is not domain data the
            // operation decides on: every aggregate command carries the identifier of the aggregate it
            // addresses, and for a creating command that is the identifier of the aggregate to create.
            // Inventing one here instead would silently discard what the caller sent.
            cd.prepend(new ConstructorParameter(eINSTANCE.createParameter(
                "Unique aggregate identifier, as sent by the command.", aggregate.idTypeNullsafe, "id", false), true))
            constructors.add(cd)
        }
        return constructors
    }

    def _constructors(CodeSnippetContext ctx, Aggregate aggregate, String className) {
        '''
            «FOR constructor : aggregate.constructors.nullSafe»
                «new SrcJavaDocMethod(ctx, constructor)»
                «new SrcConstructorSignature(ctx, "public", className, GenerateOptions.empty(), constructor)» {
                    super();
                    // TODO Implement!
                }
                
            «ENDFOR»
        '''
    }

}
