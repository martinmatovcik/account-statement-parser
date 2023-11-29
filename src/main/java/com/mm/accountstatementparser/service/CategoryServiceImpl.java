package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.repository.CategoryRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category getCategoryById(UUID id) {
    return categoryRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Category with given ID does not exist"));
  }

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

  @Override
  public Category updateFieldsInCategoryById(UUID id, Map<Object, Object> fields) {
    Category categoryToUpdate = getCategoryById(id);
    fields.forEach(
            (key, value) -> {
              Field field = ReflectionUtils.findField(Transaction.class, (String) key);
              Objects.requireNonNull(field).setAccessible(true);
            });
    return categoryRepository.save(categoryToUpdate);
  }

  @Override
  public void deleteCategoryById(UUID id) {

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
    if (!CollectionUtils.isEmpty(category.getCategoryItems())) {
        BigDecimal plannedAmount = BigDecimal.ZERO;
        for (CategoryItem categoryItem : category.getCategoryItems()) {
          plannedAmount = plannedAmount.add(categoryItem.getPlannedAmount().abs());
        }
        category.setPlannedAmount(plannedAmount);

      BigDecimal realAmount = BigDecimal.ZERO;
      for (CategoryItem categoryItem : category.getCategoryItems()) {
        realAmount = realAmount.add(categoryItem.getRealAmount().abs());
      }
      category.setRealAmount(realAmount);
    }

    category.setDifference(category.getPlannedAmount().subtract(category.getRealAmount()));

    updateCategoryById(category.getId(), category);
  }

  @Override
  public Category findOrCreateCategoryOthers() {
    return categoryRepository
        .findByCode("unassigned")
        .orElseGet(
            () ->
                persistCategory(Category.builder().code("others").headerValue("Ostatné").build()));
  }
}
