package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.cqrsDsl.Exception
import org.fuin.dsl.cqrs.cqrsDsl.InternalType
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.EventExtensions.*
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.cqrs.cqrsDsl.Command

/**
 * Creates source code for a one or more attribute declarations.
 */
class SrcVarsDecl implements CodeSnippet {

    val CodeSnippetContext ctx
    val GenerateOptions options
    val List<? extends Variable> attributes
    val boolean builderPopulated
    val boolean mandatoryAnnotated

    /**
     * Constructor with list of attributes.
     * 
     * @param ctx Context.
     * @param modifiers Modifiers for the attribute.
     * @param options Options to use.
     * @param attributes List.
     */
    new(CodeSnippetContext ctx, String modifiers, GenerateOptions options, List<? extends Variable> attributes) {
        this(ctx, modifiers, options, attributes, false)
    }

    /**
     * Constructor that allows marking the attributes as assigned by a builder.
     * 
     * @param ctx Context.
     * @param modifiers Modifiers for the attribute.
     * @param options Options to use.
     * @param attributes List.
     * @param builderPopulated TRUE if a builder assigns the attributes after construction.
     */
    new(CodeSnippetContext ctx, String modifiers, GenerateOptions options, List<? extends Variable> attributes,
        boolean builderPopulated) {
        this(ctx, modifiers, options, attributes, builderPopulated, false)
    }

    /**
     * Constructor that additionally allows stating mandatory attributes as a Bean Validation constraint.
     *
     * @param ctx Context.
     * @param modifiers Modifiers for the attribute.
     * @param options Options to use.
     * @param attributes List.
     * @param builderPopulated TRUE if a builder assigns the attributes after construction.
     * @param mandatoryAnnotated TRUE to emit {@code @NotNull} on every non-optional attribute.
     */
    new(CodeSnippetContext ctx, String modifiers, GenerateOptions options, List<? extends Variable> attributes,
        boolean builderPopulated, boolean mandatoryAnnotated) {
        this.ctx = ctx
        this.options = options
        this.attributes = new ArrayList<Variable>(attributes)
        this.builderPopulated = builderPopulated
        this.mandatoryAnnotated = mandatoryAnnotated
    }

    /**
     * Constructor with internal type.
     * 
     * @param ctx Context.
     * @param visibility Visibility for the attribute.
     * @param options Options to use.
     * @param internalType Type that has a list of attributes.
     */
    new(CodeSnippetContext ctx, String visibility, GenerateOptions options, InternalType internalType) {
        this(ctx, visibility, options, internalType.attributes)
    }

    /**
     * Constructor with event.
     * 
     * @param ctx Context.
     * @param visibility Visibility for the attribute.
     * @param options Options to use.
     * @param event Event that has a list of attributes.
     */
    new(CodeSnippetContext ctx, String visibility, GenerateOptions options, Event event) {
        this(ctx, visibility, options, event, false)
    }

    /**
     * Constructor with event that allows marking the attributes as assigned by a builder.
     * 
     * @param ctx Context.
     * @param visibility Visibility for the attribute.
     * @param options Options to use.
     * @param event Event that has a list of attributes.
     * @param builderPopulated TRUE if a builder assigns the attributes after construction.
     */
    new(CodeSnippetContext ctx, String visibility, GenerateOptions options, Event event, boolean builderPopulated) {
        this(ctx, visibility, options, event.eventVariables, builderPopulated, true);
    }

    /**
     * Constructor with command.
     * 
     * @param ctx Context.
     * @param visibility Visibility for the attribute.
     * @param options Options to use.
     * @param command Command that has a list of attributes.
     */
    new(CodeSnippetContext ctx, String visibility, GenerateOptions options, Command command) {
        this(ctx, visibility, options, command, false)
    }

    /**
     * Constructor with command that allows marking the attributes as assigned by a builder.
     * 
     * @param ctx Context.
     * @param visibility Visibility for the attribute.
     * @param options Options to use.
     * @param command Command that has a list of attributes.
     * @param builderPopulated TRUE if a builder assigns the attributes after construction.
     */
    new(CodeSnippetContext ctx, String visibility, GenerateOptions options, Command command, boolean builderPopulated) {
        this(ctx, visibility, options, command.commandVariables, builderPopulated, true);
    }

    /**
     * Constructor with exception.
     * 
     * @param ctx Context.
     * @param visibility Visibility for the attribute.
     * @param options Options to use.
     * @param exception Event that has a list of attributes.
     */
    new(CodeSnippetContext ctx, String visibility, GenerateOptions options, Exception ex) {
        this(ctx, visibility, options, ex.attributes)
    }

    override toString() {
        '''
            «FOR attribute : attributes.nullSafe»
                «new SrcVarDecl(ctx, "private", options, attribute, builderPopulated, mandatoryAnnotated)»

            «ENDFOR»
        '''
    }

}
