package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
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
  public List<Category> getAll() {
    return categoryRepository.findAll();
  }

  @Override
  public Category getEntityById(UUID id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Category with given ID does not exist"));
  }

  @Override
  public Category persistEntity(Category entity) {
    return categoryRepository.save(entity);
  }

  @Override
  public Category updateEntityById(UUID id, Category updatedEntity) {
    Category entityToUpdate = getEntityById(id);
    BeanUtils.copyProperties(updatedEntity, entityToUpdate, "id");
    return categoryRepository.save(entityToUpdate);
  }

  @Override
  public Category updateFieldsInEntityById(UUID id, Map<Object, Object> fields) {
    Category entityToUpdate = getEntityById(id);
    fields.forEach(
        (key, value) -> {
          Field field = ReflectionUtils.findField(Category.class, (String) key);
          Objects.requireNonNull(field).setAccessible(true);
        });
    return categoryRepository.save(entityToUpdate);
  }

  @Override
  public void deleteEntityById(UUID id) {
    categoryRepository.deleteById(id);
  }

  @Override
  public Category findCategoryByCode(String categoryCode) {
    return categoryRepository.findByCode(categoryCode).orElseThrow();
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

    updateEntityById(category.getId(), category);
  }

  @Override
  public Category findOrCreateCategoryOthers() {
    return categoryRepository
        .findByCode("unassigned")
        .orElseGet(
            () ->
                persistEntity(Category.builder().code("others").headerValue("Ostatné").build()));
  }
}
