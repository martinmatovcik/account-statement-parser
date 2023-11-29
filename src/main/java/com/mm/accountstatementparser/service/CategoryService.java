package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;

public interface CategoryService extends CrudEntityService<Category> {
  Category findCategoryByCode(String categoryCode);

  void updatePlanedAmountRealAmountAndDifference(Category newCategory, CategoryItem categoryItem);

  Category findOrCreateCategoryOthers();
}
