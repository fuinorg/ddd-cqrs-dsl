package org.fuin.dsl.ddd.gen.base

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.Hint
import org.fuin.dsl.cqrs.cqrsDsl.JSON
import org.fuin.dsl.cqrs.cqrsDsl.JsonArray
import org.fuin.dsl.cqrs.cqrsDsl.JsonBoolean
import org.fuin.dsl.cqrs.cqrsDsl.JsonNumber
import org.fuin.dsl.cqrs.cqrsDsl.JsonObject
import org.fuin.dsl.cqrs.cqrsDsl.JsonString

/**
 * Strongly typed view of a "JpaHint" {@link Hint} that describes one or more JPA tables. It walks the
 * parsed EMF JSON model of the hint (like {@link SrcGen4JHint} does for the "SrcGen4J" hint) and maps
 * it to plain typed objects. Unlike "SrcGen4J" this is a <em>data</em> hint - it carries the table and
 * column definitions themselves - and is expected to be declared inside a {@code view} body, so the
 * generated classes end up in the view's package.
 * <p>
 * Expected structure (every field maps to an attribute of {@code @Table} / {@code @Column} or to the
 * {@code @Digits} / {@code @DecimalMin} bean-validation constraints):
 * <pre>
 * hint JpaHint {
 *     "tables": [
 *         {
 *             "className": "Customer",
 *             "name": "CUSTOMER", "catalog": "", "schema": "",
 *             "uniqueConstraints": [ { "name": "UQ_EMAIL", "columnNames": ["EMAIL"] } ],
 *             "indexes":          [ { "name": "IX_NAME", "columnList": "LAST_NAME", "unique": false } ],
 *             "columns": [
 *                 {
 *                     "fieldName": "amount", "javaType": "java.math.BigDecimal", "id": false,
 *                     "name": "AMOUNT", "unique": false, "nullable": false,
 *                     "insertable": true, "updatable": true, "columnDefinition": "", "table": "",
 *                     "length": 255, "precision": 12, "scale": 2,
 *                     "digits": { "integer": 10, "fraction": 2 },
 *                     "decimalMin": { "value": "0.0", "inclusive": true }
 *                 }
 *             ]
 *         }
 *     ]
 * }
 * </pre>
 * There is deliberately no "package" key: the target package is the enclosing view's package.
 */
class JpaHint {

    val List<JpaTable> tables

    /**
     * Constructor with all data.
     *
     * @param tables Table definitions (value of the "tables" array) - Never <code>null</code>.
     */
    new(List<JpaTable> tables) {
        this.tables = tables
    }

    /** @return Table definitions (value of the "tables" array) - Never <code>null</code>, but may be empty. */
    def getTables() {
        tables
    }

    /**
     * Parses the JSON of the given hint into a strongly typed object.
     *
     * @param hint Hint to parse - Cannot be <code>null</code>.
     *
     * @return Strongly typed variant of the hint's JSON.
     */
    def static JpaHint parse(Hint hint) {
        parse(hint.json)
    }

    /**
     * Parses the given JSON value (expected to be an object) into a strongly typed object.
     *
     * @param json JSON object to parse - Cannot be <code>null</code>.
     *
     * @return Strongly typed variant of the JSON.
     */
    def static JpaHint parse(JSON json) {
        val root = json.asObject
        new JpaHint(root.arrayValues("tables").map[asObject.parseTable])
    }

    def private static JpaTable parseTable(JsonObject obj) {
        new JpaTable(
            obj.stringValue("className"),
            obj.stringValue("name"),
            obj.stringValue("catalog"),
            obj.stringValue("schema"),
            obj.arrayValues("uniqueConstraints").map[asObject.parseUniqueConstraint],
            obj.arrayValues("indexes").map[asObject.parseIndex],
            obj.arrayValues("columns").map[asObject.parseColumn]
        )
    }

    def private static JpaUniqueConstraint parseUniqueConstraint(JsonObject obj) {
        new JpaUniqueConstraint(
            obj.stringValue("name"),
            obj.arrayValues("columnNames").map[asStringValue]
        )
    }

    def private static JpaIndex parseIndex(JsonObject obj) {
        new JpaIndex(
            obj.stringValue("name"),
            obj.stringValue("columnList"),
            obj.booleanValue("unique")
        )
    }

    def private static JpaColumn parseColumn(JsonObject obj) {
        new JpaColumn(
            obj.stringValue("fieldName"),
            obj.stringValue("javaType"),
            obj.booleanValue("id"),
            obj.stringValue("name"),
            obj.booleanValue("unique"),
            obj.booleanValue("nullable"),
            obj.booleanValue("insertable"),
            obj.booleanValue("updatable"),
            obj.stringValue("columnDefinition"),
            obj.stringValue("table"),
            obj.intValue("length"),
            obj.intValue("precision"),
            obj.intValue("scale"),
            obj.objectValue("digits")?.parseDigits,
            obj.objectValue("decimalMin")?.parseDecimalMin
        )
    }

    def private static JpaDigits parseDigits(JsonObject obj) {
        new JpaDigits(obj.intValue("integer"), obj.intValue("fraction"))
    }

    def private static JpaDecimalMin parseDecimalMin(JsonObject obj) {
        new JpaDecimalMin(obj.stringValue("value"), obj.booleanValue("inclusive"))
    }

    // ---- JSON navigation helpers (used as extension methods) ----

    def private static JsonObject asObject(JSON json) {
        if (json instanceof JsonObject) {
            json
        } else {
            throw new IllegalArgumentException("Expected a JSON object, but was: " + json?.eClass?.name)
        }
    }

    def private static JSON member(JsonObject obj, String key) {
        obj.members.findFirst[member|member.key == key]?.value
    }

    def private static String stringValue(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            null
        } else if (value instanceof JsonString) {
            value.value
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON string for key '" + key + "', but was: " + value.eClass.name)
        }
    }

    def private static Boolean booleanValue(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            null
        } else if (value instanceof JsonBoolean) {
            Boolean.valueOf("true" == value.value)
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON boolean for key '" + key + "', but was: " + value.eClass.name)
        }
    }

    def private static Integer intValue(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            return null
        } else if (value instanceof JsonNumber) {
            try {
                return Integer.valueOf(value.value.trim)
            } catch (NumberFormatException ex) {
                return null
            }
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON number for key '" + key + "', but was: " + value.eClass.name)
        }
    }

    def private static JsonObject objectValue(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            null
        } else if (value instanceof JsonObject) {
            value
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON object for key '" + key + "', but was: " + value.eClass.name)
        }
    }

    def private static String asStringValue(JSON json) {
        if (json instanceof JsonString) {
            json.value
        } else {
            throw new IllegalArgumentException("Expected a JSON string, but was: " + json?.eClass?.name)
        }
    }

    def private static List<JSON> arrayValues(JsonObject obj, String key) {
        val value = obj.member(key)
        if (value === null) {
            newArrayList
        } else if (value instanceof JsonArray) {
            value.elements
        } else {
            throw new IllegalArgumentException(
                "Expected a JSON array for key '" + key + "', but was: " + value.eClass.name)
        }
    }

}

/** A single JPA table: the generated class name plus its {@code @Table} attributes and columns. */
class JpaTable {

    val String className
    val String name
    val String catalog
    val String schema
    val List<JpaUniqueConstraint> uniqueConstraints
    val List<JpaIndex> indexes
    val List<JpaColumn> columns

    new(String className, String name, String catalog, String schema,
        List<JpaUniqueConstraint> uniqueConstraints, List<JpaIndex> indexes, List<JpaColumn> columns) {
        this.className = className
        this.name = name
        this.catalog = catalog
        this.schema = schema
        this.uniqueConstraints = uniqueConstraints
        this.indexes = indexes
        this.columns = columns
    }

    /** @return Generated Java class name (value of the "className" key) or <code>null</code>. */
    def getClassName() { className }

    /** @return {@code @Table} "name" or <code>null</code>. */
    def getName() { name }

    /** @return {@code @Table} "catalog" or <code>null</code>. */
    def getCatalog() { catalog }

    /** @return {@code @Table} "schema" or <code>null</code>. */
    def getSchema() { schema }

    /** @return {@code @Table} "uniqueConstraints" - Never <code>null</code>, but may be empty. */
    def getUniqueConstraints() { uniqueConstraints }

    /** @return {@code @Table} "indexes" - Never <code>null</code>, but may be empty. */
    def getIndexes() { indexes }

    /** @return Column definitions (value of the "columns" array) - Never <code>null</code>, but may be empty. */
    def getColumns() { columns }

}

/** A {@code @UniqueConstraint} entry of a {@code @Table}. */
class JpaUniqueConstraint {

    val String name
    val List<String> columnNames

    new(String name, List<String> columnNames) {
        this.name = name
        this.columnNames = columnNames
    }

    /** @return Constraint "name" or <code>null</code>. */
    def getName() { name }

    /** @return "columnNames" - Never <code>null</code>, but may be empty. */
    def getColumnNames() { columnNames }

}

/** An {@code @Index} entry of a {@code @Table}. */
class JpaIndex {

    val String name
    val String columnList
    val Boolean unique

    new(String name, String columnList, Boolean unique) {
        this.name = name
        this.columnList = columnList
        this.unique = unique
    }

    /** @return Index "name" or <code>null</code>. */
    def getName() { name }

    /** @return "columnList" or <code>null</code>. */
    def getColumnList() { columnList }

    /** @return "unique" or <code>null</code> when not set. */
    def getUnique() { unique }

}

/** A single column: the Java field plus its {@code @Column} attributes and optional bean-validation. */
class JpaColumn {

    val String fieldName
    val String javaType
    val Boolean id
    val String name
    val Boolean unique
    val Boolean nullable
    val Boolean insertable
    val Boolean updatable
    val String columnDefinition
    val String table
    val Integer length
    val Integer precision
    val Integer scale
    val JpaDigits digits
    val JpaDecimalMin decimalMin

    new(String fieldName, String javaType, Boolean id, String name, Boolean unique, Boolean nullable,
        Boolean insertable, Boolean updatable, String columnDefinition, String table, Integer length,
        Integer precision, Integer scale, JpaDigits digits, JpaDecimalMin decimalMin) {
        this.fieldName = fieldName
        this.javaType = javaType
        this.id = id
        this.name = name
        this.unique = unique
        this.nullable = nullable
        this.insertable = insertable
        this.updatable = updatable
        this.columnDefinition = columnDefinition
        this.table = table
        this.length = length
        this.precision = precision
        this.scale = scale
        this.digits = digits
        this.decimalMin = decimalMin
    }

    /** @return Java field name (value of the "fieldName" key) or <code>null</code>. */
    def getFieldName() { fieldName }

    /** @return Java type of the field (value of the "javaType" key) or <code>null</code>. */
    def getJavaType() { javaType }

    /** @return TRUE when the column is annotated with {@code @Id}; <code>null</code>/FALSE otherwise. */
    def getId() { id }

    /** @return {@code @Column} "name" or <code>null</code>. */
    def getName() { name }

    /** @return {@code @Column} "unique" or <code>null</code>. */
    def getUnique() { unique }

    /** @return {@code @Column} "nullable" or <code>null</code>. */
    def getNullable() { nullable }

    /** @return {@code @Column} "insertable" or <code>null</code>. */
    def getInsertable() { insertable }

    /** @return {@code @Column} "updatable" or <code>null</code>. */
    def getUpdatable() { updatable }

    /** @return {@code @Column} "columnDefinition" or <code>null</code>. */
    def getColumnDefinition() { columnDefinition }

    /** @return {@code @Column} "table" or <code>null</code>. */
    def getTable() { table }

    /** @return {@code @Column} "length" or <code>null</code>. */
    def getLength() { length }

    /** @return {@code @Column} "precision" or <code>null</code>. */
    def getPrecision() { precision }

    /** @return {@code @Column} "scale" or <code>null</code>. */
    def getScale() { scale }

    /** @return {@code @Digits} constraint or <code>null</code> when not set. */
    def getDigits() { digits }

    /** @return {@code @DecimalMin} constraint or <code>null</code> when not set. */
    def getDecimalMin() { decimalMin }

}

/** The {@code @Digits} bean-validation constraint. */
class JpaDigits {

    val Integer integer
    val Integer fraction

    new(Integer integer, Integer fraction) {
        this.integer = integer
        this.fraction = fraction
    }

    /** @return {@code @Digits} "integer" or <code>null</code>. */
    def getInteger() { integer }

    /** @return {@code @Digits} "fraction" or <code>null</code>. */
    def getFraction() { fraction }

}

/** The {@code @DecimalMin} bean-validation constraint. */
class JpaDecimalMin {

    val String value
    val Boolean inclusive

    new(String value, Boolean inclusive) {
        this.value = value
        this.inclusive = inclusive
    }

    /** @return {@code @DecimalMin} "value" (a string, as in JPA) or <code>null</code>. */
    def getValue() { value }

    /** @return {@code @DecimalMin} "inclusive" or <code>null</code>. */
    def getInclusive() { inclusive }

}
