package org.fuin.dsl.ddd.gen.resourceset

import java.util.ArrayList
import java.util.Iterator
import java.util.List
import java.util.Map
import java.util.TreeSet
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractElement
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.dsl.ddd.gen.base.SrcAll
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsStringExtensions.*

/**
 * Creates the application's <b>permission catalogue</b> from the model: one entry per
 * {@code command} and one per {@code method} of a {@code view}.
 *
 * <p>The catalogue is the static, release-versioned list of operations an authorization decision may
 * be written against. Deriving it from the model rather than curating it by hand is the whole point -
 * it makes the catalogue <b>exhaustive by construction</b>, so an operation shipped without an entry
 * becomes impossible. A hand-kept list fails in two different ways: a command with no entry is denied
 * and therefore dead on release day, while a view method with no entry is an <i>unchecked read</i>.
 *
 * <p>Two rules fix what an entry is:
 * <ul>
 * <li><b>Every command is one write permission.</b> A command already is the unit of "may do this" -
 * named in the model, dispatched by type, handled in one place - so the mapping is 1:1.</li>
 * <li><b>Every method of a view is one read permission - the method, not the view.</b> A view is one
 * projection with query methods of quite different sensitivity: one feeds a picker with category
 * names, the one beside it returns a person's full record. A permission on the view would be
 * all-or-nothing on the projection, which is too coarse to be what a handler checks.</li>
 * </ul>
 *
 * <p>A whole-view id ({@code «View».*}) is emitted <em>in addition</em>, for <b>assigning</b> only:
 * ticking twenty-five method boxes is how a role editor becomes unusable. It must never be what an
 * enforcement point checks, or there are two catalogues and two answers to the same question.
 *
 * <p>This factory has no opinion about authorization beyond publishing the operation surface. Who may
 * call what is domain data and belongs in the domain model.
 *
 * <p>Two artifacts are produced, both once for the whole model:
 * <ul>
 * <li>{@code PERMISSIONS.md} - the readable catalogue, for designing roles against.</li>
 * <li>{@code PermissionIds} - the same list as Java constants, plus {@code ALL} for validating a
 * stored role definition and {@code VIEW_METHODS} for expanding a whole-view grant.</li>
 * </ul>
 */
class PermissionCatalogueArtifactFactory extends AbstractSource<ResourceSet> {

    /** Module both artifacts are written to - the catalogue is needed on every side. */
    static val MODULE = "shared"

    /** Name of the generated Java class holding the catalogue as constants. */
    static val CLASS_NAME = "PermissionIds"

    /** Name of the generated documentation file. */
    static val DOC_NAME = "PERMISSIONS.md"

    /** Suffix a whole-view permission id carries. */
    static val VIEW_ALL_SUFFIX = ".*"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.RES_PERMISSION_CATALOGUE
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        if (preparationRun) {

            // No code generation during preparation phase
            return null
        }

        val List<CommandEntry> commands = new ArrayList<CommandEntry>()
        val List<ViewEntry> views = new ArrayList<ViewEntry>()
        var String project = null

        val Iterator<EObject> it = resourceSet.allContents.filter(typeof(EObject)).filter[isPrimary(it)]
        while (it.hasNext) {
            val EObject container = it.next
            if (container instanceof Module) {
                for (element : container.elements) {
                    if (element instanceof Command) {
                        project = element.context.name
                        commands.add(commandEntry(element, container))
                    } else if (element instanceof View) {
                        project = element.context.name
                        views.add(viewEntry(element, container))
                    }
                }
            }
        }

        // A model with no operation at all gets no catalogue. Emitting one anyway would name a target
        // module the project may have no reason to own - see SpringBeansArtifactFactory for the failure
        // that produces.
        if (project === null || (commands.empty && views.empty)) {
            return null
        }

        // Sorted so the generated files are stable in version control: the order elements come out of
        // the resource set is not guaranteed, and a catalogue that reshuffles on every build is
        // unreviewable.
        val sortedCommands = commands.sortBy[context + "/" + module + "/" + id].toList
        val sortedViews = views.sortBy[context + "/" + module + "/" + name].toList

        val List<GeneratedArtifact> artifacts = new ArrayList<GeneratedArtifact>()
        artifacts.add(markdown(sortedCommands, sortedViews))
        artifacts.add(javaConstants(project, sortedCommands, sortedViews))
        return artifacts
    }

    /** Collects the one write permission a command contributes. */
    private def CommandEntry commandEntry(Command command, Module module) {
        val entry = new CommandEntry()
        entry.context = command.context.name
        entry.module = module.name
        entry.id = command.name
        entry.target = targetOf(command)
        entry.owner = ownerOf(command)
        entry.doc = command.doc.docText
        return entry
    }

    /** Collects a view and the one read permission each of its methods contributes. */
    private def ViewEntry viewEntry(View view, Module module) {
        val entry = new ViewEntry()
        entry.context = view.context.name
        entry.module = module.name
        entry.name = view.name
        entry.restPath = view.restPath
        entry.doc = view.doc.docText
        for (Method method : view.methods) {
            val m = new MethodEntry()
            m.name = method.name
            m.id = view.name + "." + method.name
            m.returns = returnsOf(method)
            m.restPath = method.restPath
            m.doc = method.doc.docText
            entry.methods.add(m)
        }
        return entry
    }

    /**
     * The aggregate method a command dispatches to, as "Aggregate.method". A command declares its
     * target as a cross reference, so the owner is the target's container.
     */
    private def String targetOf(Command command) {
        val target = command.target
        if (target === null) {
            return ""
        }
        val owner = target.eContainer
        if (owner === null) {
            return target.name
        }
        return owner.name + "." + target.name
    }

    /**
     * The aggregate or entity a command acts on - the owner of the method it targets.
     * <p>
     * Empty when the command declares no target, or when that target has no owner: the grammar allows
     * both, and neither can be grouped under anything.
     */
    private def String ownerOf(Command command) {
        val target = command.target
        if (target === null) {
            return ""
        }
        val owner = target.eContainer
        if (owner === null) {
            return ""
        }
        return owner.name
    }

    /**
     * The doc comment as one line of prose.
     * <p>
     * The shared "text" extension strips "&#47;**", "*&#47;" and " * ", but a paragraph separator is a
     * line holding nothing but an asterisk, which has no trailing space and therefore survives as a
     * stray "*" in the middle of the sentence. Drop those here rather than in the extension, which
     * every other generator shares.
     */
    private def String docText(String doc) {
        return doc.text.replaceAll("(?:^|(?<=\\s))\\*(?=\\s|$)", " ").replaceAll("\\s+", " ").trim
    }

    /** The declared result of a view method, as the model spells it - "optional MasterDataDetails". */
    private def String returnsOf(Method method) {
        val rt = method.returnType
        if (rt === null || rt.type === null) {
            return ""
        }
        val StringBuilder sb = new StringBuilder()
        if (rt.optional !== null) {
            sb.append("optional ")
        }
        sb.append(rt.type.name)
        if (rt.generics !== null) {
            val StringBuilder args = new StringBuilder()
            for (arg : rt.generics.args) {
                if (args.length > 0) {
                    args.append(", ")
                }
                args.append(arg.name)
            }
            sb.append("<").append(args).append(">")
        }
        return sb.toString
    }

    // ---- Markdown ------------------------------------------------------------------------------

    /** Creates the readable catalogue, grouped by context and module. */
    private def GeneratedArtifact markdown(List<CommandEntry> commands, List<ViewEntry> views) {
        val groups = new TreeSet<String>()
        commands.forEach[groups.add(context + "/" + module)]
        views.forEach[groups.add(context + "/" + module)]
        val src = '''
            # Permission Catalogue

            Generated from the model - do not edit. Every `command` is one write permission and every
            `method` of a `view` is one read permission. The list is derived from the model, so it is
            exhaustive by construction: an operation cannot ship without an entry.

            A permission id is a **stable identifier**. Stored role definitions reference it by name, so
            renaming a command or a view method is a data migration - deprecate, migrate, then remove.

            The `«VIEW_ALL_SUFFIX»` id of a view stands for every one of its methods, including methods added in a
            later release. It exists for **assigning** roles only and must never be what an enforcement
            point checks.

            «FOR group : groups»
                ## «group.replace("/", " / ")»
                «val ctx = group.substring(0, group.indexOf("/"))»
                «val mod = group.substring(group.indexOf("/") + 1)»
                «val groupCommands = commands.filter[context == ctx && module == mod].toList»
                «IF !groupCommands.empty»

                    ### Commands (write permissions)

                    | Permission id | Target | Description |
                    |---|---|---|
                    «FOR c : groupCommands»
                        | `«c.id»` | «c.target.cell» | «c.doc.cell» |
                    «ENDFOR»
                «ENDIF»
                «FOR v : views.filter[context == ctx && module == mod]»

                    ### View `«v.name»`«IF v.restPath !== null» (`«v.restPath»`)«ENDIF»

                    «IF !v.doc.nullOrEmpty»«v.doc»

                    «ENDIF»Whole-view id: `«v.name»«VIEW_ALL_SUFFIX»`
                    «IF v.methods.empty»

                    No methods - this view contributes no read permission.
                    «ELSE»

                    | Permission id | Returns | Description |
                    |---|---|---|
                    «FOR m : v.methods»
                        | `«m.id»` | «m.returns.cell» | «m.doc.cell» |
                    «ENDFOR»
                    «ENDIF»
                «ENDFOR»

            «ENDFOR»
        '''
        return newArtifact(DOC_NAME, src.toString.getBytes("UTF-8"), MODULE, "genMainRes")
    }

    /**
     * Makes a value safe to put in a Markdown table cell: a pipe would end the column and a doc
     * comment is free text.
     */
    private def String cell(String value) {
        if (value.nullOrEmpty) {
            return ""
        }
        return value.replace("|", "\\|")
    }

    // ---- Java constants ------------------------------------------------------------------------

    /** Creates the catalogue as Java constants, plus the two lookups an authorization model needs. */
    private def GeneratedArtifact javaConstants(String project, List<CommandEntry> commands, List<ViewEntry> views) {
        val pkg = project + ".shared.domain"
        val ctx = new SimpleCodeSnippetContext(null)
        ctx.requiresImport("java.util.Map")
        ctx.requiresImport("java.util.Set")

        // Constant names are derived from the permission id, which is unique only within a module: two
        // modules may each declare a "CategoryView". Uniquify against everything already taken so the
        // generated class always compiles.
        val used = new TreeSet<String>()
        for (c : commands) {
            c.constant = unique(used, c.id.toSqlUpper)
        }
        for (v : views) {
            // Each part is converted on its own and joined afterwards: converting the joined name
            // would see the separator as the character before an upper case letter and double it.
            v.constant = unique(used, v.name.toSqlUpper + "_ALL")
            for (m : v.methods) {
                m.constant = unique(used, v.name.toSqlUpper + "_" + m.name.toSqlUpper)
            }
        }

        val src = '''
            /**
             * Every operation this application exposes, as the stable ids an authorization decision is
             * written against: one constant per command (a write permission) and one per view method (a
             * read permission).
             *
             * <p>Generated from the model on every build, which is what makes the catalogue exhaustive -
             * an operation cannot ship without an entry here.
             *
             * <p>The ids are <b>stable identifiers</b>: stored role definitions reference them by name,
             * so renaming a command or a view method strands every role naming it. Treat such a rename as
             * a data migration - deprecate, migrate, then remove.
             */
            public final class «CLASS_NAME» {

                «FOR c : commands»
                    /** Write permission for {@code «c.id»}«IF !c.doc.nullOrEmpty» - «c.doc»«ENDIF» */
                    public static final String «c.constant» = "«c.id»";

                «ENDFOR»
                «FOR v : views»
                    /**
                     * Every method of {@code «v.name»}, including methods added in a later release.
                     * For assigning roles only - never check this at an enforcement point.
                     */
                    public static final String «v.constant» = "«v.name»«VIEW_ALL_SUFFIX»";

                    «FOR m : v.methods»
                        /** Read permission for {@code «m.id»}«IF !m.doc.nullOrEmpty» - «m.doc»«ENDIF» */
                        public static final String «m.constant» = "«m.id»";

                    «ENDFOR»
                «ENDFOR»
                /**
                 * Every single-operation permission id - the whole-view ids are not in here, because a
                 * group is not itself an operation. A stored role naming something outside this set
                 * references an operation this release does not have, and must fail loudly rather than
                 * silently granting nothing.
                 */
                public static final Set<String> ALL = Set.of(
                    «FOR id : allIds(commands, views) SEPARATOR ","»
                        «id»
                    «ENDFOR»
                );

                /**
                 * The method ids of each view, keyed by view name - what a whole-view grant expands to.
                 * The expansion belongs in the effective-permission projection rather than in stored role
                 * data, which is why a release adding a view method has to trigger a rebuild for the new
                 * method to be picked up.
                 */
                public static final Map<String, Set<String>> VIEW_METHODS = Map.ofEntries(
                    «FOR v : views SEPARATOR ","»
                        Map.entry("«v.name»", Set.of(«FOR m : v.methods SEPARATOR ", "»«m.constant»«ENDFOR»))
                    «ENDFOR»
                );

                /**
                 * What each command acts on, keyed by command id - the aggregate or entity that owns the
                 * method the command targets. A view method needs no such entry: its id already names the
                 * view it belongs to.
                 *
                 * Presentation only, and deliberately so: this is what lets an editor group seventy flat
                 * identifiers under the thing they act on, which is the difference between a usable list
                 * and one nobody reads. Nothing checks it - a permission check is always against a single
                 * id. A command declared without a target has no entry.
                 */
                public static final Map<String, String> COMMAND_TARGETS = Map.ofEntries(
                    «FOR c : commands.filter[!owner.nullOrEmpty] SEPARATOR ","»
                        Map.entry(«c.constant», "«c.owner»")
                    «ENDFOR»
                );

                private «CLASS_NAME»() {
                    throw new UnsupportedOperationException("It's not allowed to create an instance of this utility class");
                }

            }
        '''
        return newArtifact(filename(pkg, CLASS_NAME),
            new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, src).toString.getBytes("UTF-8"), MODULE, "genMainJava")
    }

    /** Constant names of every single operation - commands first, then each view's methods. */
    private def List<String> allIds(List<CommandEntry> commands, List<ViewEntry> views) {
        val List<String> result = new ArrayList<String>()
        commands.forEach[result.add(constant)]
        views.forEach[v|v.methods.forEach[result.add(constant)]]
        return result
    }

    /** Returns the given name, suffixed with a counter while something already claimed it. */
    private def String unique(TreeSet<String> used, String name) {
        var candidate = name
        var int i = 2
        while (!used.add(candidate)) {
            candidate = name + "_" + i
            i = i + 1
        }
        return candidate
    }

    private def String filename(String pkg, String className) {
        (pkg + "." + className).replace('.', '/') + ".java"
    }

    // ---- Collected model data ------------------------------------------------------------------

    /** One write permission: a command. */
    private static class CommandEntry {
        public String context
        public String module
        public String id
        public String target

        public String owner
        public String doc
        public String constant
    }

    /** A view and the read permissions its methods contribute. */
    private static class ViewEntry {
        public String context
        public String module
        public String name
        public String restPath
        public String doc
        public String constant
        public List<MethodEntry> methods = new ArrayList<MethodEntry>()
    }

    /** One read permission: a method of a view. */
    private static class MethodEntry {
        public String id
        public String name
        public String returns
        public String restPath
        public String doc
        public String constant
    }

}
