import 'package:melkheftken_contract/src-gen/categories/category_id.dart';
import 'package:melkheftken_contract/src-gen/categories/category_name.dart';
import 'package:melkheftken_contract/src-gen/categories/category_type.dart';
import 'package:melkheftken_contract/src-gen/common/basics/versioned_entity_id_path.dart';
import 'package:melkheftken_contract/src/descriptor/attribute_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';
import 'package:melkheftken_contract/src/descriptor/view_descriptor.dart';
import 'package:melkheftken_contract/src/json/json.dart';

/// A read-model row for the category list: the identity, the label, and the side of the ledger.
class CategoryDetails {
  /// Constructor with all data.
  const CategoryDetails({
    required this.source,
    required this.id,
    required this.name,
    required this.kind,
  });

  /// Reads the row off the server's JSON.
  factory CategoryDetails.fromJson(Map<String, dynamic> json) => CategoryDetails(
        source: VersionedEntityIdPath.fromJson(requiredObject(json, 'source')),
        id: CategoryId.fromWire(requiredString(json, 'id')),
        name: CategoryName(requiredString(json, 'name')),
        kind: CategoryType.fromWire(requiredString(json, 'kind')),
      );

  /// What this type is called on screen, attribute by attribute.
  static const TypeDescriptor descriptor = TypeDescriptor(
    name: 'CategoryDetails',
    attributes: <AttributeDescriptor>[
      AttributeDescriptor(
        name: 'source',
        kind: ValueKind.identifier,
        modelType: 'VersionedEntityIdPath',
        role: AttributeRole.source,
      ),
      AttributeDescriptor(
        name: 'id',
        kind: ValueKind.identifier,
        modelType: 'CategoryId',
        role: AttributeRole.identifier,
        text: ModelText(
          bundle: 'Categories',
          key: 'id',
          shortLabel: 'CATID',
          label: 'Category ID',
          tooltip: 'Unique identifier of the category',
        ),
      ),
      AttributeDescriptor(
        name: 'name',
        kind: ValueKind.text,
        modelType: 'CategoryName',
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
        modelType: 'CategoryType',
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

  /// Aggregate this row was projected from and the version it reflects.
  final VersionedEntityIdPath source;

  /// The category's identifier.
  final CategoryId id;

  final CategoryName name;

  /// The ledger side the category aggregates on.
  final CategoryType kind;

  /// Reads the attribute called [attribute] off this row, for a renderer that has only a
  /// descriptor.
  ///
  /// An operator rather than a method, because a method needs a name and every name is one a
  /// model is entitled to give an attribute - `value` among them, which is what a wrapped
  /// single value is habitually called.
  Object? operator [](String attribute) => switch (attribute) {
        'source' => source,
        'id' => id.typed,
        'name' => name.value,
        'kind' => kind.wireName,
        _ => throw ArgumentError("CategoryDetails has no attribute '$attribute'"),
      };

  /// Writes the row back as JSON.
  Map<String, Object?> toJson() => <String, Object?>{
        'source': source.toJson(),
        'id': id.typed,
        'name': name.value,
        'kind': kind.wireName,
      };

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is CategoryDetails &&
          other.source == source &&
          other.id == id &&
          other.name == name &&
          other.kind == kind;

  @override
  int get hashCode => Object.hash(source, id, name, kind);

  @override
  String toString() => 'CategoryDetails[id=${id.typed}, name=${name.value}, kind=${kind.wireName}]';
}
