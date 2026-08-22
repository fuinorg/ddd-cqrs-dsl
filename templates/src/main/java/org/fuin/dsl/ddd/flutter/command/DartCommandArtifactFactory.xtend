package org.fuin.dsl.ddd.flutter.command

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsAbstractEntityExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.EventExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*

/**
 * Creates a Dart class from a <code>command</code>: what the write side accepts, and what a form has to
 * collect before it can be sent.
 *
 * <p>Three things it derives that a client would otherwise write down and get wrong:
 *
 * <ul>
 * <li><b>The attributes.</b> A command usually declares none of its own - it names an operation, and
 * takes that operation's parameters. Copying them is what keeps the form and the aggregate in step.</li>
 * <li><b>What it does.</b> A constructor creates, an operation firing an exodus event removes, anything
 * else modifies. That decides whether a screen offers it on a button, behind a row, or behind a
 * confirmation - and whether the command carries an aggregate version at all.</li>
 * <li><b>Which field a refusal belongs on.</b> See {@link #rejections}.</li>
 * </ul>
 */
class DartCommandArtifactFactory extends AbstractDartSource<Command> {

    /** Events whose presence means the operation ends the aggregate. */
    static val String EXODUS = "ExodusEvent"

    override getModelType() {
        typeof(Command)
    }

    override getTypeKey() {
        TypeKeys.DART_COMMAND
    }

    override create(Command command, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        val refReg = context.codeReferenceRegistry
        val filename = DartNames.file(command.module, command.name)
        refReg.putReference(TypeKeys.refKey(command, typeKey), filename)

        if (preparationRun) {
            return null
        }

        return List.of(newArtifact(filename, tidy(create(command).toString).getBytes("UTF-8"),
            command, typeKey))
    }

    def private create(Command command) {
        val className = command.name
        val attributes = new ArrayList<DartAttribute>()
        for (Variable variable : command.commandVariables.nullSafe) {
            attributes.add(new DartAttribute(variable))
        }
        val aggregate = command.aggregate
        val entity = command.entity
        // The wire carries the path from the root down, so the root alone cannot say which child.
        val childTargeted = aggregate !== null && entity !== null && entity !== aggregate
        val aggregateIdType = aggregate?.idType?.name ?: "String"
        val entityIdType = if (childTargeted) entity.idType?.name else null
        // A model without an aggregate id leaves the identifier a bare String, which has no typed form.
        val aggregateTyped = if (aggregate?.idType === null) "aggregateId" else "aggregateId.typed"
        val bundle = bundleName(command.module)
        val versioned = !(command.target instanceof Constructor)
        val rejections = rejections(command, attributes)

        '''
        «FOR imp : imports(command, attributes, aggregateIdType, entityIdType)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(command.doc, "")»
        class «className» {
          /// Constructor with all data.
          const «className»({
            required this.aggregateId,
            «IF childTargeted»
            required this.entityId,
            «ENDIF»
            «FOR a : attributes»
            required this.«a.name»,
            «ENDFOR»
            «IF versioned»
            this.aggregateVersion,
            «ENDIF»
          });

          /// Unique name used to store the command, and the last path segment of `POST /cmd/<type>`.
          static const String eventType = «dartString(className)»;

          /// What this command is called and what it needs.
          static const CommandDescriptor descriptor = CommandDescriptor(
            type: eventType,
            module: «dartString(command.module?.name)»,
            target: «dartString(command.aggregate?.name)»,«IF entity !== null»
            targetType: «dartString(entity.name.asEntityTypeConstant)»,«ENDIF»
            targetOrigin: «origin(command, aggregate, entity)»,
            kind: «kind(command)»,
            doc: «dartStringOrNull(docText(command.doc))»,
            message: «dartStringRaw(command.message)»,
            «IF !rejections.empty»
            rejections: <String, String>{
              «FOR entry : rejections.entrySet»
              «dartString(entry.key)»: «dartString(entry.value)»,
              «ENDFOR»
            },
            «ENDIF»
            «IF !attributes.empty»
            attributes: <AttributeDescriptor>[
              «FOR a : attributes»
              «descriptorOf(a, bundle)»
              «ENDFOR»
            ],
            «ENDIF»
          );

          /// Identifier of the aggregate this is directed at.
          final «aggregateIdType» aggregateId;
          «IF childTargeted»

          /// Identifier of the entity inside that aggregate this is directed at.
          final «entityIdType» entityId;
          «ENDIF»

          /// Path from the aggregate root down to the entity this is directed at, in the form the
          /// wire carries: typed segments separated by a slash.
          String get entityIdPath => «IF childTargeted»'${«aggregateTyped»}/${entityId.typed}'«ELSE»«aggregateTyped»«ENDIF»;
          «IF versioned»

          /// Version of the aggregate the change was decided on, so the write side can tell whether it
          /// is still current. Absent when the client does not know it.
          final int? aggregateVersion;
          «ENDIF»
          «FOR a : attributes»

          «IF !dartDoc(a.attribute.doc, "").empty»«dartDoc(a.attribute.doc, "")»
          «ENDIF»final «a.type» «a.name»;
          «ENDFOR»

          /// Writes the command as the request body of `POST /cmd/«className»`.
          Map<String, Object?> toJson() => <String, Object?>{
                'entity-id-path': entityIdPath,
                «IF versioned»
                if (aggregateVersion != null) 'aggregate-version': aggregateVersion,
                «ENDIF»
                «FOR a : attributes»
                «dartString(a.name)»: «a.toJson»,
                «ENDFOR»
              };
        }
        '''
    }

    /**
     * Where the client gets the <code>entity-id-path</code> from. A singleton aggregate is not
     * expressible - it reads as an ordinary client-minted create - see <code>todo.md</code>.
     */
    def private static String origin(Command command, Aggregate aggregate, AbstractEntity entity) {
        val idType = aggregate?.idType
        if (idType !== null && idType.base === null && !idType.attributes.nullSafe.empty) {
            // A natural key follows from what the command already carries.
            return "CommandTargetOrigin.derived"
        }
        if (command.target instanceof Constructor) {
            return if (entity !== null && aggregate !== null && entity !== aggregate)
                    "CommandTargetOrigin.parentOfRow"
                else
                    "CommandTargetOrigin.clientGenerated"
        }
        return "CommandTargetOrigin.row"
    }

    /** What the command does to the aggregate it targets. */
    def private static String kind(Command command) {
        val AbstractMethod target = command.target
        if (target === null || target instanceof Constructor) {
            return "CommandKind.create"
        }
        for (event : target.firedEvents.nullSafe) {
            if (event?.annotations.nullSafe.exists[annotation?.name == EXODUS]) {
                return "CommandKind.remove"
            }
        }
        return "CommandKind.modify"
    }

    /**
     * Which attribute each business rule's refusal belongs on, keyed by the exception's simple name.
     *
     * <p>A refusal arrives as the exception's class and the model's own wording, and says nothing about
     * which field the rule was about - so a form would show "a category named X already exists" above
     * itself rather than under the name. What is derived here is what the model actually states:
     *
     * <ol>
     * <li>the exception carries an attribute with the same name as one of the command's, or</li>
     * <li>the command has exactly one attribute, in which case any field-level refusal is about it.</li>
     * </ol>
     *
     * <p><b>Anything else is left out rather than guessed.</b> Deriving it from the exception's class
     * name works for <code>DuplicateCategoryNameException</code> and <code>name</code> and stops working
     * the moment the attribute is called <code>newName</code>; a refusal shown on the wrong field is
     * worse than one shown above the form. The model has no way to say it outright today - see
     * <code>todo.md</code>.
     */
    def private static Map<String, String> rejections(Command command, List<DartAttribute> attributes) {
        val out = new LinkedHashMap<String, String>()
        val target = command.target
        if (target === null) {
            return out
        }
        val displayed = attributes.filter[displayed].toList
        for (rule : target.businessRules?.businessRuleInstances.nullSafe) {
            val exception = rule?.businessRule?.exception
            if (exception !== null) {
                // Exactly one, or none. Two of the command's attributes sharing a name with the
                // exception's says the model has not decided which the rule is about, and a refusal
                // shown on the wrong field is worse than one shown above the form.
                val matching = displayed.filter[a | exception.attributes.nullSafe.exists[name == a.name]].toList
                var String attribute = if(matching.size === 1) matching.get(0).name else null
                if (attribute === null && displayed.size === 1) {
                    attribute = displayed.get(0).name
                }
                if (attribute !== null) {
                    out.put(exception.name, attribute)
                }
            }
        }
        return out
    }

    def private descriptorOf(DartAttribute a, String bundle) {
        val meta = a.meta
        '''
        AttributeDescriptor(
          name: «dartString(a.name)»,
          kind: «a.valueKind»,«IF a.modelType !== null»
          modelType: «dartString(a.modelType)»,«ENDIF»«IF a.nestedDescriptor !== null»
          nested: «a.nestedDescriptor»,«ENDIF»«IF a.optional»
          optional: true,«ENDIF»«IF a.multiple»
          multiple: true,«ENDIF»«IF states(meta)»
          text: ModelText(
            bundle: «dartString(bundle)»,
            key: «dartString(a.name)»,
            shortLabel: «dartStringOrNull(meta?.slabel)»,
            label: «dartStringOrNull(meta?.label)»,
            tooltip: «dartStringOrNull(meta?.tooltip)»,«IF meta?.prompt !== null»
            prompt: «dartString(meta.prompt)»,«ENDIF»
          ),«ENDIF»«IF a.constraints !== null»
          constraints: «a.constraints»,«ENDIF»«IF a.values !== null»
          values: «a.values»,«ENDIF»
        ),'''
    }

    def private imports(Command command, List<DartAttribute> attributes, String aggregateIdType,
            String entityIdType) {
        val out = new TreeSet<String>()
        val aggregate = command.aggregate
        if (aggregate?.idType !== null) {
            out.add(DartNames.importOf(packageOf(aggregate.idType), aggregate.idType.module,
                aggregateIdType))
        }
        if (entityIdType !== null) {
            val entityId = command.entity?.idType
            if (entityId !== null) {
                out.add(DartNames.importOf(packageOf(entityId), entityId.module, entityIdType))
            }
        }
        for (a : attributes) {
            val referenced = a.referenced
            if (referenced !== null) {
                out.add(importOf(referenced))
            }
        }
        out.add(runtimeImport("src/descriptor/command_descriptor.dart"))
        if (attributes.exists[usesWireHelper]) {
            out.add(runtimeImport("src/json/json.dart"))
        }
        if (!attributes.empty) {
            out.add(runtimeImport("src/descriptor/attribute_descriptor.dart"))
            if (attributes.exists[states(meta)]) {
                out.add(runtimeImport("src/descriptor/model_text.dart"))
            }
        }
        return out
    }

}
