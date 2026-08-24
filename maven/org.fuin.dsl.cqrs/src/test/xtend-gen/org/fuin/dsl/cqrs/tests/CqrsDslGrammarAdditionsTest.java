/**
 * Covers the constructs added for business keys, generated business rules and the read model's
 * declared identity. Parsing is only half of what is asserted: every one of them introduces a cross
 * reference that must resolve inside the element it is written in, so each test also checks that the
 * reference found its target - and, where it matters, that a same named element elsewhere did not.
 */
package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import java.util.Collections;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate;
import org.fuin.dsl.cqrs.cqrsDsl.Attribute;
import org.fuin.dsl.cqrs.cqrsDsl.BusinessRule;
import org.fuin.dsl.cqrs.cqrsDsl.Command;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.Key;
import org.fuin.dsl.cqrs.cqrsDsl.RuleAttrRef;
import org.fuin.dsl.cqrs.cqrsDsl.RuleComparison;
import org.fuin.dsl.cqrs.cqrsDsl.RuleExpr;
import org.fuin.dsl.cqrs.cqrsDsl.RuleIsEmpty;
import org.fuin.dsl.cqrs.cqrsDsl.RuleNot;
import org.fuin.dsl.cqrs.cqrsDsl.RuleOperand;
import org.fuin.dsl.cqrs.cqrsDsl.RuleOr;
import org.fuin.dsl.cqrs.cqrsDsl.RuleRefOperand;
import org.fuin.dsl.cqrs.cqrsDsl.ServiceCallArgument;
import org.fuin.dsl.cqrs.cqrsDsl.SoftDelete;
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject;
import org.fuin.dsl.cqrs.cqrsDsl.Variable;
import org.fuin.dsl.cqrs.cqrsDsl.VariableArgument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsDslGrammarAdditionsTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  private ValidationTestHelper validationHelper;

  /**
   * A read model row names the attribute that identifies it.
   */
  @Test
  public void rowDeclaresItsIdentity() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Row {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String id");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("identified-by id");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final ValueObject row = IterableExtensions.<ValueObject>head(EcoreUtil2.<ValueObject>getAllContentsOfType(model, ValueObject.class));
    Assertions.assertNotNull(row.getIdentifiedBy(), "identified-by did not link");
    Assertions.assertEquals("id", row.getIdentifiedBy().getName());
  }

  /**
   * And only its own. A row is identified by one of its attributes, so an attribute of the same name
   * on another row is not a candidate - which is the whole reason this is a cross reference.
   */
  @Test
  public void identityDoesNotReachAnotherRow() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context foo {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module bar {");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Other {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String elsewhere");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Row {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String id");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("identified-by elsewhere");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel result = this.parseHelper.parse(_builder);
      final Function1<ValueObject, Boolean> _function = (ValueObject it) -> {
        String _name = it.getName();
        return Boolean.valueOf(Objects.equals(_name, "Row"));
      };
      final ValueObject row = IterableExtensions.<ValueObject>findFirst(EcoreUtil2.<ValueObject>getAllContentsOfType(result, ValueObject.class), _function);
      Assertions.assertTrue(row.getIdentifiedBy().eIsProxy(), 
        "\'elsewhere\' belongs to another row and should not have resolved");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A business key names attributes of the type declaring it, and says what a collision does.
   */
  @Test
  public void aggregateDeclaresABusinessKey() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Reported when the name is taken. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception DuplicateNameException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Name is already taken\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String kind");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** No two things of the same kind share a name. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("key NamePerKind exception DuplicateNameException {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("attributes name, kind");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("on-collision refuse");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("display-as \"$-name ($-kind)\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final Aggregate thing = IterableExtensions.<Aggregate>head(EcoreUtil2.<Aggregate>getAllContentsOfType(model, Aggregate.class));
    final Key key = IterableExtensions.<Key>head(thing.getKeys());
    Assertions.assertEquals(2, key.getAttributes().size());
    final Function1<Attribute, String> _function = (Attribute it) -> {
      return it.getName();
    };
    Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("name", "kind")), ListExtensions.<Attribute, String>map(key.getAttributes(), _function));
    Assertions.assertEquals("refuse", key.getOnCollision().getLiteral());
  }

  /**
   * Having no business key is stated rather than left out, and the reason is mandatory.
   */
  @Test
  public void aggregateDeclaresThatItHasNoKey() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Near duplicates are expected here; merging is the answer, not refusal. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("no-key");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    Assertions.assertNotNull(IterableExtensions.<Aggregate>head(EcoreUtil2.<Aggregate>getAllContentsOfType(model, Aggregate.class)).getNoKey());
  }

  /**
   * A soft deleted aggregate names the event that marks it gone, and may name the one that revives it.
   */
  @Test
  public void softDeleteNamesItsEvents() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("event ThingRemovedEvent {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("event ThingRestoredEvent {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId soft-delete ThingRemovedEvent restored-by ThingRestoredEvent {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final SoftDelete softDelete = IterableExtensions.<Aggregate>head(EcoreUtil2.<Aggregate>getAllContentsOfType(model, Aggregate.class)).getSoftDelete();
    Assertions.assertEquals("ThingRemovedEvent", softDelete.getDeleteEvent().getName());
    Assertions.assertEquals("ThingRestoredEvent", softDelete.getRestoreEvent().getName());
  }

  /**
   * The reviving half is optional - nothing in the model has one yet.
   */
  @Test
  public void softDeleteWithoutRestore() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("event ThingRemovedEvent {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId soft-delete ThingRemovedEvent {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final SoftDelete softDelete = IterableExtensions.<Aggregate>head(EcoreUtil2.<Aggregate>getAllContentsOfType(model, Aggregate.class)).getSoftDelete();
    Assertions.assertEquals("ThingRemovedEvent", softDelete.getDeleteEvent().getName());
    Assertions.assertNull(softDelete.getRestoreEvent());
  }

  /**
   * A rule declares the values it is handed and the condition it verifies over them.
   */
  @Test
  public void ruleComparesAnAttributeToNull() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** Makes sure the thing is assigned. */");
    _builder.newLine();
    _builder.append("business-rule MustBeAssigned exception SomeException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** The entry it backs, if any. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("optional String assignedEntry");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("requires assignedEntry != null");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final BusinessRule rule = this.ruleOf(_builder);
    RuleExpr _requires = rule.getRequires();
    final RuleComparison comparison = ((RuleComparison) _requires);
    Assertions.assertEquals("assignedEntry", comparison.getLeft().getAttribute().getName());
    Assertions.assertEquals("ne", comparison.getOp().getName());
  }

  /**
   * A named value of an enumeration is a cross reference, scoped by the type on the left.
   */
  @Test
  public void ruleComparesAnAttributeToAnEnumValue() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** Makes sure the transaction is ignored. */");
    _builder.newLine();
    _builder.append("business-rule MustBeIgnored exception SomeException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** The reconciliation status. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Status status");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("requires status == IGNORED");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final BusinessRule rule = this.ruleOf(_builder);
    RuleExpr _requires = rule.getRequires();
    final RuleComparison comparison = ((RuleComparison) _requires);
    RuleOperand _right = comparison.getRight();
    final RuleRefOperand right = ((RuleRefOperand) _right);
    Assertions.assertEquals("IGNORED", right.getTarget().eGet(right.getTarget().eClass().getEStructuralFeature("name")));
  }

  /**
   * Two attributes of the same enumeration compare too - the right hand side is not enum values only.
   */
  @Test
  public void ruleComparesTwoAttributes() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** Makes sure both sides agree. */");
    _builder.newLine();
    _builder.append("business-rule TypeMustMatch exception SomeException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** One side. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Status status");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** The other. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Status otherStatus");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("requires status == otherStatus");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final BusinessRule rule = this.ruleOf(_builder);
    RuleExpr _requires = rule.getRequires();
    RuleOperand _right = ((RuleComparison) _requires).getRight();
    final RuleRefOperand right = ((RuleRefOperand) _right);
    Assertions.assertEquals("otherStatus", right.getTarget().eGet(right.getTarget().eClass().getEStructuralFeature("name")));
  }

  /**
   * The one built-in operator over a collection.
   */
  @Test
  public void ruleAsksAboutACollection() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** Makes sure nothing is linked. */");
    _builder.newLine();
    _builder.append("business-rule MustHaveNoLinks exception SomeException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** What is linked. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String links");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("requires links.is-empty()");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final BusinessRule empty = this.ruleOf(_builder);
    Assertions.<RuleIsEmpty>assertInstanceOf(RuleIsEmpty.class, empty.getRequires());
  }

  /**
   * Negation, conjunction, disjunction and parentheses - the one compound shape the corpus needs.
   */
  @Test
  public void ruleCombinesConditions() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** Makes sure a financial change does not conflict with a link. */");
    _builder.newLine();
    _builder.append("business-rule MustNotBeLinkedForFinancialChange exception SomeException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** The transaction it is linked to, if any. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("optional String accountTransactionId");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** The date as it stands. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String date");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** The date as it would become. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String newDate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("requires accountTransactionId == null || (date == newDate && !accountTransactionId.is-empty())");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final BusinessRule rule = this.ruleOf(_builder);
    RuleExpr _requires = rule.getRequires();
    final RuleOr or = ((RuleOr) _requires);
    Assertions.<RuleComparison>assertInstanceOf(RuleComparison.class, or.getLeft());
    Assertions.assertNotNull(or.getRight());
  }

  /**
   * A bare Boolean attribute is a condition on its own.
   */
  @Test
  public void ruleUsesABareBoolean() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/** Makes sure the name is free. */");
    _builder.newLine();
    _builder.append("business-rule NameMustBeUnique exception SomeException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** Whether the name is taken. */");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Boolean nameTaken");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("requires !nameTaken");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final BusinessRule rule = this.ruleOf(_builder);
    RuleExpr _requires = rule.getRequires();
    final RuleNot not = ((RuleNot) _requires);
    RuleExpr _expr = not.getExpr();
    Assertions.assertEquals("nameTaken", ((RuleAttrRef) _expr).getAttribute().getName());
  }

  /**
   * The usage site binds the rule's attributes to what the carrying operation actually holds.
   */
  @Test
  public void usageBindsActuals() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Boolean");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Something went wrong. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception SomeException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Nope\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Makes sure the name is free. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("business-rule NameMustBeUnique exception SomeException {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("/** Whether the name is taken. */");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("Boolean nameTaken");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("requires !nameTaken");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Renames it. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method rename business-rules NameMustBeUnique(exists(newName)) {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("/** The new name. */");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("String newName");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("operation-context RenameService");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("service RenameService {");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("/** Whether the name is taken. */");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("method exists {");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("/** The name to check. */");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("String candidate");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("returns Boolean");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final ServiceCallArgument call = IterableExtensions.<ServiceCallArgument>head(EcoreUtil2.<ServiceCallArgument>getAllContentsOfType(model, ServiceCallArgument.class));
    Assertions.assertEquals("exists", call.getMethod().getName());
    final Function1<Variable, String> _function = (Variable it) -> {
      return it.getName();
    };
    Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("newName")), ListExtensions.<Variable, String>map(call.getArgs(), _function));
  }

  /**
   * A plain value of the operation is an actual too, and so is a literal.
   */
  @Test
  public void usageBindsAVariableAndALiteral() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type Integer");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("/** Something went wrong. */");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("exception SomeException {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("message \"Nope\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id ThingId identifies Thing {}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Thing identifier ThingId {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String name");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Makes sure the name is short enough. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("business-rule NameMustBeShort exception SomeException {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("/** The name being checked. */");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("String candidate");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("/** How long it may be. */");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("Integer limit");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("consistency strong");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("/** Renames it. */");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("method rename business-rules NameMustBeShort(name, 10) {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("/** The new name. */");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("String newName");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final VariableArgument variable = IterableExtensions.<VariableArgument>head(EcoreUtil2.<VariableArgument>getAllContentsOfType(model, VariableArgument.class));
    Assertions.assertEquals("name", variable.getVariable().getName());
  }

  /**
   * A hint may now sit wherever wording may, not only on a context or a view.
   */
  @Test
  public void hintsSitWhereverMetaInfoDoes() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("type String");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("value-object Row {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("hint org.fuin.Whatever { \"a\" : true }");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("String id {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("hint org.fuin.Field { \"b\" : 1 }");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("command DoIt {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"Do it\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Do it now\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Does the thing\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("hint org.fuin.Action { \"c\" : \"yes\" }");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final ValueObject row = IterableExtensions.<ValueObject>head(EcoreUtil2.<ValueObject>getAllContentsOfType(model, ValueObject.class));
    Assertions.assertEquals(1, row.getHints().size());
    Assertions.assertEquals(1, IterableExtensions.<Attribute>head(row.getAttributes()).getOverridden().getHints().size());
  }

  /**
   * A command carries its own wording, like every other named element except an event.
   */
  @Test
  public void commandCarriesWording() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("context foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("module bar {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("command DoIt {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("slabel \"Do it\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("label \"Do it now\"");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("tooltip \"Does the thing\"");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final DomainModel model = this.parsed(_builder);
    final Command command = IterableExtensions.<Command>head(EcoreUtil2.<Command>getAllContentsOfType(model, Command.class));
    Assertions.assertEquals("Do it", command.getMetaInfo().getSlabel());
    Assertions.assertEquals("Do it now", command.getMetaInfo().getLabel());
    Assertions.assertEquals("Does the thing", command.getMetaInfo().getTooltip());
  }

  /**
   * Parses the model and fails the test on any syntax or linking error.
   */
  private DomainModel parsed(final CharSequence source) {
    try {
      DomainModel _xblockexpression = null;
      {
        final DomainModel result = this.parseHelper.parse(source);
        Assertions.assertNotNull(result);
        final EList<Resource.Diagnostic> errors = result.eResource().getErrors();
        boolean _isEmpty = errors.isEmpty();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Unexpected errors: ");
        String _join = IterableExtensions.join(errors, ", ");
        _builder.append(_join);
        Assertions.assertTrue(_isEmpty, _builder.toString());
        this.validationHelper.assertNoErrors(result);
        _xblockexpression = result;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Wraps a business rule in the smallest model that can hold one and returns the rule.
   */
  private BusinessRule ruleOf(final CharSequence rule) {
    BusinessRule _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context foo {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module bar {");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type String");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Boolean");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** Something went wrong. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("exception SomeException {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("message \"Nope\"");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("/** How far a transaction got. */");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("enum Status {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("instances {");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("/** Still open. */");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("OPEN");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("/** Deliberately skipped. */");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("IGNORED");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate-id ThingId identifies Thing {}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("aggregate Thing identifier ThingId {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("String name");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append(rule, "\t\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parsed(_builder);
      _xblockexpression = IterableExtensions.<BusinessRule>head(EcoreUtil2.<BusinessRule>getAllContentsOfType(model, BusinessRule.class));
    }
    return _xblockexpression;
  }
}
