package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.EntityParent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CategoryService {
  List<Category> getAllCategories();

  Category getCategoryById(UUID id);

  Category persistCategory(Category category);

  Category updateCategoryById(UUID id, Category updatedCategory);

  Category updateFieldsInCategoryById(UUID id, Map<Object, Object> fields);

  void deleteCategoryById(UUID id);

  Category findCategoryByCode(String categoryCode);

  List<Category> findAll();

  void updatePlanedAmountRealAmountAndDifference(Category category);

  Category findOrCreateCategoryOthers();
}
