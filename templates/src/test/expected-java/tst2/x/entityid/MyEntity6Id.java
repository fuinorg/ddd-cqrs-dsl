/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package p.shared.domain.x.entityid;

import jakarta.annotation.Generated;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.persistence.AttributeConverter;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.io.Serial;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;
import javax.annotation.concurrent.Immutable;
import org.fuin.ddd4j.core.EntityType;
import org.fuin.ddd4j.core.HasEntityTypeConstant;
import org.fuin.ddd4j.core.StringBasedEntityType;
import org.fuin.ddd4j.core.UuidEntityId;
import org.fuin.objects4j.common.ConstraintViolationException;
import org.fuin.objects4j.common.HasPublicStaticIsValidMethod;
import org.fuin.objects4j.common.HasPublicStaticValueOfMethod;
import org.jspecify.annotations.Nullable;

/**
 * Entity ID no attribute and base UUID.
 */
@Generated("Generated class - Manual changes will be overwritten")
@Immutable
@HasEntityTypeConstant
@HasPublicStaticIsValidMethod
@HasPublicStaticValueOfMethod
public final class MyEntity6Id extends UuidEntityId {

    @Serial
    private static final long serialVersionUID = 1000L;

    /** Unique name of the aggregate this identifier refers to. */
    public static final EntityType TYPE = new StringBasedEntityType("MY_ENTITY6");

    /**
     * Default constructor.
     */
    @SuppressWarnings("NullAway.Init")
    protected MyEntity6Id() {
        super(TYPE);
    }

    /**
     * Constructor with mandatory data.
     *
     * @param value
     *            Persistent value.
     */
    public MyEntity6Id(final UUID value) {
        super(TYPE, value);
    }

    /**
     * Parses a given string and returns a new instance of MyEntity6Id.
     * 
     * @param value
     *            String with valid UUID to convert. A {@literal null} value
     *            returns {@literal null}.
     * 
     * @return Converted value.
     */
    @Nullable
    public static MyEntity6Id valueOf(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        requireArgValid("value", value);
        return new MyEntity6Id(UUID.fromString(value));
    }

    /**
     * Verifies that a given string can be converted into the type.
     * 
     * @param value
     *            Value to validate.
     * 
     * @return Returns {@literal true} if it's a valid type else {@literal false}.
     */
    public static boolean isValid(final String value) {
        if (value == null) {
            return true;
        }
        return UuidEntityId.isValid(value);
    }

    /**
     * Verifies if the argument is valid and throws an exception if this is not the case.
     * 
     * @param name
     *            Name of the value for a possible error message.
     * @param value
     *            Value to check.
     * 
     * @throws ConstraintViolationException
     *             The value was not valid.
     */
    public static void requireArgValid(final String name, final String value) throws ConstraintViolationException {
        if (!isValid(value)) {
            throw new ConstraintViolationException("The argument '" + name + "' is not valid: '" + value + "'");
        }
    }

    /**
     * Ensures that the string can be converted into the type.
     */
    @Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = { Validator.class })
    @Documented
    public @interface MyEntity6IdStr {

        String message()

        default "{p.shared.domain.x.entityid.MyEntity6Id.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    /**
     * Validates if a string is compliant with the type.
     */
    public static final class Validator implements ConstraintValidator<MyEntity6IdStr, String> {

        @Override
        public void initialize(final MyEntity6IdStr annotation) {
            // Not used
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext context) {
            return MyEntity6Id.isValid(value);
        }

    }
    

    /**
     * Converts the value object from/to UUID.
     */
    public static final class Converter extends XmlAdapter<UUID, MyEntity6Id> implements AttributeConverter<MyEntity6Id, UUID>, JsonbAdapter<MyEntity6Id, UUID> {

        // General methods

        /**
         * Converts the UUID into a MyEntity6Id. A {@literal null} parameter will return {@literal null}.
         * 
         * @param value
         *            UUID to convert into a MyEntity6Id.
         * 
         * @return Value object of type MyEntity6Id.
         */
        @Nullable
        public MyEntity6Id toVO(@Nullable final UUID value) {
            if (value == null) {
                return null;
            }
            return new MyEntity6Id(value);
        }

        /**
         * Converts a MyEntity6Id into a UUID. A {@literal null} parameter will return {@literal null}.
         * 
         * @param value
         *            Value object of type MyEntity6Id.
         * 
         * @return UUID.
         */
        @Nullable
        public UUID fromVO(@Nullable final MyEntity6Id value) {
            if (value == null) {
                return null;
            }
            return value.asBaseType();
        }

        // JAXB XML Adapter

        @Override
        @Nullable
        public MyEntity6Id unmarshal(@Nullable final UUID value) throws Exception {
            return toVO(value);
        }

        @Override
        @Nullable
        public UUID marshal(@Nullable final MyEntity6Id obj) throws Exception {
            return fromVO(obj);
        }

        // JPA Attribute Converter

        @Override
        @Nullable
        public UUID convertToDatabaseColumn(@Nullable final MyEntity6Id obj) {
            return fromVO(obj);
        }

        @Override
        @Nullable
        public MyEntity6Id convertToEntityAttribute(@Nullable final UUID value) {
            return toVO(value);
        }

        // JSONB Adapter

        @Override
        @Nullable
        public UUID adaptToJson(@Nullable final MyEntity6Id obj) throws Exception {
            return fromVO(obj);
        }

        @Override
        @Nullable
        public MyEntity6Id adaptFromJson(@Nullable final UUID value) throws Exception {
            return toVO(value);
        }

    }

}
