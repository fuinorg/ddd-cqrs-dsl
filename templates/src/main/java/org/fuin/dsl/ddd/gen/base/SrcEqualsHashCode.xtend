package org.fuin.dsl.ddd.gen.base

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*

/**
 * Creates {@code equals} and {@code hashCode} over all attributes of a value object or identifier.
 * <p>
 * A value object <em>is</em> its values: two instances carrying the same data have to be equal, or the type
 * behaves like a reference and every {@code Map} key, {@code Set} member, {@code List.contains} and
 * assertion built on it is silently wrong. A single-value object gets these methods from its base type
 * (see {@link SrcVoBaseMethodsString} and friends); a combined one - two or more attributes, or an
 * identifier that is a natural composite key - has no base to inherit them from, which is what this fills.
 * <p>
 * The methods are final: the abstract class owns the fields, so a subclass has nothing left to compare.
 */
class SrcEqualsHashCode implements CodeSnippet {

    val CodeSnippetContext ctx

    val String className

    val List<? extends Variable> attributes

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param className Name of the class the generated code compares against - the concrete type, so
     *                  {@code getClass()} equality is what it should be.
     * @param attributes Attributes that make up the value.
     */
    new(CodeSnippetContext ctx, String className, List<? extends Variable> attributes) {
        this.ctx = ctx
        this.className = className
        this.attributes = attributes
    }

    override toString() {
        if (attributes.nullSafe.empty) {
            return ""
        }
        ctx.requiresImport("java.util.Objects")
        '''
        @Override
        public final int hashCode() {
            return Objects.hash(«FOR attr : attributes SEPARATOR ', '»«attr.name»«ENDFOR»);
        }

        @Override
        public final boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final «className» other = («className») obj;
            return «FOR attr : attributes SEPARATOR "\n    && "»Objects.equals(«attr.name», other.«attr.name»)«ENDFOR»;
        }
        '''
    }

}
