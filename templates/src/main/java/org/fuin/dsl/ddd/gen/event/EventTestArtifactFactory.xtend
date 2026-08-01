package org.fuin.dsl.ddd.gen.event

import java.util.Map
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.SrcInvokeMethod
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static org.fuin.dsl.ddd.gen.base.Utils.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractElementExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAggregateExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEventExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsLiteralExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsTypeExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsVariableExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.VariableExtensions.*
import org.fuin.dsl.ddd.gen.base.TypeKeys
import java.util.List

class EventTestArtifactFactory extends AbstractSource<Event> {

    override getModelType() {
        typeof(Event)
    }

    override getTypeKey() {
        TypeKeys.JAVA_EVENT_TEST
    }

    override create(Event event, Map<String, Object> context, boolean preparationRun) throws GenerateException {
        val AbstractEntity entity = event.entity;
        val className = event.getName() + "Test"
        // The module is optional: derive the package from the element itself - the enclosing
        // entity for a domain event, otherwise the event.
        val EObject owner = if (entity === null) event else entity
        val pkg = owner.asPackage
        val fqn = pkg + "." + className
        val filename = fqn.replace('.', '/') + ".java";

        val CodeReferenceRegistry refReg = context.codeReferenceRegistry
        refReg.putReference(TypeKeys.refKey(event, TypeKeys.JAVA_EVENT_TEST), fqn)

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
        ctx.addImports(entity)
        ctx.addReferences(entity, event)

        var String src;
        if (entity === null) {
            src = createStandardEventTest(ctx, event, pkg, className).toString();
        } else {
            src = createDomainEventTest(ctx, event, pkg, className).toString();
        }

        return List.of(newArtifact(filename, src.getBytes("UTF-8"), owner));
    }

    def addImports(CodeSnippetContext ctx, AbstractEntity entity) {
        ctx.requiresImport("static org.assertj.core.api.Assertions.*")
        ctx.requiresImport("org.junit.jupiter.api.Test")
        if (options.jaxb) {
            ctx.requiresImport("jakarta.xml.bind.annotation.adapters.XmlAdapter")            
        }
        if (entity !== null) {
            ctx.requiresImport("org.fuin.ddd4j.jaxb.EntityIdPathXmlAdapter")
            ctx.requiresImport("org.fuin.ddd4j.core.EntityIdPath")
        }
        if (options.jsonb) {
            ctx.requiresImport("org.fuin.ddd4j.jsonb.EventIdJsonbAdapter");
            ctx.requiresImport("jakarta.json.bind.Jsonb");
            ctx.requiresImport("jakarta.json.bind.JsonbBuilder");
            ctx.requiresImport("jakarta.json.bind.JsonbConfig");
            ctx.requiresImport("org.eclipse.yasson.FieldAccessStrategy");
        }
        ctx.requiresImport("static org.fuin.utils4j.Utils4J.serialize")
        ctx.requiresImport("static org.fuin.utils4j.Utils4J.deserialize")
        ctx.requiresImport("static org.fuin.utils4j.jaxb.JaxbUtils.marshal")
        ctx.requiresImport("static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal")        
    }

    def addReferences(CodeSnippetContext ctx, AbstractEntity entity, Event event) {
        ctx.requiresReference(TypeKeys.refKey(event))
        if (entity !== null) {
            ctx.requiresReference(TypeKeys.refKey(event.aggregate.idTypeNullsafe))
            ctx.requiresReference(contextSegment(event.module).toFirstUpper + "EntityIdFactory")
        }
        for (v : event.attributes.nullSafe) {
            addRequiredReferences(v, ctx)
        }
    }

    def createDomainEventTest(SimpleCodeSnippetContext ctx, Event event, String pkg, String className) {
        val String src = ''' 
            // CHECKSTYLE:OFF
            public final class «event.name»Test {
            
                @Test
                public final void testSerializeDeserialize() {
            
                    // PREPARE
                    final «event.name» original = createTestee();
            
                    // TEST
                    final «event.name» copy = deserialize(serialize(original));
            
                    // VERIFY
                    assertThat(original).isEqualTo(copy);
                    «FOR v : event.attributes.nullSafe»
                        assertThat(original.get«v.name.toFirstUpper»()).isEqualTo(copy.get«v.name.toFirstUpper»());
                    «ENDFOR»
            
                }
            
            «IF options.jaxb»
                @Test
                public final void testMarshalUnmarshalXml() {
            
                    // PREPARE
                    final «event.name» original = createTestee();
            
                    // TEST
                    final String xml = marshal(original, createAdapter(), «event.name».class);
                    final «event.name» copy = unmarshal(xml, createAdapter(), «event.name».class);
            
                    // VERIFY
                    assertThat(original).isEqualTo(copy);
                    «FOR v : event.attributes.nullSafe»
                        assertThat(original.get«v.name.toFirstUpper»()).isEqualTo(copy.get«v.name.toFirstUpper»());
                    «ENDFOR»
            
                }
            
            «ENDIF»            
            «IF options.jsonb»
                @Test
                public final void testMarshalUnmarshalJson() {
            
                    // PREPARE
                    final «event.name» original = createTestee();
            
                    final JsonbConfig config = new JsonbConfig()
                            .withAdapters(new EventIdJsonbAdapter())
                            .withPropertyVisibilityStrategy(new FieldAccessStrategy());
                    final Jsonb jsonb = JsonbBuilder.create(config);
            
                    // TEST
                    final String json = jsonb.toJson(original, «event.name».class);
                    final «event.name» copy = jsonb.fromJson(json, «event.name».class);
            
                    // VERIFY
                    assertThat(original).isEqualTo(copy);
                    «FOR v : event.attributes.nullSafe»
                        assertThat(original.get«v.name.toFirstUpper»()).isEqualTo(copy.get«v.name.toFirstUpper»());
                    «ENDFOR»
            
                }
            
            «ENDIF»            
                private «event.name» createTestee() {
                    // TODO Set test values
                    final «event.aggregate.idTypeNullsafe.name» entityId = new «event.aggregate.idTypeNullsafe.name»(«event.aggregate.idTypeNullsafe.firstExample.str»);
                    «FOR v : event.attributes.nullSafe»
                        final «v.type(ctx)» «v.name» = «v.firstExample.str»;
                    «ENDFOR»
                    return new «new SrcInvokeMethod(ctx, event.name, union("new EntityIdPath(entityId)", event.attributes.asNames))»
                }
            
                protected final XmlAdapter<?, ?>[] createAdapter() {
                    final EntityIdPathXmlAdapter EntityIdPathXmlAdapter = new EntityIdPathXmlAdapter(new «contextSegment(event.module).toFirstUpper»EntityIdFactory());
                    return new XmlAdapter[] { EntityIdPathXmlAdapter };
                }
            
            }
            // CHECKSTYLE:ON
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

    def createStandardEventTest(SimpleCodeSnippetContext ctx, Event event, String pkg, String className) {
        val String src = ''' 
            // CHECKSTYLE:OFF
            public final class «event.name»Test {
            
                @Test
                public final void testSerializeDeserialize() {
            
                    // PREPARE
                    final «event.name» original = createTestee();
            
                    // TEST
                    final «event.name» copy = deserialize(serialize(original));
            
                    // VERIFY
                    assertThat(original).isEqualTo(copy);
                    «FOR v : event.attributes.nullSafe»
                        assertThat(original.get«v.name.toFirstUpper»()).isEqualTo(copy.get«v.name.toFirstUpper»());
                    «ENDFOR»
            
                }
            
            «IF options.jaxb»
                @Test
                public final void testMarshalUnmarshalXml() {
            
                    // PREPARE
                    final «event.name» original = createTestee();
            
                    // TEST
                    final String xml = marshal(original, createAdapter(), «event.name».class);
                    final «event.name» copy = unmarshal(xml, createAdapter(), «event.name».class);
            
                    // VERIFY
                    assertThat(original).isEqualTo(copy);
                    «FOR v : event.attributes.nullSafe»
                        assertThat(original.get«v.name.toFirstUpper»()).isEqualTo(copy.get«v.name.toFirstUpper»());
                    «ENDFOR»
            
                }
            
            «ENDIF»            
            «IF options.jsonb»
                @Test
                public final void testMarshalUnmarshalJson() {
            
                    // PREPARE
                    final «event.name» original = createTestee();
            
                    final JsonbConfig config = new JsonbConfig()
                            .withAdapters(new EventIdJsonbAdapter())
                            .withPropertyVisibilityStrategy(new FieldAccessStrategy());
                    final Jsonb jsonb = JsonbBuilder.create(config);
            
                    // TEST
                    final String json = jsonb.toJson(original, «event.name».class);
                    final «event.name» copy = jsonb.fromJson(json, «event.name».class);
            
                    // VERIFY
                    assertThat(original).isEqualTo(copy);
                    «FOR v : event.attributes.nullSafe»
                        assertThat(original.get«v.name.toFirstUpper»()).isEqualTo(copy.get«v.name.toFirstUpper»());
                    «ENDFOR»
            
                }
            
            «ENDIF»            
                private «event.name» createTestee() {
                    «IF event.attributes.nullSafe.size > 0»
                        // TODO Set test values
                        «FOR v : event.attributes.nullSafe»
                            final «v.type(ctx)» «v.name» = new «v.type(ctx)»(«v.firstExample.str»);
                        «ENDFOR»
                    «ENDIF»
                    return new «new SrcInvokeMethod(ctx, event.name, event.attributes.asNames)»
                }
            
                protected final XmlAdapter<?, ?>[] createAdapter() {
                    return new XmlAdapter[] { };
                }
            
            }
            // CHECKSTYLE:ON
        '''

        new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString

    }

}
