package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies which attributes a 'base' allows.
 * 
 * <p>A 'base' says the type wraps a single value, and the generator writes it as exactly that: one
 * declared attribute holds the value and the constructor taking it is what the generated valueOf()
 * and the converters call. With no attribute there is neither a constructor nor anything for
 * asBaseType() to return; with several and a base the generator instantiates from, the emitted
 * one-argument call matches no constructor. Both used to reach javac as a broken write-once file.
 * 
 * <p>An entity id on Integer or UUID and an aggregate id on UUID are exempt: for those the generator
 * emits the complete class from the base type alone, so there is nothing to declare.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsBaseTypeAttributesValidationTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void aBaseWithTheOneAttributeHoldingItIsFine() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("value-object Email base String {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("String value");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void anInstantiatedBaseWithNoAttributeAtAllIsRefused() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("value-object Key base UUID {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder)), CqrsDslPackage.Literals.VALUE_OBJECT, CqrsDslValidator.BASE_TYPE_NEEDS_ONE_ATTRIBUTE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void aStringBaseWithNoAttributeIsLeftToItsWriteOnceClass() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("value-object Email base String {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void aStringBaseMayPackSeveralAttributes() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("value-object PhoneNumber base String {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("String country");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("String number");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void anInstantiatedBaseMayNot() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("value-object Pair base UUID {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UUID a");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("UUID b");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder)), CqrsDslPackage.Literals.VALUE_OBJECT, CqrsDslValidator.BASE_TYPE_NEEDS_ONE_ATTRIBUTE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void anEntityIdOnIntegerOrUuidIsWrittenWholeFromItsBase() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("aggregate-id ThingId identifies Thing base UUID {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("aggregate Thing identifier ThingId {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("entity-id PartId identifies Part base Integer {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("entity Part identifier PartId root Thing {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("entity-id SlotId identifies Slot base UUID {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("entity Slot identifier SlotId root Thing {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoIssues(this.parseHelper.parse(this.model(_builder)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void anAggregateIdOnIntegerNeedsIt() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("aggregate-id ThingId identifies Thing base Integer {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("aggregate Thing identifier ThingId {");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder)), CqrsDslPackage.Literals.AGGREGATE_ID, CqrsDslValidator.BASE_TYPE_NEEDS_ONE_ATTRIBUTE);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private String model(final CharSequence body) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context p {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module c.n {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type UUID");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append(body, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
