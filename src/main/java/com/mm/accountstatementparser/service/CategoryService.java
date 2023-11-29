package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import java.util.List;

public interface CategoryService extends CrudEntityService<Category> {
  Category findCategoryByCode(String categoryCode);

  void updatePlanedAmountRealAmountAndDifference(Category category);

  Category findOrCreateCategoryOthers();
}
