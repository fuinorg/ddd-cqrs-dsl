package org.fuin.dsl.ddd.gen.base

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Creates the body of a constructor or method of an aggregate or entity. Rather than a bare
 * "TODO Implement!" it lays out the three steps such a method always performs, filled in as far as
 * the model allows:
 * <ol>
 * <li><b>Check preconditions</b> - a real null check per mandatory parameter. A parameter declared
 * "optional" is legitimately nullable and is therefore skipped. Constraint validation is not
 * generated yet: enforcing a parameter level constraint needs a jakarta Validator handed into the
 * method, which is still to be designed.</li>
 * <li><b>Verify business constraints</b> - one entry per business rule declared on the method,
 * naming the exception it is expected to throw.</li>
 * <li><b>Apply events</b> - one commented builder stub per event the method may fire, to be
 * completed and handed to "apply(DomainEvent)" of AbstractAggregateRoot.</li>
 * </ol>
 * The event stubs are commented out on purpose: the builder generally needs values the generator
 * cannot know (the entity id path, or an attribute such as a "previous" value that is not a
 * parameter), so live code would not compile.
 */
class SrcDomainMethodBody implements CodeSnippet {

    val CodeSnippetContext ctx
    val List<Parameter> parameters
    val BusinessRules businessRules
    val List<Event> firedEvents

    /**
     * Constructor for a method.
     *
     * @param ctx Context.
     * @param method Method to create the body for.
     */
    new(CodeSnippetContext ctx, Method method) {
        this(ctx, method.parameters, method.businessRules, method.firedEvents)
    }

    /**
     * Constructor for a constructor.
     *
     * @param ctx Context.
     * @param constructor Constructor to create the body for.
     */
    new(CodeSnippetContext ctx, Constructor constructor) {
        this(ctx, constructor.parameters, constructor.businessRules, constructor.firedEvents)
    }

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param parameters Parameters to check.
     * @param businessRules Business rules to verify or <code>null</code>.
     * @param firedEvents Events that may be fired.
     */
    new(CodeSnippetContext ctx, List<Parameter> parameters, BusinessRules businessRules, List<Event> firedEvents) {
        this.ctx = ctx
        this.parameters = parameters
        this.businessRules = businessRules
        this.firedEvents = firedEvents
        if (mandatoryParameters.size > 0) {
            ctx.requiresImport("org.fuin.objects4j.common.Contract")
        }
    }

    /** Returns the parameters that cannot be null and therefore get a check. */
    private def getMandatoryParameters() {
        parameters.nullSafe.filter[optional === null].toList
    }

    private def getBusinessRuleInstances() {
        if (businessRules === null) {
            return newArrayList
        }
        return businessRules.businessRuleInstances.nullSafe
    }

    override toString() {
        '''
            // Check preconditions
            «IF mandatoryParameters.size == 0»
                // Nothing to check.
            «ELSE»
                «FOR parameter : mandatoryParameters»
                    Contract.requireArgNotNull("«parameter.name»", «parameter.name»);
                «ENDFOR»
            «ENDIF»

            // Verify business constraints
            «IF businessRuleInstances.size == 0»
                // None declared for this operation.
            «ELSE»
                «FOR instance : businessRuleInstances»
                    // TODO Verify "«instance.businessRule.name»" and throw «instance.businessRule.exception.name» if it is violated.
                «ENDFOR»
            «ENDIF»

            // Apply events
            «IF firedEvents.nullSafe.size == 0»
                // This operation fires no event.
            «ELSE»
                «FOR event : firedEvents.nullSafe»
                    // TODO apply(«event.name».builder()
                    //     ... set the event's attributes ...
                    //     .build());
                «ENDFOR»
            «ENDIF»
        '''
    }

}
