package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.testing.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.fuin.dsl.cqrs.cqrsDsl.EntityIdPathType;
import org.fuin.dsl.cqrs.cqrsDsl.PathSegment;
import org.fuin.dsl.cqrs.cqrsDsl.SegmentRange;
import org.fuin.dsl.cqrs.validation.CqrsDslValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Verifies the typed path from an aggregate root down to the entity it addresses.
 * 
 * <p>A child of a root there are many of cannot be addressed by its own identifier - 'TRANSACTION 45'
 * exists in every account-year - so it travels as a path. Untyped, that path says nothing about what it
 * addresses, which is why this model could hold fourteen of them all pointing at a transaction and state
 * it nowhere.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsEntityIdPathTypeTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;

  @Test
  public void testAPathNamesTheIdentifiersItIsMadeOf() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** How a transaction is addressed. */");
      _builder.newLine();
      _builder.append("entity-id-path AccountTransactionPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(this.model(_builder.toString()));
      this._validationTestHelper.assertNoErrors(model);
      final EntityIdPathType path = this.pathType(model);
      Assertions.assertEquals("AccountTransactionPath", path.getName());
      final Function1<PathSegment, String> _function = (PathSegment it) -> {
        return it.getType().getName();
      };
      Assertions.assertEquals(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("AnnualTransactionsId", "AccountTransactionId")), 
        IterableExtensions.<String>toList(ListExtensions.<PathSegment, String>map(path.getSegments(), _function)));
      final Function1<PathSegment, Boolean> _function_1 = (PathSegment it) -> {
        SegmentRange _range = it.getRange();
        return Boolean.valueOf((_range == null));
      };
      Assertions.assertTrue(IterableExtensions.<PathSegment>forall(path.getSegments(), _function_1));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testASegmentMayStateHowManyItTakes() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** How a nested transaction is addressed. */");
      _builder.newLine();
      _builder.append("entity-id-path NestedPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId[1..*]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(this.model(_builder.toString()));
      this._validationTestHelper.assertNoErrors(model);
      final EList<PathSegment> segments = this.pathType(model).getSegments();
      Assertions.assertNull(segments.get(0).getRange(), "a step without a range takes exactly one");
      Assertions.assertEquals(1, segments.get(1).getRange().getMin());
      Assertions.assertTrue(segments.get(1).getRange().isUnbounded(), "\'*\' is unbounded");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testARangeCanAlsoSayZeroOrCapped() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Skippable. */");
      _builder.newLine();
      _builder.append("entity-id-path SkippablePath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId[0..*]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("/** Capped. */");
      _builder.newLine();
      _builder.append("entity-id-path CappedPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId[1..2]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final DomainModel model = this.parseHelper.parse(this.model(_builder.toString()));
      this._validationTestHelper.assertNoErrors(model);
      final List<EntityIdPathType> paths = IteratorExtensions.<EntityIdPathType>toList(Iterators.<EntityIdPathType>filter(model.eAllContents(), EntityIdPathType.class));
      Assertions.assertEquals(0, paths.get(0).getSegments().get(1).getRange().getMin());
      Assertions.assertTrue(paths.get(0).getSegments().get(1).getRange().isUnbounded());
      Assertions.assertEquals(1, paths.get(1).getSegments().get(1).getRange().getMin());
      Assertions.assertEquals(2, paths.get(1).getSegments().get(1).getRange().getMax());
      Assertions.assertFalse(paths.get(1).getSegments().get(1).getRange().isUnbounded());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAPathIsATypeAnAttributeCanUse() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** How a transaction is addressed. */");
      _builder.newLine();
      _builder.append("entity-id-path AccountTransactionPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("/** One transaction on screen. */");
      _builder.newLine();
      _builder.append("value-object TransactionRow {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("/** What this row addresses. */");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AccountTransactionPath id");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertNoErrors(this.parseHelper.parse(this.model(_builder.toString())));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAPathOfOneStepIsTheIdentifierItself() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Pointless. */");
      _builder.newLine();
      _builder.append("entity-id-path RootOnlyPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.ENTITY_ID_PATH_TYPE, 
        CqrsDslValidator.PATH_NEEDS_MORE_THAN_A_ROOT);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAPathHasToStartAtARoot() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Starts one level too high. */");
      _builder.newLine();
      _builder.append("entity-id-path TooHighPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AccountTransactionId / AnnualTransactionsId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.PATH_SEGMENT, 
        CqrsDslValidator.PATH_MUST_START_AT_A_ROOT);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testALaterStepHasToBeAnEntityOfThatRoot() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Second step is another aggregate. */");
      _builder.newLine();
      _builder.append("entity-id-path TwoRootsPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.PATH_SEGMENT, 
        CqrsDslValidator.PATH_SEGMENT_NOT_OF_ROOT);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAnEntityOfAnotherRootIsNotAStep() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Wrong owner. */");
      _builder.newLine();
      _builder.append("entity-id-path ForeignPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AccountId / AccountTransactionId");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.PATH_SEGMENT, 
        CqrsDslValidator.PATH_SEGMENT_NOT_OF_ROOT);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAnEmptyRangeIsRefused() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Contradictory. */");
      _builder.newLine();
      _builder.append("entity-id-path ImpossiblePath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId[2..1]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.SEGMENT_RANGE, CqrsDslValidator.PATH_RANGE_INVALID);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Test
  public void testAStepThatAcceptsNothingIsRefused() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("/** Accepts nothing. */");
      _builder.newLine();
      _builder.append("entity-id-path ZeroPath {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("AnnualTransactionsId / AccountTransactionId[0..0]");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      this._validationTestHelper.assertError(this.parseHelper.parse(this.model(_builder.toString())), CqrsDslPackage.Literals.SEGMENT_RANGE, CqrsDslValidator.PATH_RANGE_INVALID);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private EntityIdPathType pathType(final DomainModel model) {
    return IteratorExtensions.<EntityIdPathType>head(Iterators.<EntityIdPathType>filter(model.eAllContents(), EntityIdPathType.class));
  }

  private String model(final String declarations) {
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
    _builder.append("type UUID");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id AccountId identifies Account base UUID { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate-id AnnualTransactionsId identifies AnnualTransactions base UUID { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("entity-id AccountTransactionId identifies Transaction base String { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append(declarations, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate Account identifier AccountId { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("aggregate AnnualTransactions identifier AnnualTransactionsId { }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("entity Transaction identifier AccountTransactionId root AnnualTransactions { }");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
