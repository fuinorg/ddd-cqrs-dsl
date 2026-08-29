package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies where 'own-path' may be used.
 * 
 * <p>It exists because an entity's identifier does not address it: the same one is assigned inside every
 * root, so a service asked to look the entity up somewhere else cannot be handed 'own-id'. What it reads
 * as is the declared 'entity-id-path' for the chain - so the three ways it can be wrong are a carrier
 * with no root, a carrier that does not exist yet, and a chain the model never declared a path for.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsOwnPathValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testAnEntityWithADeclaredPathMayNameIt() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("method close business-rules MustBeOpen(own-path) fires ClosedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event ClosedEvent { message \"Closed\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(CqrsOwnPathValidationTest.PATH, _builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testWithoutADeclaredPathThereIsNoTypeToReadItAs() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("method close business-rules MustBeOpen(own-path) fires ClosedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event ClosedEvent { message \"Closed\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model("", _builder.toString())), CqrsDslPackage.Literals.ENTITY_PATH_ARGUMENT, 
        CqrsDslValidator.RULE_OWN_PATH_HAS_NO_TYPE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testACreatingOperationHasNoCarrierToAddress() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("constructor open business-rules MustBeOpen(own-path) fires OpenedEvent {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("event OpenedEvent { message \"Opened\" }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(CqrsOwnPathValidationTest.PATH, _builder.toString())), CqrsDslPackage.Literals.ENTITY_PATH_ARGUMENT, 
        CqrsDslValidator.RULE_OWN_PATH_IN_CONSTRUCTOR);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAnAggregateIsAddressedByItsOwnId() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context p {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module c.n {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Refused because the thing is closed. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("exception ClosedException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The thing that is closed. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("BoxId thing");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"${thing} is closed\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Makes sure the box is open. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("business-rule MustBeOpen exception ClosedException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("/** The box being acted on. */");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("BoxId thing");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("consistency strong");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("requires thing != null");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate-id BoxId identifies Box base String { }");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate Box identifier BoxId {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("method close business-rules MustBeOpen(own-path) fires ClosedEvent {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("event ClosedEvent { message \"Closed\" }");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(_builder), CqrsDslPackage.Literals.ENTITY_PATH_ARGUMENT, 
        CqrsDslValidator.RULE_OWN_PATH_NOT_ON_ENTITY);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private static final String PATH = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** How a thing inside a box is addressed. */");
      _builder.newLine();
      _builder.append("entity-id-path ThingPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("BoxId / ThingId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();

  private String model(final String path, final String operation) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module c.n {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id BoxId identifies Box base String { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("entity-id ThingId identifies Thing base String { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append(path, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Refused because the thing is closed. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception ClosedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The thing that is closed. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("ThingId thing");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"${thing} is closed\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Makes sure the thing is open. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("business-rule MustBeOpen exception ClosedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The thing being acted on. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("ThingId thing");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("requires thing != null");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Box identifier BoxId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** What it is called. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("entity Thing identifier ThingId root Box {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** What it is called. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append(operation, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
