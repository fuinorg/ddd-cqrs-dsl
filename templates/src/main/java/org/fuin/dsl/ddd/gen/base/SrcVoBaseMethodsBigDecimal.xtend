package org.fuin.dsl.ddd.gen.base

import org.fuin.dsl.cqrs.cqrsDsl.AbstractVO
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractVOExtensions.*

/**
 * Creates source code for value objects that have an external 'base' of type 'BigDecimal'.
 * <p>
 * A decimal cannot reuse {@link SrcVoBaseMethodsNumber}: that one converts a string with
 * '«baseName».valueOf(value)', and BigDecimal has no such method for a string - it takes the
 * constructor instead. There is also no 'AbstractBigDecimalValueObject' in objects4j, so unlike a
 * string or a long based value object, a decimal one gets 'getBaseType', 'hashCode' and 'equals' from
 * the generic part of {@link SrcVoBaseMethods} rather than from a base class.
 */
class SrcVoBaseMethodsBigDecimal implements CodeSnippet {

    val String typeName;

    val String baseName;

    /**
     * Constructor with value object.
     *
     * @param ctx Context.
     * @param vo Value object.
     */
    new(CodeSnippetContext ctx, AbstractVO vo) {
        if (vo === null) {
            throw new IllegalArgumentException("vo cannot be null")
        }
        if (vo.baseType === null) {
            throw new IllegalArgumentException("vo.base cannot be null")
        }
        this.typeName = vo.name
        this.baseName = vo.baseType.name
        ctx.requiresReference(TypeKeys.refKey(vo))
        ctx.requiresReference(TypeKeys.refKey(vo.baseType))
        ctx.requiresImport("org.jspecify.annotations.Nullable")
    }

    override toString() {
        '''
            /**
             * Returns the information if a given «baseName» can be converted into
             * an instance of «typeName». A <code>null</code> value returns <code>true</code>.
             *
             * @param value
             *            Value to check.
             *
             * @return TRUE if it's a valid «baseName», else FALSE.
             */
            public static boolean isValid(@Nullable final «baseName» value) {
                // Unlike a string, a «baseName» instance cannot be malformed - it was already parsed
                // when it was created. The method exists so a value object with a decimal base offers
                // the same contract as one with a string or an integral base.
                return true;
            }

            /**
             * Parses a given «baseName» and returns a new instance of «typeName».
             *
             * @param value
             *            Value to convert. A <code>null</code> value returns
             *            <code>null</code>.
             *
             * @return Converted value.
             */
            @Nullable
            public static «typeName» valueOf(@Nullable final «baseName» value) {
                if (value == null) {
                    return null;
                }
                return new «typeName»(value);
            }

            /**
             * Parses a given String and returns a new instance of «typeName».
             *
             * @param value
             *            Value to convert. A <code>null</code> value returns
             *            <code>null</code>.
             *
             * @return Converted value.
             *
             * @throws NumberFormatException The value is not a valid decimal number.
             */
            @Nullable
            public static «typeName» valueOf(@Nullable final String value) {
                if (value == null) {
                    return null;
                }
                return new «typeName»(new «baseName»(value));
            }

        '''
    }

}
