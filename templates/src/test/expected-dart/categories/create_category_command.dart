import 'package:melkheftken_contract/src-gen/categories/category_id.dart';
import 'package:melkheftken_contract/src-gen/categories/category_name.dart';
import 'package:melkheftken_contract/src-gen/categories/category_type.dart';
import 'package:melkheftken_contract/src/descriptor/attribute_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/command_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';

/// Create a custom category.
class CreateCategoryCommand {
  /// Constructor with all data.
  const CreateCategoryCommand({
    required this.entityIdPath,
    required this.name,
    required this.kind,
  });

  /// Unique name used to store the command, and the last path segment of `POST /cmd/<type>`.
  static const String eventType = 'CreateCategoryCommand';

  /// What this command is called and what it needs.
  static const CommandDescriptor descriptor = CommandDescriptor(
    type: eventType,
    module: 'categories',
    target: 'Category',
    kind: CommandKind.create,
    doc: 'Create a custom category.',
    message: r"Create ${kind} category '${name}'",
    attributes: <AttributeDescriptor>[
      AttributeDescriptor(
        name: 'name',
        kind: ValueKind.text,
        text: ModelText(
          bundle: 'Categories',
          key: 'name',
          shortLabel: 'Name',
          label: 'Name',
          tooltip: 'What this category is called in lists and pickers',
          prompt: 'Office and administration',
        ),
        constraints: CategoryName.constraints,
      ),
      AttributeDescriptor(
        name: 'kind',
        kind: ValueKind.enumeration,
        text: ModelText(
          bundle: 'Categories',
          key: 'kind',
          shortLabel: 'Type',
          label: 'Type',
          tooltip: 'Which side of the ledger this category totals on',
        ),
        values: CategoryType.descriptors,
      ),
    ],
  );

  /// Identifier of the aggregate this is directed at.
  final CategoryId entityIdPath;

  final CategoryName name;

  final CategoryType kind;

  /// Writes the command as the request body of `POST /cmd/CreateCategoryCommand`.
  Map<String, Object?> toJson() => <String, Object?>{
        'entity-id-path': entityIdPath.typed,
        'name': name.value,
        'kind': kind.wireName,
      };
}
