package org.fuin.dsl.ddd.gen.rule

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates the class that verifies everything one aggregate or entity declares.
 *
 * <p>Shared by the aggregate and the entity because the two differ in nothing that matters here: both
 * carry operations, and an operation either declares rules or does not.
 *
 * @param <T> Aggregate or entity.
 */
abstract class AbstractRulesArtifactFactory<T extends AbstractEntity> extends AbstractSource<T> {

    override getTypeKey() {
        TypeKeys.JAVA_BUSINESS_RULES
    }

    override create(T owner, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val className = owner.name + "Rules"
        val pkg = owner.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java"

        val refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(owner, TypeKeys.JAVA_BUSINESS_RULES), fqn)

        if (SrcRulesClass.guarded(owner).empty) {
            // Nothing declares a rule, so there is nothing to verify and no empty class to explain.
            return null
        }

        if (preparationRun) {
            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.requiresImport("org.fuin.objects4j.common.Contract")
        ctx.requiresReference(TypeKeys.refKey(owner))

        val String src = new SrcRulesClass(ctx, owner, className).toString

        return List.of(newArtifact(filename,
            new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString.getBytes("UTF-8"), owner))
    }

}
