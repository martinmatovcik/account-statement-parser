package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category persistCategory(Category category);

    Category updateCategoryById(UUID id, Category updatedCategory);

    Category findCategoryByCode(String categoryCode);

    List<Category> findAll();

    void updatePlanedAmountRealAmountAndDifference(Category category);
}
