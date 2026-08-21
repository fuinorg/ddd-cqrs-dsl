import 'package:melkheftken_contract/src-gen/categories/category_details.dart';
import 'package:melkheftken_contract/src-gen/categories/category_type.dart';
import 'package:melkheftken_contract/src/json/json.dart';
import 'package:melkheftken_contract/src/transport.dart';

/// Reads the categories: the whole list for management, or only those of one type for
/// classification.
class CategoryViewClient {
  /// Constructor with mandatory data.
  const CategoryViewClient(this.transport);

  /// Path the view is served under.
  static const String basePath = '/view/category';

  /// Reads the view over whatever transport the application is wired with.
  final ViewTransport transport;

  /// Returns all categories sorted by type (income before expense) and, within each type,
  /// alphabetically by name.
  Future<List<CategoryDetails>> listCategories() async {
    final body = await transport.get('$basePath/list-categories');
    return objectList(body).map(CategoryDetails.fromJson).toList(growable: false);
  }

  /// Returns only the categories of the given type, for pickers that classify a record on a known
  /// side of the ledger.
  Future<List<CategoryDetails>> listByType(CategoryType kind) async {
    final body = await transport.get(
      '$basePath/list-by-type',
      query: <String, Object?>{'kind': kind.wireName},
    );
    return objectList(body).map(CategoryDetails.fromJson).toList(growable: false);
  }
}
