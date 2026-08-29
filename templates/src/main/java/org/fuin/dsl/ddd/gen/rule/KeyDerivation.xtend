package org.fuin.dsl.ddd.gen.rule

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Entity
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.Key
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.dsl.cqrs.cqrsDsl.Service
import org.fuin.dsl.cqrs.cqrsDsl.Type
import org.eclipse.xtext.EcoreUtil2
import org.fuin.srcgen4j.commons.GenerateException

import static org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory.eINSTANCE

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsDslFactoryExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsKeyExtensions.*

/**
 * What a business key stands for once it is generated: a uniqueness rule, the service method that
 * answers whether the key is taken, and what each operation hands them.
 *
 * <p>All of it is built out of model objects the templates already know how to render, so a derived
 * rule is rendered by the same code as a declared one and cannot drift away from it. Only two objects
 * are new - the rule's <code>Boolean</code> answer and the service method - and both point at types the
 * model really declares, because a type is rendered by looking its Java name up in the code reference
 * registry and nothing detached is in there.
 *
 * <p><b>Only a key that refuses generates anything.</b> <code>overwrite</code> and <code>skip</code>
 * say what the handler does with a second occurrence; neither refuses an operation, so neither has a
 * rule to verify.
 */
class KeyDerivation {

    /**
     * The <code>Boolean</code> the derived rule's answer is typed as.
     *
     * <p>Taken from the model rather than made up: a type is rendered from what the code reference
     * registry says its Java name is, and only a declared type is in there. Which context declared it
     * does not matter - every one of them maps to the same Java class.</p>
     *
     * @param key Key being generated.
     *
     * @return The declared Boolean type.
     */
    def static Type booleanType(Key key) throws GenerateException {
        val resourceSet = key.eResource?.resourceSet
        if (resourceSet !== null) {
            for (resource : resourceSet.resources) {
                val found = resource.allContents.filter(ExternalType).findFirst[name == "Boolean"]
                if (found !== null) {
                    return found
                }
            }
        }
        throw new GenerateException("'" + key.name + "' derives a rule answering whether the key is "
            + "taken, and no 'Boolean' type is declared anywhere in the model to type that answer with")
    }

    /**
     * What the derived rule decides from: whether the key is taken, then the key itself.
     *
     * <p>The key attributes are the type's own, unchanged, which is what lets the refusal name what it
     * refused - an exception asking for <code>name</code> is answered by the attribute called
     * <code>name</code>, exactly as it is for a declared rule.</p>
     *
     * @param key Key being generated.
     *
     * @return Attributes of the derived rule, the answer first.
     */
    def static List<Attribute> derivedAttributes(Key key) throws GenerateException {
        val out = new ArrayList<Attribute>()
        out.add(eINSTANCE.createAttribute("Whether something already holds the key, as the caller found out.",
            key.booleanType, key.answerName, false))
        out.addAll(key.keyAttributes)
        return out
    }

    /**
     * The service method that answers whether the key is taken.
     *
     * <p>Declared on the operation's own context service, because the answer is a question about
     * everything else of its kind and a rule never reaches for that itself. An operation that edits
     * also hands over the carrier's identity: renaming something to the name it already carries is a
     * repetition rather than a collision, and without the identity there is nothing to exclude it by.</p>
     *
     * @param key Key being checked.
     * @param operation Operation checking it.
     *
     * @return Method to declare on the operation's context service.
     */
    def static Method derivedServiceMethod(Key key, AbstractMethod operation) throws GenerateException {
        val method = eINSTANCE.createMethod
        method.name = key.serviceMethodName
        method.doc = "Returns whether anything else already holds " + key.name
            + (if (operation instanceof Constructor) "." else ", the carrier itself excepted.")
        for (attribute : key.keyAttributes) {
            method.parameters.add(eINSTANCE.createParameter(
                "The " + attribute.name + " to check.", attribute.type, attribute.name, false))
        }
        if (!(operation instanceof Constructor)) {
            val idType = key.carrierIdType
            method.parameters.add(eINSTANCE.createParameter(
                "The one being changed, whose own key is not a collision.", idType,
                idType.name.toFirstLower, false))
        }
        val returns = eINSTANCE.createReturnType
        returns.doc = "True when it is already held."
        returns.type = key.booleanType
        method.returnType = returns
        return method
    }

    /**
     * The methods a service declares because the operation it serves checks a key.
     *
     * <p>Only for a usage that names the key and stops. A usage that writes its actuals out names the
     * service method it calls, so there is nothing left to derive - and deriving one anyway would put a
     * second, unused method on the interface somebody has to implement.</p>
     *
     * @param service Service declared as an operation's context.
     *
     * @return Methods to add to the interface, empty where the operation checks no key.
     */
    def static List<Method> derivedMethods(Service service) throws GenerateException {
        val out = new ArrayList<Method>()
        val operation = EcoreUtil2.getContainerOfType(service, AbstractMethod)
        if (operation === null || operation.operationContext !== service) {
            return out
        }
        for (instance : operation.businessRules?.businessRuleInstances.nullSafe) {
            val rule = instance.businessRule
            if (rule instanceof Key && instance.params.nullSafe.empty) {
                out.add((rule as Key).derivedServiceMethod(operation))
            }
        }
        return out
    }

    /**
     * The identifier of the type the key is declared on, which excludes the carrier from its own check.
     *
     * @param key Key being checked.
     *
     * @return Identifier type of the declaring aggregate or entity.
     */
    def static Type carrierIdType(Key key) throws GenerateException {
        val owner = key.eContainer
        val idType = switch (owner) {
            Aggregate: owner.idType
            Entity: owner.idType
            default: null
        }
        if (idType === null) {
            throw new GenerateException("'" + key.name + "' is checked by an operation that edits, which "
                + "excludes the carrier from its own check, and " + (owner as AbstractEntity)?.name
                + " declares no identifier to exclude it by")
        }
        return idType
    }

    /**
     * What one operation hands the derived rule: the service call answering it, then the key's values.
     *
     * <p>A key attribute is read off the operation's parameter of its type, and off the carrier where
     * there is none. Bound by type rather than by name because an operation that edits names its
     * parameter after the change - a <code>rename</code> takes a <code>newName</code> - so a name match
     * would bind nothing on the operations where this matters most. The build refuses the two cases
     * that cannot be worked out, so nothing here guesses.</p>
     *
     * @param key Key being checked.
     * @param operation Operation checking it.
     * @param serviceVariable Name of the parameter holding the operation's context service.
     * @param carrier Expression the carrier's own state is read from, or <code>null</code> for a
     *            creating operation, which has none.
     *
     * @return Java expressions, in the order the derived rule takes them.
     */
    def static List<String> actuals(Key key, AbstractMethod operation, String serviceVariable, String carrier)
            throws GenerateException {
        val values = new ArrayList<String>()
        for (attribute : key.keyAttributes) {
            values.add(valueOf(key, attribute, operation, carrier))
        }
        val call = new ArrayList<String>(values)
        if (carrier !== null) {
            call.add(carrier + ".getId()")
        }
        val out = new ArrayList<String>()
        out.add(serviceVariable + "." + key.serviceMethodName + "(" + call.join(", ") + ")")
        out.addAll(values)
        return out
    }

    def private static String valueOf(Key key, Attribute attribute, AbstractMethod operation, String carrier)
            throws GenerateException {
        val Parameter bound = key.boundParameter(attribute, operation)
        if (bound !== null) {
            return bound.name
        }
        if (carrier === null) {
            throw new GenerateException(operation.name + " creates, so it has no prior state to read '"
                + attribute.name + "' from, and takes no parameter of its type")
        }
        return carrier + ".get" + attribute.name.toFirstUpper + "()"
    }

    def private static String toFirstUpper(String value) {
        return value.substring(0, 1).toUpperCase + value.substring(1)
    }

    def private static String toFirstLower(String value) {
        return value.substring(0, 1).toLowerCase + value.substring(1)
    }

}
