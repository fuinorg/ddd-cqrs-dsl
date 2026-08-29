package org.fuin.dsl.ddd.gen.rule

import java.util.List
import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.Key
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsKeyExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*
import static extension org.fuin.dsl.ddd.gen.rule.KeyDerivation.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Creates the class that verifies one <code>key</code>: the uniqueness rule the key stands for.
 *
 * <p>It is the same kind of artifact a <code>business-rule</code> produces, registered under the same
 * type key, so everything that constructs a rule reaches a derived one the same way. What the key
 * saves is writing it: the rule decides from whether the key is taken plus the key's own values, its
 * condition is that it is not taken, and its refusal is the exception the key names.
 *
 * <p><b>Only a key that refuses generates a class.</b> <code>overwrite</code> and <code>skip</code>
 * describe what a handler does with a second occurrence rather than a reason to refuse an operation,
 * so there is no rule to verify and nothing here to emit.
 */
class KeyArtifactFactory extends AbstractSource<Key> {

    override getModelType() {
        typeof(Key)
    }

    override getTypeKey() {
        TypeKeys.JAVA_BUSINESS_RULE
    }

    override create(Key key, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (!key.refuses) {
            return null
        }

        val className = key.ruleName
        val pkg = key.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java"

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(key), fqn)

        if (preparationRun) {
            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(key)
        ctx.addReferences(key)

        return List.of(newArtifact(filename,
            create(ctx, key, pkg, className).toString.getBytes("UTF-8"), key))
    }

    def addImports(CodeSnippetContext ctx, Key key) {
        ctx.requiresImport("org.fuin.dsl.cqrs.common.rules.BusinessRule")
        ctx.requiresImport("org.fuin.objects4j.common.Contract")
    }

    def addReferences(CodeSnippetContext ctx, Key key) throws GenerateException {
        ctx.requiresReference(TypeKeys.refKey(key.exception))
        for (attribute : key.derivedAttributes) {
            ctx.requiresReference(TypeKeys.refKey(attribute.type))
        }
    }

    def create(SimpleCodeSnippetContext ctx, Key key, String pkg, String className)
            throws GenerateException {

        // A key is satisfied exactly when nothing else holds it. There is no other shape to render:
        // the condition is what the construct means rather than something the model states.
        val String condition = "!" + key.answerName

        val String src = new SrcRuleVerifierClass(ctx, className, key.doc.text, key.derivedAttributes,
            key.exception, condition).toString

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString
    }

}
