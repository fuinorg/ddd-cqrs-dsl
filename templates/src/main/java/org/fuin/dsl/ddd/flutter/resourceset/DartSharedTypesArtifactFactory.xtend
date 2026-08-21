package org.fuin.dsl.ddd.flutter.resourceset

import java.util.ArrayDeque
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntityId
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.flutter.base.DartNames
import org.fuin.dsl.ddd.flutter.entityid.DartIdArtifactFactory
import org.fuin.dsl.ddd.flutter.enumobject.DartEnumArtifactFactory
import org.fuin.dsl.ddd.flutter.valueobject.DartRowArtifactFactory
import org.fuin.dsl.ddd.flutter.valueobject.DartValueObjectArtifactFactory
import org.fuin.dsl.ddd.gen.base.TypeKeys
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.GenerateException
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.commons.Variable

import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Writes the types this model uses but does not own.
 *
 * <p><b>Why this exists at all.</b> A model reaches into another context's shared types - an
 * <code>EmailAddress</code>, a <code>MediaType</code>, the <code>VersionedEntityIdPath</code> every
 * projected row carries - and the JVM target never regenerates them, because they arrive compiled in
 * that context's jar. Dart has no jar. Nothing publishes a Dart package for a dependency model, so a
 * type this client references and does not generate is a type that does not exist: the import resolves
 * to a file nothing wrote.
 *
 * <p>So the rule the JVM target follows - generate only what the source directory declares - is right
 * there and wrong here, for a reason that is about packaging rather than about models. This target
 * generates a foreign type into its own package, where it is the client's copy of somebody else's
 * contract, exactly as the jar is on the JVM side.
 *
 * <p><b>Only what is reached.</b> A dependency model holds far more than any one client uses, so this
 * walks out from what the model itself declares and takes only the types that walk reaches, then what
 * those reach in turn. Emitting the whole of a shared context would fill the package with types no
 * screen will ever name.
 *
 * <p><b>Why a resource-set factory.</b> Not a stylistic choice: the generator hands per-element
 * factories only primary elements, and calls resource-set factories with the whole model regardless.
 * This is the one place in the target that can see a foreign type at all, which is why the emitting is
 * done here by delegation rather than by each factory deciding for itself.
 */
class DartSharedTypesArtifactFactory extends AbstractDartSource<ResourceSet> {

    /** Where the generated files go. Named outright, as in the catalogue: a foreign type has no entry
     * in the target mapping, which is keyed by what the local model declares. */
    static val MODULE = "flutter.contract"

    /** See {@link #MODULE}. */
    static val FOLDER = "genMainDart"

    override getModelType() {
        typeof(ResourceSet)
    }

    override getTypeKey() {
        TypeKeys.DART_SHARED_TYPES
    }

    override isIncremental() {
        false
    }

    override create(ResourceSet resourceSet, Map<String, Object> context, boolean preparationRun)
            throws GenerateException {

        if (preparationRun) {
            return null
        }

        val out = new ArrayList<GeneratedArtifact>()
        for (type : reachedFrom(resourceSet).values) {
            val artifacts = emit(type, context)
            if (artifacts !== null) {
                out.addAll(artifacts)
            }
        }
        return if(out.empty) null else out
    }

    /**
     * Runs whichever factory claims the type.
     *
     * <p>Each is asked and each answers for itself, rather than this deciding by shape: what makes a
     * value object a wrapper and not a row is that factory's business, and a copy of the test here
     * would be a second place to keep it right.
     *
     * @param type Foreign type to write.
     * @param context Generation context.
     *
     * @return What was written, or <code>null</code> when no factory claimed it.
     */
    def private List<GeneratedArtifact> emit(EObject type, Map<String, Object> context) {
        for (factory : factoriesFor(type)) {
            val artifacts = factory.create(type, context, false)
            if (artifacts !== null && !artifacts.empty) {
                return artifacts
            }
        }
        return null
    }

    def private List<ArtifactFactory<EObject>> factoriesFor(EObject type) {
        val out = new ArrayList<ArtifactFactory<EObject>>()
        switch (type) {
            AbstractEntityId: out.add(init(new DartIdArtifactFactory(), "dartId"))
            EnumObject: out.add(init(new DartEnumArtifactFactory(), "dartEnum"))
            ValueObject: {
                out.add(init(new DartValueObjectArtifactFactory(), "dartValueObject"))
                out.add(init(new DartRowArtifactFactory(), "dartRow"))
            }
        }
        return out
    }

    /** Gives a delegate the same package and target this factory was configured with. */
    def private <T> ArtifactFactory<EObject> init(ArtifactFactory<T> factory, String artifact) {
        val config = new ArtifactFactoryConfig(artifact, factory.class.name, MODULE, FOLDER)
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, dartPackage))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory as ArtifactFactory<EObject>
    }

    /**
     * The foreign types this model reaches, and the ones those reach in turn.
     *
     * @param resourceSet Everything that was parsed, the model's own resources and the dependencies
     *                    loaded while resolving its references.
     *
     * @return Types to write, keyed by where they will be written, in a stable order.
     */
    def private Map<String, EObject> reachedFrom(ResourceSet resourceSet) {
        val found = new LinkedHashMap<String, EObject>()
        val pending = new ArrayDeque<EObject>()

        val contents = resourceSet.allContents
        while (contents.hasNext) {
            val notifier = contents.next
            if (notifier instanceof EObject && isPrimary(notifier as EObject)) {
                pending.add(notifier as EObject)
            }
        }

        while (!pending.empty) {
            for (referenced : pending.poll.eCrossReferences) {
                if (wanted(referenced)) {
                    val key = DartNames.file(referenced.module, nameOf(referenced))
                    if (!found.containsKey(key)) {
                        found.put(key, referenced)
                        // Its own attributes may name further foreign types, so it is walked as well.
                        pending.add(referenced)
                        val own = referenced.eAllContents
                        while (own.hasNext) {
                            pending.add(own.next)
                        }
                    }
                }
            }
        }
        return found
    }

    def private boolean wanted(EObject obj) {
        if (isPrimary(obj)) {
            return false
        }
        return obj instanceof ValueObject || obj instanceof EnumObject || obj instanceof AbstractEntityId
    }

    def private static String nameOf(EObject obj) {
        switch (obj) {
            ValueObject: obj.name
            EnumObject: obj.name
            AbstractEntityId: obj.name
            default: null
        }
    }

}
