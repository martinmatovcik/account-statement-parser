package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.repository.CategoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public Category persistCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategoryById(UUID id, Category updatedCategory) {
    Category categoryToUpdate = findByIdOrElseThrow(id);
    BeanUtils.copyProperties(updatedCategory, categoryToUpdate, "id");
    return categoryRepository.save(categoryToUpdate);
  }

  private Category findByIdOrElseThrow(UUID id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public Category findCategoryByCode(String categoryCode) {
    return categoryRepository.findByCode(categoryCode).orElseThrow();
  }

  @Override
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  @Override
  public void updatePlanedAmountRealAmountAndDifference(Category category) {
    category.setPlannedAmount(
        category.getItems().stream()
            .map(item -> item.getPlannedAmount().abs())
            .reduce(BigDecimal.ZERO, BigDecimal::add));

    category.setRealAmount(
        category.getItems().stream()
            .map(item -> item.getRealAmount().abs())
            .reduce(BigDecimal.ZERO, BigDecimal::add));

    category.setDifference(category.getPlannedAmount().subtract(category.getRealAmount()));

    updateCategoryById(category.getId(), category);
  }
}
