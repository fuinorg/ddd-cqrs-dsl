package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.AbstractEntity;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Event;
import org.fuin.dsl.cqrs.extensions.CqrsEventExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that the aggregate an event belongs to is still found when the two are declared in
 * <em>different files</em> of the same module.
 * 
 * <p>A module may be split across files, and a model that publishes only part of itself has to be
 * split - the aggregate goes into the private part, the events it fires stay public. Whether an
 * event has an owner is what makes it a domain event carrying that aggregate's id rather than a
 * plain one, so resolving it per file would silently change the generated code the moment a model
 * is split.</p>
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsSplitModuleEventOwnerTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  /**
   * The aggregate declares "fires", and it lives in the other file.
   */
  @Test
  public void findsTheFiringAggregateInAnotherFile() {
    try {
      final Path root = Files.createTempDirectory("split-module-fires");
      final XtextResourceSet resourceSet = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module ordering {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate-id OrderId identifies Order base String {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object OrderName base String {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("event OrderCreatedEvent {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("OrderName name");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel public_ = this.parse(root, "public.cqrs", resourceSet, _builder);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context shop {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module ordering {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("aggregate Order identifier OrderId {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("constructor create fires OrderCreatedEvent {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t\t");
      _builder_1.append("OrderName name");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      this.parse(root, "private.cqrs", resourceSet, _builder_1);
      EcoreUtil.resolveAll(resourceSet);
      final Event event = IterableExtensions.<Event>head(Iterables.<Event>filter(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(public_.getContexts()).getModules()).getElements(), Event.class));
      final AbstractEntity owner = CqrsEventExtensions.getEntity(event);
      Assertions.assertNotNull(owner, 
        "the aggregate firing the event must be found although it lives in another file");
      Assertions.assertEquals("Order", owner.getName());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * An event nothing fires stays a plain event - the wider search must not invent an owner.
   */
  @Test
  public void keepsAnUnownedEventPlain() {
    try {
      final Path root = Files.createTempDirectory("split-module-unowned");
      final XtextResourceSet resourceSet = this.resourceSetProvider.get();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context shop {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module ordering {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object OrderName base String {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("event SomethingHappenedEvent {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("OrderName name");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parse(root, "public.cqrs", resourceSet, _builder);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context shop {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module ordering {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("aggregate-id OrderId identifies Order base String {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("aggregate Order identifier OrderId {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("constructor create {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t\t");
      _builder_1.append("OrderName name");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      this.parse(root, "private.cqrs", resourceSet, _builder_1);
      EcoreUtil.resolveAll(resourceSet);
      final Event event = IterableExtensions.<Event>head(Iterables.<Event>filter(IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(model.getContexts()).getModules()).getElements(), Event.class));
      Assertions.assertNull(CqrsEventExtensions.getEntity(event), 
        "an event no aggregate fires must stay a plain event");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private DomainModel parse(final Path root, final String name, final XtextResourceSet resourceSet, final CharSequence text) {
    try {
      return this.parseHelper.parse(text, URI.createFileURI(root.resolve(name).toString()), resourceSet);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
