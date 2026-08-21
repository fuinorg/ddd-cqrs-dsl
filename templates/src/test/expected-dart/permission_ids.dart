/// Every operation this release can be given permission for.
///
/// One id per command and one per view method, so an operation cannot ship without an entry -
/// which on the read side would mean an unchecked read. The `.*` forms are an assigning
/// shorthand only: they are expanded server-side, and a client always checks a single operation.
///
/// None of this is enforcement. The server refuses on every call regardless; these only stop a
/// client offering what it knows will be refused.
class PermissionIds {
  const PermissionIds._();

  /// Create a custom category.
  static const String createCategoryCommand = 'CreateCategoryCommand';

  /// Delete a custom category.
  static const String removeCategoryCommand = 'RemoveCategoryCommand';

  /// Rename a custom category.
  static const String renameCategoryCommand = 'RenameCategoryCommand';

  /// Every read method of `CategoryView`.
  static const String categoryViewAll = 'CategoryView.*';

  /// Returns all categories sorted by type (income before expense) and, within each type,
  /// alphabetically by name.
  static const String categoryViewListCategories = 'CategoryView.listCategories';

  /// Returns only the categories of the given type, for pickers that classify a record on a known
  /// side of the ledger.
  static const String categoryViewListByType = 'CategoryView.listByType';

  /// Every id in this release.
  static const Set<String> all = <String>{
    createCategoryCommand,
    removeCategoryCommand,
    renameCategoryCommand,
    categoryViewListCategories,
    categoryViewListByType,
  };

  /// What each whole-view id expands to.
  static const Map<String, Set<String>> viewMethods = <String, Set<String>>{
    'CategoryView': <String>{categoryViewListCategories, categoryViewListByType},
  };

  /// The aggregate each command targets.
  static const Map<String, String> commandTargets = <String, String>{
    createCategoryCommand: 'Category',
    removeCategoryCommand: 'Category',
    renameCategoryCommand: 'Category',
  };
}
