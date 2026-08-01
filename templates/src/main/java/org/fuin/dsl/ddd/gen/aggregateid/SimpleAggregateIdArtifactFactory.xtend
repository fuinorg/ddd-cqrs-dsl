package org.fuin.dsl.ddd.gen.aggregateid

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AggregateId
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAggregateIdExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import java.util.List
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.dsl.ddd.gen.base.SrcMetaAnnotations

class SimpleAggregateIdArtifactFactory extends AbstractSource<AggregateId> {

    override getModelType() {
        typeof(AggregateId)
    }

    override getTypeKey() {
        TypeKeys.JAVA_AGGREGATE_ID
    }

    override create(AggregateId aggregateId, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (aggregateId.base === null || aggregateId.base.name != "UUID") {
            // Do not generate anything
            return null
        }

        val className = aggregateId.getName()
        val Module ns = aggregateId.module
        val pkg = aggregateId.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(aggregateId), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(aggregateId)
        ctx.addReferences(aggregateId)

        return List.of(newArtifact(filename,
            create(ctx, ns, aggregateId, pkg, className).toString().getBytes("UTF-8"), aggregateId));
    }

    def addImports(CodeSnippetContext ctx, AggregateId aggregateId) {
        ctx.requiresImport("jakarta.validation.Constraint")
        ctx.requiresImport("jakarta.validation.ConstraintValidator")
        ctx.requiresImport("jakarta.validation.ConstraintValidatorContext")
        ctx.requiresImport("jakarta.validation.Payload")
        ctx.requiresImport("org.jspecify.annotations.Nullable")
        ctx.requiresImport("java.io.Serial");
        ctx.requiresImport("java.lang.annotation.Documented")
        ctx.requiresImport("java.lang.annotation.ElementType")
        ctx.requiresImport("jakarta.annotation.Generated")
        ctx.requiresImport("java.lang.annotation.Retention")
        ctx.requiresImport("java.lang.annotation.RetentionPolicy")
        ctx.requiresImport("java.lang.annotation.Target")
        ctx.requiresImport("org.fuin.ddd4j.core.AggregateRootUuid")
        ctx.requiresImport("org.fuin.ddd4j.core.EntityType")
        ctx.requiresImport("org.fuin.ddd4j.core.StringBasedEntityType")
        ctx.requiresImport("org.fuin.ddd4j.core.HasEntityTypeConstant")
        ctx.requiresImport("org.fuin.objects4j.common.HasPublicStaticIsValidMethod")
        ctx.requiresImport("org.fuin.objects4j.common.HasPublicStaticValueOfMethod")
        ctx.requiresImport("org.fuin.objects4j.common.ConstraintViolationException");
        ctx.requiresImport("javax.annotation.concurrent.Immutable")
        ctx.requiresImport("java.util.UUID");
        if (options.jsonb) {
            ctx.requiresImport("jakarta.json.bind.adapter.JsonbAdapter")        
        }
        if (options.jpa) {
            ctx.requiresImport("jakarta.persistence.AttributeConverter")        
        }        
        if (options.jaxb) {
            ctx.requiresImport("jakarta.xml.bind.annotation.adapters.XmlAdapter")        
        }
    }

    def addReferences(CodeSnippetContext ctx, AggregateId entityId) {
        // Do nothing
    }

    def create(SimpleCodeSnippetContext ctx, Module ns, AggregateId id, String pkg, String className) {
        val src = '''
            «new SrcJavaDocType(id)»
            «new SrcMetaAnnotations(ctx, id.metaInfo, bundleName(ns), className)»
            @Generated("Generated class - Manual changes will be overwritten")
            @Immutable
            @HasEntityTypeConstant
            @HasPublicStaticIsValidMethod
            @HasPublicStaticValueOfMethod
            public final class «className» extends AggregateRootUuid {
            
                @Serial
                private static final long serialVersionUID = 1000L;

                /** Unique name of the aggregate this identifier refers to. */
                public static final EntityType TYPE = new StringBasedEntityType("«id.aggregateNullsafe.name»");
            
                /**
                 * Default constructor.
                 */
                @SuppressWarnings("NullAway.Init")
                protected «className»() {
                    super(TYPE);
                }
            
                /**
                 * Constructor with all data.
                 *
                 * @param value
                 *            Persistent value.
                 */
                public «className»(final UUID value) {
                    super(TYPE, value);
                }
            
                /**
                 * Parses a given string and returns a new instance of «className».
                 * 
                 * @param value
                 *            String with valid UUID to convert. A {@literal null} value
                 *            returns {@literal null}.
                 * 
                 * @return Converted value.
                 */
                @Nullable
                public static «className» valueOf(@Nullable final String value) {
                    if (value == null) {
                        return null;
                    }
                    requireArgValid("value", value);
                    return new «className»(UUID.fromString(value));
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
                    return AggregateRootUuid.isValid(value);
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
                public @interface «className»Str {
            
                    String message()
            
                    default "{«pkg».«className».message}";
            
                    Class<?>[] groups() default {};
            
                    Class<? extends Payload>[] payload() default {};
            
                }

                /**
                 * Validates if a string is compliant with the type.
                 */
                public static final class Validator implements ConstraintValidator<«className»Str, String> {
            
                    @Override
                    public void initialize(final «className»Str annotation) {
                        // Not used
                    }
            
                    @Override
                    public boolean isValid(final String value, final ConstraintValidatorContext context) {
                        return «className».isValid(value);
                    }
            
                }
                
            
                «IF options.jaxb || options.jsonb || options.jpa»
                /**
                 * Converts the value object from/to UUID.
                 */
                public static final class Converter «IF options.jaxb»extends XmlAdapter<UUID, «className»> «ENDIF»«IF options.jpa && options.jsonb»implements AttributeConverter<«className», UUID>, JsonbAdapter<«className», UUID>«ELSE»«IF options.jsonb»implements JsonbAdapter<«className», UUID>«ENDIF»«IF options.jpa»implements AttributeConverter<«className», UUID>«ENDIF»«ENDIF» {

                    // General methods

                    /**
                     * Converts the UUID into a «className». A {@literal null} parameter will return {@literal null}.
                     * 
                     * @param value
                     *            UUID to convert into a «className».
                     * 
                     * @return Value object of type «className».
                     */
                    @Nullable
                    public «className» toVO(@Nullable final UUID value) {
                        if (value == null) {
                            return null;
                        }
                        return new «className»(value);
                    }

                    /**
                     * Converts a «className» into a UUID. A {@literal null} parameter will return {@literal null}.
                     * 
                     * @param value
                     *            Value object of type «className».
                     * 
                     * @return UUID.
                     */
                    @Nullable
                    public UUID fromVO(@Nullable final «className» value) {
                        if (value == null) {
                            return null;
                        }
                        return value.asBaseType();
                    }

                    «IF options.jaxb»
                    // JAXB XML Adapter

                    @Override
                    @Nullable
                    public «className» unmarshal(@Nullable final UUID value) throws Exception {
                        return toVO(value);
                    }

                    @Override
                    @Nullable
                    public UUID marshal(@Nullable final «className» obj) throws Exception {
                        return fromVO(obj);
                    }

                    «ENDIF»
                    «IF options.jpa»
                    // JPA Attribute Converter

                    @Override
                    @Nullable
                    public UUID convertToDatabaseColumn(@Nullable final «className» obj) {
                        return fromVO(obj);
                    }

                    @Override
                    @Nullable
                    public «className» convertToEntityAttribute(@Nullable final UUID value) {
                        return toVO(value);
                    }

                    «ENDIF»
                    «IF options.jsonb»
                    // JSONB Adapter

                    @Override
                    @Nullable
                    public UUID adaptToJson(@Nullable final «className» obj) throws Exception {
                        return fromVO(obj);
                    }

                    @Override
                    @Nullable
                    public «className» adaptFromJson(@Nullable final UUID value) throws Exception {
                        return toVO(value);
                    }

                    «ENDIF»
                }
                «ENDIF»
            
            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}
