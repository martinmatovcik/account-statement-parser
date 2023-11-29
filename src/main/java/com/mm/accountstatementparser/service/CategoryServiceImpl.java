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
  public Category updateEntity(Category updatedEntity) {
    return updateEntityById(updatedEntity.getId(), updatedEntity);
  }

  @Override
  public Category updateEntityById(UUID id, Category updatedEntity) {
    Category entityToUpdate = getEntityById(id);
    BeanUtils.copyProperties(updatedEntity, entityToUpdate, "id");
    return categoryRepository.save(entityToUpdate);
  }

  @Override
  public Category updateEntityFieldsById(UUID id, Map<Object, Object> fields) {
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
  public void updatePlanedAmountRealAmountAndDifference(
      Category newCategory, CategoryItem categoryItem) {

    BigDecimal actualPlannedAmount = categoryItem.getPlannedAmount();
    BigDecimal actualRealAmount = categoryItem.getRealAmount();

    Category originalCategory = categoryItem.getCategory();

    if (originalCategory != null) {
      BigDecimal originalPlannedAmount = originalCategory.getPlannedAmount();
      BigDecimal originalRealAmount = originalCategory.getRealAmount();
      originalCategory.setPlannedAmount(originalPlannedAmount.subtract(actualPlannedAmount));
      originalCategory.setRealAmount(originalRealAmount.subtract(actualRealAmount));
      originalCategory.setDifference(originalPlannedAmount.subtract(originalRealAmount));

      updateEntity(originalCategory);
    }

    BigDecimal newPlannedAmount = newCategory.getPlannedAmount();
    BigDecimal newRealAmount = newCategory.getRealAmount();
    newCategory.setPlannedAmount(newPlannedAmount.add(actualPlannedAmount));
    newCategory.setRealAmount(newRealAmount.add(actualRealAmount));
    newCategory.setDifference(newPlannedAmount.subtract(newRealAmount));

    updateEntity(newCategory);
  }

  @Override
  public Category findOrCreateCategoryOthers() {
    return categoryRepository
        .findByCode("others")
        .orElseGet(
            () -> persistEntity(Category.builder().code("others").headerValue("Ostatné").build()));
  }
}
