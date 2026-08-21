import 'package:melkheftken_contract/src/descriptor/attribute_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';

/// The side of the ledger a category aggregates on, so reports can total income and expenses
/// separately. A category's type is fixed at creation.
enum CategoryType {
  /// Money coming in - totalled on the income side of every report.
  income('INCOME'),

  /// Money going out - totalled on the expense side of every report.
  expense('EXPENSE');

  /// Constructor with mandatory data.
  const CategoryType(this.wireName);

  /// The instance as it appears on the wire.
  final String wireName;

  /// All instances, in model order.
  static const List<CategoryType> all = <CategoryType>[income, expense];

  /// Valid instances - those not marked deprecated in the model.
  static const List<CategoryType> valid = <CategoryType>[income, expense];

  /// Deprecated instances.
  static const List<CategoryType> deprecated = <CategoryType>[];

  /// What to call each instance on screen.
  ///
  /// Always present, empty when the model captions nothing. A renderer that is handed this then
  /// shows the wire name, which is honest - and a member that appears and disappears would make
  /// every descriptor referencing it depend on whether somebody happened to write a label.
  static const List<EnumValueDescriptor> descriptors = <EnumValueDescriptor>[
    EnumValueDescriptor(
      name: 'INCOME',
      text: ModelText(
        bundle: 'Categories',
        key: 'INCOME',
        shortLabel: 'Income',
        label: 'Income',
        tooltip: 'Money coming in - totalled on the income side of every report',
      ),
    ),
    EnumValueDescriptor(
      name: 'EXPENSE',
      text: ModelText(
        bundle: 'Categories',
        key: 'EXPENSE',
        shortLabel: 'Expense',
        label: 'Expense',
        tooltip: 'Money going out - totalled on the expense side of every report',
      ),
    ),
  ];

  /// Reads an instance off its wire name.
  static CategoryType fromWire(String wireName) => all.firstWhere(
        (v) => v.wireName == wireName,
        orElse: () => throw FormatException('Unknown CategoryType: $wireName'),
      );
}
