package org.fuin.dsl.ddd.gen.resourceset

import java.math.BigDecimal
import java.math.BigInteger
import java.util.Currency
import java.util.Iterator
import java.util.Locale
import java.util.Map
import java.util.UUID
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.ddd.gen.base.AbstractSource
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.core.emf.CodeReferenceRegistry

import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*
import java.util.Collection
import java.util.List
import java.util.Set
import java.util.Collections

/**
 * Registers a set of external types. It does NOT create any source code.
 * The default module for the types is expected to be "&lt;context&gt;.types".
 * It's possible to change the module using the variable <code>module</code>
 * The "ddd" file that contains the types should look like this:<br>
 * <code>
 * context myctx {
 *     module types {
 *         type Byte
 *         type Short
 *         type Integer
 *         type Long
 *         type Float
 *         type Double
 *         type Boolean
 *         type Character
 *         type String
 *         type Date
 *         type Time
 *         type Timestamp
 *         type UUID
 *         type Currency
 *         type BigDecimal
 *         type BigInteger
 *         type Number
 *         type Locale
 *         type Object
 *         type EntityIdPath
 *         type Collection generics 1
 *         type List generics 1
 *         type Map generics 2
 *         type Set generics 1
 *         type Binary
 *     }
 * }
 * <code>
 * You can provide the following variables to customize the implementations:<br>
 * <code>types.Date</code> - Default="java.time.LocalDate"<br>
 * <code>types.Time</code> - Default="java.time.LocalTime"<br>
 * <code>types.Timestamp</code> - Default="java.time.ZonedDateTime"<br>
 * <code>types.UUID</code> - Default="java.util.UUID"<br>
 */
class CtxExternalTypes extends AbstractSource<ResourceSet> {

    override getModelType() {
        typeof(ResourceSet)
    }

    /** Registers external type mappings only - nothing is generated, so there is no artifact kind. */
    override getTypeKey() {
        null
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun) throws GenerateException {

        val pkg = getVar("module", "types")
        val dateType = getVar(pkg + ".Date", "java.time.LocalDate")
        val timeType = getVar(pkg + ".Time", "java.time.LocalDateTime")
        val dateTimeType = getVar(pkg + ".Timestamp", "java.time.ZonedDateTime")
        val uuidType = getVar(pkg + ".UUID", UUID.name)

        // Just registers the external types. The module holding them is identified by its last
        // segment (the "module" variable, "types" by default), so a module named "common.types"
        // registers its types under "<context>.common.types.X".
        val Iterator<Module> iter = resourceSet.getAllContents().filter(typeof(Module))
        while (iter.hasNext) {
            val Module ns = iter.next
            val nsName = ns.name
            if (nsName !== null && (nsName == pkg || nsName.endsWith("." + pkg))) {
            val name = org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.getContext(ns).name + "." + nsName
            val CodeReferenceRegistry refReg = context.codeReferenceRegistry
            refReg.putReference(name + ".Byte", Byte.name)
            refReg.putReference(name + ".Short", Short.name)
            refReg.putReference(name + ".Integer", Integer.name)
            refReg.putReference(name + ".Long", Long.name)
            refReg.putReference(name + ".Float", Float.name)
            refReg.putReference(name + ".Double", Double.name)
            refReg.putReference(name + ".Boolean", Boolean.name)
            refReg.putReference(name + ".Character", Character.name)
            refReg.putReference(name + ".String", String.name)
            refReg.putReference(name + ".Date", dateType)
            refReg.putReference(name + ".Time", timeType)
            refReg.putReference(name + ".Timestamp", dateTimeType)
            refReg.putReference(name + ".UUID", uuidType)
            refReg.putReference(name + ".Currency", Currency.name)
            refReg.putReference(name + ".BigDecimal", BigDecimal.name)
            refReg.putReference(name + ".BigInteger", BigInteger.name)
            refReg.putReference(name + ".Number", Number.name)
            refReg.putReference(name + ".Locale", Locale.name)
            refReg.putReference(name + ".Object", Object.name)
            refReg.putReference(name + ".EntityIdPath", "org.fuin.ddd4j.core.EntityIdPath")
            refReg.putReference(name + ".Collection", Collection.name)
            refReg.putReference(name + ".List", List.name)
            refReg.putReference(name + ".Map", Map.name)
            refReg.putReference(name + ".Set", Set.name)
            refReg.putReference(name + ".Binary", "byte[]")
            }

        }

        // Will never produce anything
        return Collections.emptyList();

    }

}
