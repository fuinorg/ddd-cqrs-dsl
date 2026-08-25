package org.fuin.dsl.ddd.gen.base

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import java.util.ArrayList
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRules
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.EventExtensions.*

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
 * completed and handed to "apply(DomainEvent)" of AbstractAggregateRoot. The stub spells out one
 * setter call per value the event carries, so completing it is mostly a matter of uncommenting:
 * a value the operation was given is passed straight through, and everything else is left as a
 * "..." placeholder.</li>
 * </ol>
 * The event stubs are commented out on purpose: the entity id path is mandatory on every domain
 * event but its expression differs (an aggregate root that already has its id, one that is just
 * being created, a child entity), and an attribute such as a "previous" value comes from state
 * rather than from a parameter. Live code would therefore not compile.
 */
class SrcDomainMethodBody implements CodeSnippet {

    /** Stands in for a value the generator cannot know. */
    static val String PLACEHOLDER = "..."

    val CodeSnippetContext ctx
    val List<Parameter> parameters
    val BusinessRules businessRules
    val List<Event> firedEvents
    val String entityIdPathArgument
    val String aggregateVersionArgument
    val String validatorCall

    /**
     * Constructor for a method.
     *
     * @param ctx Context.
     * @param method Method to create the body for.
     */
    new(CodeSnippetContext ctx, Method method) {
        this(ctx, method.parameters, method.businessRules, method.firedEvents,
            method.eContainer.entityIdPathArgument(false), method.eContainer.aggregateVersionArgument,
            validatorCall(method, method.businessRules, method.eContainer, false))
    }

    /**
     * Constructor for a constructor.
     *
     * @param ctx Context.
     * @param constructor Constructor to create the body for.
     */
    new(CodeSnippetContext ctx, Constructor constructor) {
        this(ctx, constructor.parameters, constructor.businessRules, constructor.firedEvents,
            constructor.eContainer.entityIdPathArgument(true), constructor.eContainer.aggregateVersionArgument,
            validatorCall(constructor, constructor.businessRules, constructor.eContainer, true))
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
        this(ctx, parameters, businessRules, firedEvents, PLACEHOLDER, PLACEHOLDER)
    }

    /**
     * Constructor with all mandatory data, including what to pass for the two inherited event values
     * the operation itself has to provide.
     *
     * @param ctx Context.
     * @param parameters Parameters to check.
     * @param businessRules Business rules to verify or <code>null</code>.
     * @param firedEvents Events that may be fired.
     * @param entityIdPathArgument Argument for the event's mandatory "entityIdPath".
     * @param aggregateVersionArgument Argument for the event's "aggregateVersion".
     */
    new(CodeSnippetContext ctx, List<Parameter> parameters, BusinessRules businessRules, List<Event> firedEvents,
        String entityIdPathArgument, String aggregateVersionArgument) {
        this(ctx, parameters, businessRules, firedEvents, entityIdPathArgument, aggregateVersionArgument, null)
    }

    /**
     * Constructor that also knows how to reach the generated validator.
     *
     * @param ctx Context.
     * @param parameters Parameters to check.
     * @param businessRules Business rules to verify or <code>null</code>.
     * @param firedEvents Events that may be fired.
     * @param entityIdPathArgument Argument for the event's mandatory "entityIdPath".
     * @param aggregateVersionArgument Argument for the event's "aggregateVersion".
     * @param validatorCall The one line that verifies everything this operation declares, or
     *                      <code>null</code> where the caller could not work out how to reach it.
     */
    new(CodeSnippetContext ctx, List<Parameter> parameters, BusinessRules businessRules, List<Event> firedEvents,
        String entityIdPathArgument, String aggregateVersionArgument, String validatorCall) {
        this.validatorCall = validatorCall
        this.ctx = ctx
        this.parameters = parameters
        this.businessRules = businessRules
        this.firedEvents = firedEvents
        this.entityIdPathArgument = entityIdPathArgument
        this.aggregateVersionArgument = aggregateVersionArgument
        if (mandatoryParameters.size > 0) {
            ctx.requiresImport("org.fuin.objects4j.common.Contract")
        }
    }

    /**
     * Returns what to pass for the event's mandatory entity id path. An aggregate method can use the
     * aggregate's own identifier and a constructor the one it was handed. A child entity needs the
     * path from the root down to itself, which the generator does not build to avoid an import in
     * commented-out code.
     *
     * @param owner Element declaring the operation - an aggregate, an entity or a service.
     * @param constructor TRUE for a constructor, FALSE for a method.
     *
     * @return Argument expression, or a placeholder.
     */
    private def static String entityIdPathArgument(EObject owner, boolean constructor) {
        if (owner instanceof Aggregate) {
            // The identity is known before the first event is applied - a creating constructor hands it
            // to the abstract super class - so every operation can read it the same way.
            return "getId()"
        }
        return PLACEHOLDER
    }

    /**
     * Returns what to pass for the event's aggregate version. Nothing else populates it - not
     * "apply(...)", not the repository - while the read model projects it, so the operation is the
     * only place it can come from.
     *
     * @param owner Element declaring the operation - an aggregate, an entity or a service.
     *
     * @return Argument expression, or a placeholder.
     */
    private def static String aggregateVersionArgument(EObject owner) {
        if (owner instanceof Aggregate) {
            return "getNextApplyVersion()"
        }
        if (owner instanceof Entity) {
            return "getRoot().getNextApplyVersion()"
        }
        return PLACEHOLDER
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

    /**
     * Returns what to pass to a builder setter for the given event variable. A parameter of this
     * operation with the same name is the value the fact is meant to carry, so it is used directly -
     * which is always the case for a "copies-attributes-of" event, whose variables <em>are</em> the
     * operation's parameters. Anything else (a "previous value" attribute, or state the aggregate
     * holds) is left as a placeholder, because the generator cannot know where it comes from.
     *
     * @param variable Event variable to pass a value for.
     *
     * @return Name of the matching parameter, or a placeholder.
     */
    private def String argumentFor(Variable variable) {
        for (parameter : parameters.nullSafe) {
            if (parameter.name == variable.name) {
                return parameter.name
            }
        }
        return "..."
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
            «ELSEIF validatorCall !== null»
                «validatorCall»
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
                    //     .entityIdPath(«entityIdPathArgument»)
                    //     .aggregateVersion(«aggregateVersionArgument»)
                    «FOR variable : event.eventVariables»
                        //     .«variable.name»(«argumentFor(variable)»)
                    «ENDFOR»
                    //     .build());
                «ENDFOR»
            «ENDIF»
        '''
    }


    /**
     * The one line that verifies everything an operation declares.
     *
     * <p>This is the whole contract between the write-once class and the generated validator: the
     * operation names its operation and nothing else. Every rule it verifies lives inside the generated
     * method, and the validator offers no way to hand it a rule from outside - so the model is the
     * complete list of what is enforced, and adding a rule to an operation never means editing this
     * file again.
     *
     * <p>A creating operation calls a static method, because there is no instance to hand over yet.
     */
    def private static String validatorCall(AbstractMethod operation, BusinessRules rules, EObject owner,
        boolean creating) {
        if (rules === null || rules.businessRuleInstances.nullSafe.empty || !(owner instanceof AbstractEntity)) {
            return null
        }
        val args = new ArrayList<String>
        for (parameter : operation.parameters.nullSafe) {
            args.add(parameter.name)
        }
        val service = operation.operationContext
        if (service !== null) {
            args.add(service.name.substring(0, 1).toLowerCase + service.name.substring(1))
        }
        val className = (owner as AbstractEntity).name + "Rules"
        val target = if(creating) className + "." else "new " + className + "(this)."
        return target + operation.name + "(" + args.join(", ") + ");"
    }

}
