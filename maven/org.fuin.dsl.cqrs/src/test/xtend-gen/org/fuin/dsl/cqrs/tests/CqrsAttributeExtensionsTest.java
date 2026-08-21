package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import java.util.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.fuin.dsl.cqrs.extensions.CqrsAttributeExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Turning an attribute into a parameter must leave the attribute as it was.
 * 
 * <p>These read like tests of nothing - convert something, then look at what was converted - and that
 * is the point. EMF gives an object one container, so assigning an attribute's <code>overridden</code>
 * wording to a freshly built parameter <em>moves</em> it: the parameter gains the wording and the
 * attribute in the parsed model silently loses it. Nothing fails, nothing is logged, and the damage is
 * not visible to the generator that caused it - only to whatever reads the same model afterwards, which
 * in a build with two targets is the second one.
 */
@InjectWith(CqrsDslInjectorProvider.class)
@ExtendWith(InjectionExtension.class)
@SuppressWarnings("all")
public class CqrsAttributeExtensionsTest {
  @Inject
  private ParseHelper<DomainModel> parser;

  @Inject
  private ValidationTestHelper validationTester;

  private static final String MODEL = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context a.b {");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("module m {");
      _builder.newLine();
      _builder.newLine();
      _builder.append("        ");
      _builder.append("type String");
      _builder.newLine();
      _builder.newLine();
      _builder.append("        ");
      _builder.append("type Integer");
      _builder.newLine();
      _builder.newLine();
      _builder.append("        ");
      _builder.append("/** The text length must lie between the two bounds, inclusive. */");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("constraint Length input String {");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("Integer min");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("Integer max");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("message \"must be between ${min} and ${max} characters\"");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("        ");
      _builder.append("value-object Row {");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("String name invariants Length(1, 100) {");
      _builder.newLine();
      _builder.append("                ");
      _builder.append("slabel \"N\"");
      _builder.newLine();
      _builder.append("                ");
      _builder.append("label \"Name\"");
      _builder.newLine();
      _builder.append("                ");
      _builder.append("tooltip \"What it is called\"");
      _builder.newLine();
      _builder.append("            ");
      _builder.append("}");
      _builder.newLine();
      _builder.append("        ");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("    ");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();

  @Test
  public void testAsParameterLeavesTheAttributesWordingWhereItWas() {
    final Attribute attribute = this.attribute();
    CqrsAttributeExtensions.asParameter(attribute);
    Assertions.assertNotNull(attribute.getOverridden(), "the attribute kept its wording");
    Assertions.assertEquals("Name", attribute.getOverridden().getMetaInfo().getLabel());
  }

  @Test
  public void testAsParameterLeavesTheAttributesInvariantsWhereTheyWere() {
    final Attribute attribute = this.attribute();
    CqrsAttributeExtensions.asParameter(attribute);
    Assertions.assertNotNull(attribute.getInvariants(), "the attribute kept its invariants");
    Assertions.assertEquals(1, attribute.getInvariants().getConstraintInstances().size());
  }

  @Test
  public void testAsParameterStillCarriesTheWordingOver() {
    Assertions.assertEquals("Name", CqrsAttributeExtensions.asParameter(this.attribute()).getOverridden().getMetaInfo().getLabel());
  }

  @Test
  public void testCopyWithNewNameLeavesTheOriginalIntact() {
    final Attribute attribute = this.attribute();
    final Attribute copy = CqrsAttributeExtensions.copyWithNewName(attribute, "other");
    Assertions.assertEquals("Name", copy.getOverridden().getMetaInfo().getLabel());
    Assertions.assertNotNull(attribute.getOverridden(), "the original kept its wording");
    Assertions.assertNotNull(attribute.getInvariants(), "the original kept its invariants");
  }

  private Attribute attribute() {
    try {
      final DomainModel model = this.parser.parse(CqrsAttributeExtensionsTest.MODEL);
      this.validationTester.assertNoErrors(model);
      final Function1<ValueObject, Boolean> _function = (ValueObject it) -> {
        String _name = it.getName();
        return Boolean.valueOf(Objects.equals(_name, "Row"));
      };
      final ValueObject row = IteratorExtensions.<ValueObject>findFirst(Iterators.<ValueObject>filter(model.eAllContents(), ValueObject.class), _function);
      return row.getAttributes().get(0);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
