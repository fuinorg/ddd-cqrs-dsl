import 'package:melkheftken_contract/src-gen/categories/category_details.dart';
import 'package:melkheftken_contract/src-gen/categories/category_type.dart';
import 'package:melkheftken_contract/src/descriptor/attribute_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';
import 'package:melkheftken_contract/src/descriptor/view_descriptor.dart';

/// Reads the categories: the whole list for management, or only those of one type for
/// classification.
const ViewDescriptor categoryView = ViewDescriptor(
  id: 'CategoryView',
  module: 'categories.categoryview',
  restPath: '/view/category',
  doc: 'Reads the categories: the whole list for management, or only those of one type for '
      'classification.',
  text: ModelText(
    bundle: 'Categoryview',
    key: 'CategoryView',
    shortLabel: 'Categories',
    label: 'Categories',
    tooltip: 'Every category this book keeps, and what each one is for',
  ),
  methods: <MethodDescriptor>[
    MethodDescriptor(
      id: 'CategoryView.listCategories',
      name: 'listCategories',
      path: '/list-categories',
      kind: MethodKind.list,
      doc: 'Returns all categories sorted by type (income before expense) and, within each type, '
          'alphabetically by name.',
      text: ModelText(
        bundle: 'Categoryview',
        key: 'CategoryView.listCategories',
        shortLabel: 'All',
        label: 'All categories',
        tooltip: 'Every category, income first then expense, each alphabetical by name',
      ),
      returns: CategoryDetails.descriptor,
    ),
    MethodDescriptor(
      id: 'CategoryView.listByType',
      name: 'listByType',
      path: '/list-by-type',
      kind: MethodKind.list,
      doc: 'Returns only the categories of the given type, for pickers that classify a record on a '
          'known side of the ledger.',
      text: ModelText(
        bundle: 'Categoryview',
        key: 'CategoryView.listByType',
        shortLabel: 'By type',
        label: 'Categories of one type',
        tooltip: 'Only the categories on one side of the ledger, for classifying a record',
      ),
      params: <AttributeDescriptor>[
        AttributeDescriptor(
          name: 'kind',
          kind: ValueKind.enumeration,
          text: ModelText(
            bundle: 'Categoryview',
            key: 'kind',
            shortLabel: 'Type',
            label: 'Type',
            tooltip: 'The side of the ledger to list categories for',
          ),
          values: CategoryType.descriptors,
        ),
      ],
      returns: CategoryDetails.descriptor,
    ),
  ],
);
