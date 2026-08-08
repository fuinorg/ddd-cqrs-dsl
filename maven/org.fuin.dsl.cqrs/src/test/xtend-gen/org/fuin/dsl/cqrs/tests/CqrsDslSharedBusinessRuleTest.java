package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRuleInstance;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies that a {@code business-rule} may be declared at module level and referenced from an
 * aggregate in another module or context.
 * 
 * <p>A rule such as "the entity must not be deleted" is the same rule in every aggregate that has a
 * soft delete. Declaring it once and importing it is what keeps the name, the exception and the
 * consistency classification from drifting apart across contexts.</p>
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslSharedBusinessRuleTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private ValidationTestHelper validationHelper;

  /**
   * A rule outside an aggregate is a module element like any other.
   */
  @Test
  public void moduleLevelRuleParses() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context common {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module rules {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An operation was attempted on a deleted entity. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception EntityInStateDeletedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String entityIdPath");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Deleted: ${entityIdPath}\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Makes sure the entity was not (soft) deleted yet. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("business-rule EntityMustNotBeDeletedRule exception EntityInStateDeletedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    this.assertNoErrors(model);
    final org.fuin.dsl.cqrs.cqrsDsl.Module rules = IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>head(model.getContexts()).getModules());
    final Function1<BusinessRule, Boolean> _function = (BusinessRule it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equals(_name, "EntityMustNotBeDeletedRule"));
    };
    Assertions.assertNotNull(IterableExtensions.<BusinessRule>findFirst(Iterables.<BusinessRule>filter(rules.getElements(), BusinessRule.class), _function), 
      "the rule must be a module element, not something the parser dropped");
  }

  /**
   * An aggregate of another context references the shared rule through a wildcard import.
   */
  @Test
  public void importedRuleIsReferencedByAnAggregateOfAnotherContext() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _sharedRules = this.sharedRules();
    _builder.append(_sharedRules);
    _builder.newLineIfNotEmpty();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module orders {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import common.rules.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id OrderId identifies Order base String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"OID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Order ID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Unique identifier of the order\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("examples \"4711\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An order. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Order identifier OrderId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Cancels the order. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method cancel business-rules EntityMustNotBeDeletedRule {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    this.assertNoErrors(model);
    final BusinessRule rule = this.referencedRule(model);
    Assertions.assertFalse(rule.eIsProxy(), "the imported rule must resolve");
    EObject _eContainer = rule.eContainer();
    Assertions.assertEquals("rules", ((org.fuin.dsl.cqrs.cqrsDsl.Module) _eContainer).getName(), 
      "the reference must resolve to the shared module level rule");
    Assertions.assertEquals("EntityInStateDeletedException", rule.getException().getName(), 
      "the shared rule must carry its exception across the import - that is what reaches the generated code");
  }

  /**
   * Fully qualified, so no import is involved.
   */
  @Test
  public void fullyQualifiedRuleNeedsNoImport() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _sharedRules = this.sharedRules();
    _builder.append(_sharedRules);
    _builder.newLineIfNotEmpty();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module orders {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id OrderId identifies Order base common.rules.String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"OID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Order ID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Unique identifier of the order\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("examples \"4711\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An order. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Order identifier OrderId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Cancels the order. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method cancel business-rules common.rules.EntityMustNotBeDeletedRule {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    this.assertNoErrors(this.parse(_builder));
  }

  /**
   * Without an import the rule stays out of reach - a shared rule must not become visible
   * everywhere just because it sits at module level.
   */
  @Test
  public void ruleOfAnotherModuleIsNotVisibleWithoutImport() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _sharedRules = this.sharedRules();
    _builder.append(_sharedRules);
    _builder.newLineIfNotEmpty();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module orders {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import common.rules.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id OrderId identifies Order base String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"OID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Order ID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Unique identifier of the order\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("examples \"4711\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An order. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Order identifier OrderId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Cancels the order. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method cancel business-rules UnknownRule {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    this.assertErrors(this.parse(_builder));
  }

  /**
   * A rule declared inside the aggregate still works and still wins over an imported one of the
   * same name - the aggregate's own declaration is the more specific statement.
   */
  @Test
  public void ownRuleShadowsTheImportedOne() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _sharedRules = this.sharedRules();
    _builder.append(_sharedRules);
    _builder.newLineIfNotEmpty();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module orders {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import common.rules.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id OrderId identifies Order base String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"OID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Order ID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Unique identifier of the order\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("examples \"4711\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An order. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Order identifier OrderId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** The aggregate\'s own take on it. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("business-rule EntityMustNotBeDeletedRule exception EntityInStateDeletedException {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Cancels the order. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method cancel business-rules EntityMustNotBeDeletedRule {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    this.assertNoErrors(model);
    final BusinessRule rule = this.referencedRule(model);
    EObject _eContainer = rule.eContainer();
    Assertions.assertTrue((_eContainer instanceof Aggregate), 
      "the aggregate\'s own rule must win over the imported one of the same name");
    EObject _eContainer_1 = rule.eContainer();
    final Aggregate aggregate = ((Aggregate) _eContainer_1);
    Assertions.assertEquals(1, aggregate.getBusinessRules().size(), 
      "the aggregate\'s own rule must be one of its businessRules");
    Assertions.assertSame(rule, IterableExtensions.<BusinessRule>head(aggregate.getBusinessRules()), 
      "the reference must point at the rule the aggregate declared");
    Assertions.assertTrue(IterableExtensions.isEmpty(Iterables.<BusinessRule>filter(aggregate.getElements(), BusinessRule.class)), 
      "a rule declared in an aggregate must not end up among its nested elements");
  }

  /**
   * A rule an aggregate declares itself must not be reported as an illegal nested element.
   * 
   * <p>Making BusinessRule a module element made it an AbstractElement, and the check that restricts
   * what an aggregate may nest looks at every AbstractElement it contains - which now included the
   * aggregate's own rules. Nothing about the parse tree was wrong, so only validation catches it.</p>
   */
  @Test
  public void anAggregatesOwnRuleIsNoIllegalNestedElement() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _sharedRules = this.sharedRules();
    _builder.append(_sharedRules);
    _builder.newLineIfNotEmpty();
    _builder.append("context shop {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module orders {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("import common.rules.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id OrderId identifies Order base String {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"OID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Order ID\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Unique identifier of the order\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("examples \"4711\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An order. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Order identifier OrderId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** A rule of its own. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("business-rule MustNotBeShipped exception EntityInStateDeletedException {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Cancels the order. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method cancel business-rules MustNotBeShipped, EntityMustNotBeDeletedRule {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parse(_builder);
    this.assertNoErrors(model);
    this.validationHelper.assertNoIssues(model);
  }

  /**
   * The shared module every test above imports from.
   */
  private CharSequence sharedRules() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context common {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module rules {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** An operation was attempted on a deleted entity. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception EntityInStateDeletedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String entityIdPath");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Deleted: ${entityIdPath}\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Makes sure the entity was not (soft) deleted yet. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("business-rule EntityMustNotBeDeletedRule exception EntityInStateDeletedException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }

  /**
   * The rule the aggregate's only method refers to.
   */
  private BusinessRule referencedRule(final DomainModel model) {
    final Function1<Context, Boolean> _function = (Context it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equals(_name, "shop"));
    };
    final org.fuin.dsl.cqrs.cqrsDsl.Module orders = IterableExtensions.<org.fuin.dsl.cqrs.cqrsDsl.Module>head(IterableExtensions.<Context>findFirst(model.getContexts(), _function).getModules());
    final Aggregate order = IterableExtensions.<Aggregate>head(Iterables.<Aggregate>filter(orders.getElements(), Aggregate.class));
    return IterableExtensions.<BusinessRuleInstance>head(IterableExtensions.<Method>head(order.getMethods()).getBusinessRules().getBusinessRuleInstances()).getBusinessRule();
  }

  private DomainModel parse(final CharSequence text) {
    try {
      final Path root = Files.createTempDirectory("shared-business-rule");
      final URI uri = URI.createFileURI(root.resolve("model.cqrs").toString());
      return this.parseHelper.parse(text, uri, this.resourceSetProvider.get());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private void assertNoErrors(final DomainModel model) {
    EcoreUtil.resolveAll(model.eResource());
    boolean _isEmpty = model.eResource().getErrors().isEmpty();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Unexpected errors: ");
    String _join = IterableExtensions.join(model.eResource().getErrors(), ", ");
    _builder.append(_join);
    Assertions.assertTrue(_isEmpty, _builder.toString());
  }

  private void assertErrors(final DomainModel model) {
    EcoreUtil.resolveAll(model.eResource());
    Assertions.assertFalse(model.eResource().getErrors().isEmpty(), 
      "Expected the reference to fail because nothing declares it");
  }
}
