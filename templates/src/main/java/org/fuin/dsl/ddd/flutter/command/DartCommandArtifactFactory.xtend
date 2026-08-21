package org.fuin.dsl.ddd.flutter.command

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractMethod
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Constructor
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartAttribute
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.EventExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

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
        val idType = command.aggregate?.idType?.name ?: "String"
        val bundle = bundleName(command.module)
        val versioned = !(command.target instanceof Constructor)
        val rejections = rejections(command, attributes)

        '''
        «FOR imp : imports(command, attributes, idType)»
        import '«imp»';
        «ENDFOR»

        «dartDoc(command.doc, "")»
        class «className» {
          /// Constructor with all data.
          const «className»({
            required this.entityIdPath,
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
            target: «dartString(command.aggregate?.name)»,
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
          final «idType» entityIdPath;
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
                'entity-id-path': entityIdPath.typed,
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
          kind: «a.valueKind»,«IF a.optional»
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

    def private imports(Command command, List<DartAttribute> attributes, String idType) {
        val out = new TreeSet<String>()
        val aggregate = command.aggregate
        if (aggregate?.idType !== null) {
            out.add(DartNames.importOf(packageOf(aggregate.idType), aggregate.idType.module, idType))
        }
        for (a : attributes) {
            val referenced = a.referenced
            if (referenced !== null) {
                out.add(importOf(referenced))
            }
        }
        out.add(runtimeImport("src/descriptor/command_descriptor.dart"))
        if (!attributes.empty) {
            out.add(runtimeImport("src/descriptor/attribute_descriptor.dart"))
            if (attributes.exists[states(meta)]) {
                out.add(runtimeImport("src/descriptor/model_text.dart"))
            }
        }
        return out
    }

}
