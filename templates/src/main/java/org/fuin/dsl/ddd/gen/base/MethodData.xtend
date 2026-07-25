package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.Collections
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Parameter
import org.fuin.dsl.cqrs.cqrsDsl.ReturnType

import static extension org.fuin.dsl.cqrs.extensions.CqrsMethodExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.OperationContextExtensions.*

/**
 * Data required to create a method. 
 */
class MethodData extends AbstractMethodData {

    val List<Parameter> parameters
    
    val boolean makeAbstract

    val ReturnType returnType

    /**
     * The model element the data was created from, or <code>null</code> when the data was assembled
     * from single values. Kept so a body generator can still reach what the flattened data drops -
     * the business rules and the events the method fires (see {@link SrcDomainMethodBody}).
     */
    var Method method

    /**
     * Constructor with method. The method's operation context is appended to the parameters, after
     * the modelled ones, so the generated operation can be handed the collaborator it needs to verify
     * a business rule or fetch data (see {@link OperationContextExtensions}).
     *
     * @param modifiers Modifiers (Don't include "abstract" - Use next argument instead).
     * @param makeAbstract Abstract method?
     * @param method Method.
     */
    new(String modifiers, boolean makeAbstract, Method method) {
        super(method.doc, modifiers, method.name, method.allExceptions)
        this.parameters = new ArrayList<Parameter>()
        this.parameters.addAll(method.parameters)
        this.parameters.addAll(method.operationContextParameters)
        this.makeAbstract = makeAbstract
        this.returnType = method.returnType
        this.method = method
    }

    /**
     * Returns the model element the data was created from.
     *
     * @return Method or <code>null</code> if the data was not created from one.
     */
    def getMethod() {
        method
    }

    /**
     * Constructor without annotations.
     * 
     * @param doc Documentation.
     * @param modifiers Modifiers (Don't include "abstract" - Use next argument instead).
     * @param makeAbstract Abstract method?
     * @param returnType Return type.
     * @param methodName Name of the method.
     * @param parameters Parameters for the method.
     * @param exceptions Exceptions for the method.
     */
    new(String doc, String modifiers, boolean makeAbstract, ReturnType returnType, String methodName, List<Parameter> parameters, List<Exception> exceptions) {
        super(doc, null, modifiers, methodName, exceptions)
        this.parameters = new ArrayList<Parameter>()
        this.parameters.addAll(parameters)
        this.makeAbstract = makeAbstract
        this.returnType = returnType
    }

    /**
     * Constructor with all data.
     * 
     * @param doc Documentation.
     * @param annotations Annotations.
     * @param modifiers Modifiers (Don't include "abstract" - Use next argument instead).
     * @param makeAbstract Abstract method?
     * @param returnType Return type.
     * @param methodName Name of the method.
     * @param parameters Parameters for the method.
     * @param exceptions Exceptions for the method.
     */
    new(String doc, List<String> annotations, String modifiers, boolean makeAbstract, ReturnType returnType, String methodName, List<Parameter> parameters, List<Exception> exceptions) {
        super(doc, annotations, modifiers, methodName, exceptions)
        this.parameters = new ArrayList<Parameter>()
        this.parameters.addAll(parameters)
        this.makeAbstract = makeAbstract
        this.returnType = returnType
    }
    
    /**
     * Returns if the method is abstract.
     * 
     * @return TRUE if the method is abstract.
     */
    def isMakeAbstract() {
        makeAbstract
    }

    /**
     * Returns the result type of a method.
     * 
     * @return Type returned by a method.
     */
    def getReturnType() {
        returnType
    }

    override List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters)
    }

    /**
     * Inserts a new parameter at the first position.
     * 
     * @param parameter Parameter to add.
     */
    final def prepend(Parameter parameter) {
        parameters.add(0, parameter)
    }

    /**
     * Appends a new parameter at the last position.
     * 
     * @param parameter Parameter to add.
     */
    final def append(Parameter parameter) {
        parameters.add(parameter)
    }


}
