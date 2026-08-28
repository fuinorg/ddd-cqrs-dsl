package org.fuin.dsl.ddd.gen.entityid

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.PathSegment
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcJavaDocType
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Generates the type for a declared entity identifier path.
 *
 * <p>A child of a root there are many of cannot be addressed by its own identifier - 'TRANSACTION 45'
 * exists in every account-year - so it travels as a path. Untyped, that path says nothing about what it
 * addresses, and an attribute holding one is named after a type it does not hold. This turns the declared
 * shape into a Java type, so an attribute's type states what it addresses and a wrong path is refused
 * where it is built rather than wherever it is later read.
 *
 * <p>The shape itself is a constant rather than generated code: <code>EntityIdPathSpec</code> in ddd-4-java
 * does the matching, and the same class backs the <code>&#64;ExpectedEntityIdPath</code> annotation, so a
 * path checked here and a path checked by Bean Validation cannot disagree.
 */
class EntityIdPathArtifactFactory extends AbstractSource<EntityIdPathType> {

    override getModelType() {
        typeof(EntityIdPathType)
    }

    override getTypeKey() {
        TypeKeys.JAVA_ENTITY_ID_PATH
    }

    override create(EntityIdPathType path, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val className = path.name
        val Module ns = path.module
        val pkg = path.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java"
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(path), fqn)

        if (preparationRun) {
            // Only the reference is registered; nothing is written until every type is known.
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("jakarta.annotation.Generated")
        ctx.requiresImport("java.io.Serial")
        ctx.requiresImport("org.fuin.ddd4j.core.EntityIdFactory")
        ctx.requiresImport("org.fuin.ddd4j.core.EntityIdPath")
        ctx.requiresImport("org.fuin.ddd4j.core.EntityIdPathSpec")
        ctx.requiresImport("org.fuin.objects4j.common.ConstraintViolationException")
        ctx.requiresImport("org.fuin.objects4j.common.Contract")
        ctx.requiresImport("org.fuin.objects4j.common.Immutable")
        ctx.requiresImport("org.fuin.objects4j.core.AbstractStringValueObject")
        ctx.requiresImport("org.jspecify.annotations.Nullable")
        for (segment : path.segments.nullSafe) {
            ctx.requiresReference(TypeKeys.refKey(segment.type))
        }

        return List.of(newArtifact(filename,
            create(ctx, ns, path, pkg, className).toString().getBytes("UTF-8"), path))
    }

    def create(SimpleCodeSnippetContext ctx, Module ns, EntityIdPathType path, String pkg, String className)
            throws GenerateException {

        val leaf = path.segments.nullSafe.last
        val src = '''
            «new SrcJavaDocType(path.doc)»
            @Immutable
            @Generated("Generated class - Manual changes will be overwritten")
            public final class «className» extends AbstractStringValueObject {

                @Serial
                private static final long serialVersionUID = 1000L;

                /**
                 * The shape a path of this type has: «shapeDoc(path)».
                 *
                 * <p>Public because the same shape is what a caller asks about before building one, and
                 * because an «"@"»ExpectedEntityIdPath annotation elsewhere has to agree with it.
                 */
                public static final EntityIdPathSpec SPEC = EntityIdPathSpec.builder()
                        «FOR segment : path.segments.nullSafe»
                        «step(segment)»
                        «ENDFOR»
                        .build();

                private final EntityIdPath path;

                /**
                 * Constructor with the path it addresses.
                 *
                 * @param path Path, which has to have the shape above.
                 */
                public «className»(final EntityIdPath path) {
                    super();
                    Contract.requireArgNotNull("path", path);
                    SPEC.requireArgValid("path", path);
                    this.path = path;
                }

                /**
                 * Returns the identifier this path addresses, which is its last step.
                 *
                 * @return Identifier of the «leaf.type.name» this path points at.
                 */
                public «leaf.type.name» last() {
                    return path.last();
                }

                /**
                 * Returns the path as ddd-4-java models it, for the operations declared there.
                 *
                 * @return The wrapped path.
                 */
                public EntityIdPath asEntityIdPath() {
                    return path;
                }

                @Override
                public String asBaseType() {
                    return path.asBaseType();
                }

                /**
                 * Converts a string into an instance of this class.
                 *
                 * @param factory Factory used to create the identifiers of each step.
                 * @param value   String to convert, or {@literal null}.
                 * @return New instance, or {@literal null} when the value was.
                 */
                @Nullable
                public static «className» valueOf(final EntityIdFactory factory, @Nullable final String value) {
                    if (value == null) {
                        return null;
                    }
                    return new «className»(EntityIdPath.valueOf(factory, value));
                }

                /**
                 * Whether a string is a path of this shape.
                 *
                 * @param factory Factory used to create the identifiers of each step.
                 * @param value   Value to check, or {@literal null} which is nothing to disagree with.
                 * @return {@literal true} if it is valid.
                 */
                public static boolean isValid(final EntityIdFactory factory, @Nullable final String value) {
                    if (value == null) {
                        return true;
                    }
                    if (!EntityIdPath.isValid(factory, value)) {
                        return false;
                    }
                    return SPEC.matches(EntityIdPath.valueOf(factory, value));
                }

                /**
                 * Checks a string is a path of this shape and throws if it is not.
                 *
                 * @param factory Factory used to create the identifiers of each step.
                 * @param name    Name of the value for a possible error message.
                 * @param value   Value to check.
                 * @throws ConstraintViolationException The value was not valid.
                 */
                public static void requireArgValid(final EntityIdFactory factory, final String name,
                        @Nullable final String value) throws ConstraintViolationException {
                    if (!isValid(factory, value)) {
                        throw new ConstraintViolationException("The argument '" + name + "' is not valid: '"
                                + value + "'");
                    }
                }

            }
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

    /** One step of the shape, written the way a reader of the model wrote it. */
    def private String step(PathSegment segment) {
        val range = segment.range
        if (range === null) {
            return ".step(" + segment.type.name + ".class)"
        }
        val max = if(range.unbounded) "Integer.MAX_VALUE" else String.valueOf(range.max)
        return ".step(" + segment.type.name + ".class, " + range.min + ", " + max + ")"
    }

    /** The shape as the model spells it, for the constant's documentation. */
    def private String shapeDoc(EntityIdPathType path) {
        val out = <String>newArrayList
        for (segment : path.segments.nullSafe) {
            val range = segment.range
            if (range === null) {
                out.add(segment.type.name)
            } else {
                out.add(segment.type.name + "[" + range.min + ".."
                    + (if(range.unbounded) "*" else String.valueOf(range.max)) + "]")
            }
        }
        return out.join(" / ")
    }

}
