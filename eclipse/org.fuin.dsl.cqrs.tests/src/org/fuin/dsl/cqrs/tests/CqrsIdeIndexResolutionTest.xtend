package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Map
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.IResourceDescription
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Resolution has to work with what an IDE actually has: an <em>index</em> that is complete over the
 * project, and a <em>resource set</em> that holds the one file being edited and nothing else.
 *
 * <p>Every other test here loads the whole model into one resource set, which is what a headless
 * SrcGen4J run does - and there the two pools are the same object, so code that reads the wrong one
 * still gets the right answer. An Xtext editor keeps them far apart: it opens one file, and finds
 * everything else through the index the builder wrote. Anything a model needs that is neither in that
 * one file nor in the index has to be pulled in deliberately.</p>
 *
 * <p>Three things are, and each of them left every type of a <code>dependency</code> unresolvable in
 * Eclipse while the headless verifier reported no issue at all:</p>
 *
 * <ul>
 * <li>the <code>dependency</code> itself, when it is declared on the context in <em>another</em> file
 * - nothing exports it, so the files declaring that context have to be read;</li>
 * <li>the models beside a model that was read out of the archive - they are in no index either;</li>
 * <li>a model read out of the archive seeing the elements of its own file - the index cannot answer
 * for a resource it does not contain.</li>
 * </ul>
 *
 * <p>The index is modelled the way Xtext models it itself: a {@link ResourceDescriptionsData} snapshot
 * installed on the resource set. That is the same object an Eclipse builder installs, so it behaves
 * like the real one - the archive's models are not in it, because the builder never saw them.</p>
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsIdeIndexResolutionTest {

	static val COORDINATE = "org.fuin.test:cqrs-model:1.0.0"

	/** Provides the type the other archive model needs, and is a second file of the same archive. */
	static val REMOTE_TYPES = '''
		context remote {
			module com.acme.types {
				type Text
			}
		}
	'''

	/**
	 * References the model beside it (<code>Text</code>) and an element of its own file
	 * (<code>Money</code> from <code>Price</code>).
	 */
	static val REMOTE_BILLING = '''
		context remote {
			module com.acme.billing {
				import remote.com.acme.types.*

				value-object Money {
					Text amount
				}

				value-object Price {
					Money net
				}
			}
		}
	'''

	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject IResourceDescription.Manager descriptionManager
	@Inject CqrsModelArchives archives

	@BeforeEach
	def void forgetPreviousResolutions() {
		archives.invalidate
	}

	@AfterEach
	def void restoreResolver() {
		CqrsArtifactResolvers.set(null)
	}

	/**
	 * The coordinate is declared on the context in <code>declaration.cqrs</code> and the editor has
	 * <code>usage.cqrs</code> open. Nothing but the index says the two belong together, so this is what
	 * fails in Eclipse while a headless run - one resource set holding both - never notices.
	 */
	@Test
	def void aDependencyDeclaredInAnotherFileIsFound() {
		val root = twoFileProject()
		val editor = open(root, "usage.cqrs")

		assertResolves(editor)
		Assertions.assertTrue(editor.resourceSet.resources.exists[CqrsModelArchives.isArchived(it.URI)],
			"the artifact must have been resolved, which only the other file's 'dependency' asks for")
	}

	/**
	 * A model of the artifact must reach the models beside it and the elements of its own file. It is
	 * in no index - the builder never saw it - so neither can come from there.
	 */
	@Test
	def void aModelReadOutOfTheArchiveResolvesItsNeighboursAndItself() {
		val root = twoFileProject()
		val editor = open(root, "usage.cqrs")
		assertResolves(editor)

		val archived = editor.resourceSet.resources.filter[CqrsModelArchives.isArchived(it.URI)].toList
		Assertions.assertEquals(2, archived.size, "both models of the archive must have been read")
		for (model : archived) {
			Assertions.assertTrue(model.errors.empty,
				'''«model.URI.lastSegment» must resolve: «model.errors.join(", ")»''')
		}
	}

	/**
	 * The same for a <code>local</code> directory outside the project. Its models are plain files, but
	 * files the builder never saw, so they are as absent from the index as an entry of a zip - and a
	 * work in progress read this way is exactly where a model gets split over several files.
	 */
	@Test
	def void aModelReadOutOfALocalDirectoryResolvesItsNeighboursAndItself() {
		val root = Files.createTempDirectory("ide-index-local")
		val provider = Files.createDirectories(root.resolve("provider"))
		Files.writeString(provider.resolve("types.cqrs"), REMOTE_TYPES.toString)
		Files.writeString(provider.resolve("money.cqrs"), REMOTE_BILLING.toString)
		// A 'local' clause must never reach Maven, so there is nothing to resolve with.
		CqrsArtifactResolvers.set([ groupId, artifactId, version |
			throw new IllegalStateException("must not resolve when 'local' is declared")
		])

		val project = Files.createDirectories(root.resolve("project"))
		Files.writeString(project.resolve("declaration.cqrs"), '''
			context consumer {
				dependency "«COORDINATE»" local "../provider"
			}
		'''.toString)
		Files.writeString(project.resolve("usage.cqrs"), '''
			context consumer {
				module com.acme.sales {
					import remote.com.acme.billing.*

					value-object Order {
						Price total
					}
				}
			}
		'''.toString)

		val editor = open(project, "usage.cqrs")
		assertResolves(editor)

		val read = editor.resourceSet.resources.filter[URI.toFileString.startsWith(provider.toString)].toList
		Assertions.assertEquals(2, read.size, "both models of the local directory must have been read")
		for (model : read) {
			Assertions.assertTrue(model.errors.empty,
				'''«model.URI.lastSegment» must resolve: «model.errors.join(", ")»''')
		}
	}

	// ---- helpers ---------------------------------------------------------

	/**
	 * A project whose context is split over two files: one declaring the <code>dependency</code>, one
	 * importing and using what it provides.
	 *
	 * @return Directory holding both.
	 */
	private def Path twoFileProject() {
		val root = Files.createTempDirectory("ide-index")
		val archive = root.resolve("cqrs-model-1.0.0.zip")
		Files.write(archive, zip(#{
			"model/public/types.cqrs" -> REMOTE_TYPES.toString,
			"model/public/money.cqrs" -> REMOTE_BILLING.toString
		}))
		CqrsArtifactResolvers.set([ groupId, artifactId, version | archive ])

		Files.writeString(root.resolve("declaration.cqrs"), '''
			context consumer {
				dependency "«COORDINATE»"
			}
		'''.toString)
		Files.writeString(root.resolve("usage.cqrs"), '''
			context consumer {
				module com.acme.sales {
					import remote.com.acme.billing.*

					value-object Order {
						Price total
					}
				}
			}
		'''.toString)
		return root
	}

	/**
	 * Opens one file the way an editor does: a resource set of its own, holding nothing but that file,
	 * with an index over every file of the project installed on it.
	 *
	 * @param root Project directory.
	 * @param fileName File to open.
	 *
	 * @return The opened resource, fully resolved.
	 */
	private def Resource open(Path root, String fileName) {
		val workspace = resourceSetProvider.get
		val files = Files.list(root).filter[toString.endsWith(".cqrs")].sorted.toList
		for (file : files) {
			workspace.getResource(URI.createFileURI(file.toString), true)
		}
		val index = new ResourceDescriptionsData(
			new java.util.ArrayList(workspace.resources).map[descriptionManager.getResourceDescription(it)])

		val editor = resourceSetProvider.get
		ResourceDescriptionsData.ResourceSetAdapter.installResourceDescriptionsData(editor, index)
		val opened = editor.getResource(URI.createFileURI(root.resolve(fileName).toString), true)
		EcoreUtil.resolveAll(editor)
		return opened
	}

	/** Nothing in the opened file is left unresolved. */
	private def void assertResolves(Resource opened) {
		Assertions.assertTrue(opened.errors.empty,
			'''Unexpected errors in «opened.URI.lastSegment»: «opened.errors.join(", ")»''')
	}

	private def byte[] zip(Map<String, String> entries) {
		val bytes = new ByteArrayOutputStream
		val zip = new ZipOutputStream(bytes)
		for (entry : entries.entrySet) {
			zip.putNextEntry(new ZipEntry(entry.key))
			zip.write(entry.value.getBytes(StandardCharsets.UTF_8))
			zip.closeEntry
		}
		zip.close
		return bytes.toByteArray
	}
}
