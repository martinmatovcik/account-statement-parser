package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import jakarta.annotation.Nullable;

public interface CategoryService extends CrudEntityService<Category> {
  Category findCategoryByCode(String categoryCode);

  void updatePlanedAmountRealAmountAndDifference(@Nullable Category originalCategory, Category newCategory, CategoryItem categoryItem);

  Category findOrCreateCategoryOthers();
}
