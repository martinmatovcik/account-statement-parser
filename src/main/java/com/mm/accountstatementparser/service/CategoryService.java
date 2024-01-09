package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public interface CategoryService extends CrudEntityService<Category> {
  Category findCategoryByCode(String categoryCode);

  void updatePlannedAmountRealAmountAndDifference(Category newCategory, CategoryItem categoryItem);

  void updateRealAmountAndDifferenceWithCategoryItem(@Nullable Category newCategory, CategoryItem categoryItem, BigDecimal transactionAmount);

  Category findOrCreateCategoryOthers();
}
