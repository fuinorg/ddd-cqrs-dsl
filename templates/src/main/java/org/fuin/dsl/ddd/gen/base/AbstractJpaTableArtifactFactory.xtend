package org.fuin.dsl.ddd.gen.base

import java.util.ArrayList
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.fuin.dsl.cqrs.cqrsDsl.Hint
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry
import org.fuin.srcgen4j.core.emf.CodeSnippetContext
import org.fuin.srcgen4j.core.emf.SimpleCodeSnippetContext

import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Base class for factories that render JPA entity classes from a "JpaHint" (see {@link JpaHint}). It
 * holds the whole rendering: it collects the "JpaHint" hints of a model element (supplied by the
 * concrete subclass via {@link #jpaHints}), parses each into a {@link JpaHint}, and creates one
 * {@code @Entity} class per declared table in the element's package ({@link #asPackage}). A subclass
 * only has to bind it to a concrete model type ({@link #getModelType}) and say where the hints live.
 *
 * @param <T> Model element type the hints are attached to (e.g. a {@code View}).
 */
abstract class AbstractJpaTableArtifactFactory<T extends EObject> extends AbstractSource<T> {

    /**
     * Returns the "JpaHint" hints that apply to the given model element.
     *
     * @param element Model element being generated for.
     *
     * @return The element's "JpaHint" hints - Never <code>null</code>, but may be empty.
     */
    def protected Iterable<Hint> jpaHints(T element)

    override create(T element, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val String pkg = element.asPackage
        val CodeReferenceRegistry refReg = context.codeReferenceRegistry

        val List<JpaTable> tables = new ArrayList<JpaTable>()
        for (hint : jpaHints(element)) {
            tables.addAll(JpaHint.parse(hint).tables)
        }

        // Register the fully qualified names first, so other factories can import the generated tables.
        for (table : tables) {
            refReg.putReference(pkg + "." + table.className, pkg + "." + table.className)
        }

        if (preparationRun) {
            // No code generation during preparation phase
            return null
        }

        val List<GeneratedArtifact> artifacts = new ArrayList<GeneratedArtifact>()
        for (table : tables) {
            val String className = table.className
            val String fqn = pkg + "." + className
            val String filename = fqn.replace('.', '/') + ".java"
            val SimpleCodeSnippetContext ctx = new SimpleCodeSnippetContext(refReg)
            ctx.addImports(table)
            val String src = new SrcAll(ctx, copyrightHeader, pkg, ctx.imports, classBody(table, className).toString).toString
            artifacts.add(newArtifact(filename, src.getBytes("UTF-8"), element))
        }
        return artifacts
    }

    def private CharSequence classBody(JpaTable table, String className) '''
        /**
         * JPA entity for the «table.name ?: className» table.
         */
        «IF !tableAnnotationParts(table).empty»
        @Table(«tableAnnotationParts(table).join(", ")»)
        «ENDIF»
        @Entity
        public class «className» {

            «FOR col : table.columns»
            «field(col)»
            «ENDFOR»
            /**
             * Protected default constructor only required for JPA.
             */
            @SuppressWarnings("NullAway.Init")
            protected «className»() {
                super();
            }

            «FOR col : table.columns»
            «accessors(col)»
            «ENDFOR»
        }
    '''

    def private CharSequence field(JpaColumn col) '''
        «IF col.id !== null && col.id»
        @Id
        «ENDIF»
        «IF !columnAnnotationParts(col).empty»
        @Column(«columnAnnotationParts(col).join(", ")»)
        «ENDIF»
        «IF col.digits !== null»
        @Digits(«digitsParts(col.digits).join(", ")»)
        «ENDIF»
        «IF col.decimalMin !== null»
        @DecimalMin(«decimalMinParts(col.decimalMin).join(", ")»)
        «ENDIF»
        private «col.javaType.simpleType» «col.fieldName»;

    '''

    def private CharSequence accessors(JpaColumn col) '''
        public «col.javaType.simpleType» get«col.fieldName.toFirstUpper»() {
            return «col.fieldName»;
        }

        public void set«col.fieldName.toFirstUpper»(final «col.javaType.simpleType» «col.fieldName») {
            this.«col.fieldName» = «col.fieldName»;
        }

    '''

    def private List<String> tableAnnotationParts(JpaTable table) {
        val List<String> parts = new ArrayList<String>()
        if (!table.name.nullOrEmpty) parts.add('name = "' + table.name + '"')
        if (!table.catalog.nullOrEmpty) parts.add('catalog = "' + table.catalog + '"')
        if (!table.schema.nullOrEmpty) parts.add('schema = "' + table.schema + '"')
        if (!table.uniqueConstraints.empty)
            parts.add("uniqueConstraints = { " + table.uniqueConstraints.map[uniqueConstraint].join(", ") + " }")
        if (!table.indexes.empty)
            parts.add("indexes = { " + table.indexes.map[index].join(", ") + " }")
        return parts
    }

    def private String uniqueConstraint(JpaUniqueConstraint uc) {
        val List<String> parts = new ArrayList<String>()
        if (!uc.name.nullOrEmpty) parts.add('name = "' + uc.name + '"')
        parts.add("columnNames = { " + uc.columnNames.map['"' + it + '"'].join(", ") + " }")
        return "@UniqueConstraint(" + parts.join(", ") + ")"
    }

    def private String index(JpaIndex idx) {
        val List<String> parts = new ArrayList<String>()
        if (!idx.name.nullOrEmpty) parts.add('name = "' + idx.name + '"')
        if (!idx.columnList.nullOrEmpty) parts.add('columnList = "' + idx.columnList + '"')
        if (idx.unique !== null) parts.add("unique = " + idx.unique)
        return "@Index(" + parts.join(", ") + ")"
    }

    def private List<String> columnAnnotationParts(JpaColumn col) {
        val List<String> parts = new ArrayList<String>()
        if (!col.name.nullOrEmpty) parts.add('name = "' + col.name + '"')
        if (col.unique !== null) parts.add("unique = " + col.unique)
        if (col.nullable !== null) parts.add("nullable = " + col.nullable)
        if (col.insertable !== null) parts.add("insertable = " + col.insertable)
        if (col.updatable !== null) parts.add("updatable = " + col.updatable)
        if (!col.columnDefinition.nullOrEmpty) parts.add('columnDefinition = "' + col.columnDefinition + '"')
        if (!col.table.nullOrEmpty) parts.add('table = "' + col.table + '"')
        if (col.length !== null) parts.add("length = " + col.length)
        if (col.precision !== null) parts.add("precision = " + col.precision)
        if (col.scale !== null) parts.add("scale = " + col.scale)
        return parts
    }

    def private List<String> digitsParts(JpaDigits d) {
        val List<String> parts = new ArrayList<String>()
        if (d.integer !== null) parts.add("integer = " + d.integer)
        if (d.fraction !== null) parts.add("fraction = " + d.fraction)
        return parts
    }

    def private List<String> decimalMinParts(JpaDecimalMin dm) {
        val List<String> parts = new ArrayList<String>()
        if (!dm.value.nullOrEmpty) parts.add('value = "' + dm.value + '"')
        if (dm.inclusive !== null) parts.add("inclusive = " + dm.inclusive)
        return parts
    }

    def private String simpleType(String javaType) {
        if (javaType === null) {
            return "Object"
        }
        val int p = javaType.lastIndexOf('.')
        if (p < 0) javaType else javaType.substring(p + 1)
    }

    def private void addImports(CodeSnippetContext ctx, JpaTable table) {
        ctx.requiresImport("jakarta.persistence.Entity")
        if (!tableAnnotationParts(table).empty) {
            ctx.requiresImport("jakarta.persistence.Table")
        }
        if (!table.uniqueConstraints.empty) {
            ctx.requiresImport("jakarta.persistence.UniqueConstraint")
        }
        if (!table.indexes.empty) {
            ctx.requiresImport("jakarta.persistence.Index")
        }
        for (col : table.columns) {
            if (col.id !== null && col.id) {
                ctx.requiresImport("jakarta.persistence.Id")
            }
            if (!columnAnnotationParts(col).empty) {
                ctx.requiresImport("jakarta.persistence.Column")
            }
            if (col.digits !== null) {
                ctx.requiresImport("jakarta.validation.constraints.Digits")
            }
            if (col.decimalMin !== null) {
                ctx.requiresImport("jakarta.validation.constraints.DecimalMin")
            }
            if (col.javaType !== null && col.javaType.contains(".")) {
                ctx.requiresImport(col.javaType)
            }
        }
    }

}
