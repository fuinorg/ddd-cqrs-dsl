package org.fuin.dsl.ddd.gen.rule

import java.util.ArrayList
import java.util.LinkedHashSet
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.SrcParamsDecl
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*

/**
 * The class that verifies everything one aggregate or entity declares, one method per operation.
 *
 * <p><b>Nothing can be skipped, and that is the point.</b> The write-once operation names its
 * operation here and nothing else; every rule it verifies is inside the generated method, and there is
 * no method on this class that takes a rule from outside. The model is therefore the complete list of
 * what is enforced - which is the property the old arrangement lacked, where a rule was a comment and
 * the build stayed green whether or not anybody wrote the check.
 *
 * <p>It is a separate class rather than generated into the aggregate because the aggregate is written
 * once and never regenerated. Reading the state it needs is what the accessors on the abstract are for.
 *
 * <p><b>A creating operation gets a static method.</b> A constructor cannot hand over a fully
 * initialised instance and has no prior state to read, so rules on a create concern its arguments and
 * whatever the caller looked up - which is exactly what the parameters carry.
 */
class SrcRulesClass {

    val CodeSnippetContext ctx

    val AbstractEntity owner

    val String className

    /**
     * Constructor with the type whose rules these are.
     *
     * @param ctx Context to add imports to.
     * @param owner Aggregate or entity carrying the operations.
     * @param className Name of the generated class.
     */
    new(CodeSnippetContext ctx, AbstractEntity owner, String className) {
        this.ctx = ctx
        this.owner = owner
        this.className = className
    }

    /** The operations this class can verify something for; the rest need no method here. */
    def static List<AbstractMethod> guarded(AbstractEntity owner) {
        val out = new ArrayList<AbstractMethod>()
        for (constructor : owner.constructors.nullSafe) {
            if (!decidable(constructor).empty) {
                out.add(constructor)
            }
        }
        for (method : owner.methods.nullSafe) {
            if (!decidable(method).empty) {
                out.add(method)
            }
        }
        return out
    }

    /**
     * The rules of one operation this class verifies: those the model says something about.
     *
     * <p>A rule declaring a <code>requires</code> condition is generated whole. A rule declaring only
     * its attributes is a custom one - the class is written by hand, and the call is emitted anyway, so
     * that a rule nobody has written yet does not compile. That is on purpose: a stub would let a newly
     * declared rule look enforced while doing nothing, which is the failure this generator exists to
     * prevent.
     *
     * <p>A rule declaring <em>neither</em> is the one case left out. There is nothing to call - no class
     * and no constructor arguments - and nothing to write one from either, because the model has not yet
     * said what it decides from. It stays with the operation, which still carries its
     * <code>// TODO Verify</code> line, and starts being verified here the moment it declares anything.
     */
    def static List<BusinessRuleInstance> decidable(AbstractMethod operation) {
        val out = new ArrayList<BusinessRuleInstance>()
        for (instance : operation.businessRules?.businessRuleInstances.nullSafe) {
            val rule = instance.businessRule
            if (rule !== null && (rule.requires !== null || !rule.attributes.nullSafe.empty)) {
                out.add(instance)
            }
        }
        return out
    }

    override toString() {
        '''
        public final class «className» {

            private final «owner.name» self;

            /**
             * Constructor with the «owner.name» the rules are verified against.
             *
             * @param self What the rules read the current state from.
             */
            public «className»(final «owner.name» self) {
                Contract.requireArgNotNull("self", self);
                this.self = self;
            }
            «FOR operation : guarded(owner)»

            «method(operation)»
            «ENDFOR»
        }
        '''
    }

    def private method(AbstractMethod operation) {
        val creating = operation instanceof Constructor
        val parameters = new ArrayList<Parameter>(operation.parameters.nullSafe.toList)
        val service = operation.operationContext
        val serviceParam = if(service === null) null else serviceParameter(operation)
        val documented = new ArrayList<Parameter>(parameters)
        if (serviceParam !== null) {
            documented.add(serviceParam)
        }
        '''
        /**
         * Verifies everything «operation.name» declares.
         *
         «FOR parameter : documented»
         * @param «parameter.name» «parameter.superDocOrName»
         «ENDFOR»
         *
         * @throws «throwsOf(operation).join(" ")» One of the rules refused the operation.
         */
        «IF creating»static «ENDIF»void «operation.name»(«new SrcParamsDecl(ctx, GenerateOptions.empty(), parameters)»«IF serviceParam !== null»«IF !parameters.empty», «ENDIF»final «serviceType(service)» «serviceParam.name»«ENDIF») throws «throwsOf(operation).join(", ")» {
            «FOR instance : decidable(operation)»
            «new SrcRuleConstruction(instance, operation, if(creating) null else "self")»
            «ENDFOR»
        }
        '''
    }

    /** The refusals this operation can produce, each named once however many rules share it. */
    def private throwsOf(AbstractMethod operation) {
        val out = new LinkedHashSet<String>()
        for (instance : decidable(operation)) {
            ctx.requiresReference(TypeKeys.refKey(instance.businessRule.exception))
            ctx.requiresReference(TypeKeys.refKey(instance.businessRule))
            out.add(instance.businessRule.exception.name)
        }
        return out
    }

    /**
     * How this class names the operation's service.
     *
     * <p>A service declared inline in an operation is generated as a nested interface of the abstract
     * class, which only the abstract and its final subclass have in scope by its simple name - this
     * class extends neither, so it says which class the interface belongs to. Both live in the same
     * package, so nothing has to be imported. A service declared at module level is an ordinary
     * top-level type and is referenced as one.
     */
    def private String serviceType(Service service) {
        if (owner.services.nullSafe.contains(service)) {
            return owner.abstractName + "." + service.name
        }
        ctx.requiresReference(TypeKeys.refKey(service))
        return service.name
    }

    def private Parameter serviceParameter(AbstractMethod operation) {
        val service = operation.operationContext
        return eINSTANCE.createParameter("Answers what the operation cannot answer itself.", service,
            service.name.substring(0, 1).toLowerCase + service.name.substring(1), false)
    }

    def private static String superDocOrName(Parameter parameter) {
        return if(parameter.doc === null) parameter.name else parameter.doc.text
    }

}
