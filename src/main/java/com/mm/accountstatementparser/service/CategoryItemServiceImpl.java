package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignCategoryCommandDto;
import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.repository.CategoryItemRepository;
import jakarta.annotation.Nullable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
public class CategoryItemServiceImpl implements CategoryItemService {
  private final CategoryItemRepository categoryItemRepository;
  private final CategoryService categoryService;

  @Override
  public List<CategoryItem> getAll() {
    return categoryItemRepository.findAll();
  }

  @Override
  public CategoryItem getEntityById(UUID id) {
    return categoryItemRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public CategoryItem persistEntity(CategoryItem entity) {
    return categoryItemRepository.save(entity);
  }

  @Override
  public CategoryItem updateEntity(CategoryItem updatedEntity) {
    return updateEntityById(updatedEntity.getId(), updatedEntity);
  }

  @Override
  public CategoryItem updateEntityById(UUID id, CategoryItem updatedEntity) {
    CategoryItem entityToUpdate = getEntityById(id);
    BeanUtils.copyProperties(updatedEntity, entityToUpdate, "id");
    return categoryItemRepository.save(entityToUpdate);
  }

  @Override
  public CategoryItem updateEntityFieldsById(UUID id, Map<Object, Object> fields) {
    CategoryItem entityToUpdate = getEntityById(id);
    fields.forEach(
        (key, value) -> {
          Field field = ReflectionUtils.findField(Category.class, (String) key);
          Objects.requireNonNull(field).setAccessible(true);
        });
    return categoryItemRepository.save(entityToUpdate);
  }

  @Override
  public void deleteEntityById(UUID id) {
    getEntityById(id);
    categoryItemRepository.deleteById(id);
  }

  @Override
  public List<CategoryItemDto> findCategoryItemsByCategory(Category category) {
    return categoryItemRepository.findAllByCategory(category).stream()
        .map(CategoryItem::toDto)
        .toList();
  }

  @Override
  public BigDecimal sumPlannedAmountOfCategoryItems() {
    return categoryItemRepository.sumPlannedAmountOfCategoryItems();
  }

  @Override
  public BigDecimal sumPlannedAmountOfCategoryItemsForCategory(Category category) {
    return BigDecimal.valueOf(1);
    // todo: 1
    //    return itemRepository.sumPlannedAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumDifferenceOfCategoryItemsForCategory(Category category) {
    return BigDecimal.valueOf(3);
    // todo: 3
    //    return itemRepository.sumDifferenceOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumLivingExpenses(boolean isPlanned) {
    //    return isPlanned
    //        ? sumPlannedAmountOfItemsForCategory(Category.NEEDS)
    //            .add(sumPlannedAmountOfItemsForCategory(new Category()))
    //        : sumRealAmountOfItemsForCategory(Category.NEEDS)
    //            .add(sumRealAmountOfItemsForCategory(new Category()));
    return BigDecimal.ZERO;
    // todo: 0
  }

  @Override
  public CategoryItem findCategoryItemByCode(String categoryItemCode) {
    return categoryItemRepository
        .findByCode(categoryItemCode)
        .orElseThrow(() -> new RuntimeException("CategoryItem wih given CODE does not exist"));
  }

  @Override
  public Optional<CategoryItem> findCategoryItemByKeywords(List<String> transactionKeywords) {
    List<CategoryItem> categoryItems = categoryItemRepository.findAll();

    for (CategoryItem categoryItem : categoryItems) {
      if (categoryItem == null) break;
      for (String keyword : transactionKeywords)
        if (categoryItem.getKeywords().contains(keyword)) return Optional.of(categoryItem);
    }
    return Optional.empty();
  }

  @Override
  public CategoryItem findOrCreateCategoryItemUnassigned() {
    return categoryItemRepository
        .findByCode("unassigned")
        .orElseGet(
            () ->
                persistEntity(
                    CategoryItem.builder()
                        .name("Nezaradané výdavky")
                        .code("unassigned")
                        .category(categoryService.findOrCreateCategoryOthers())
                        .build()));
  }

  @Override
  public void updateRealAmountAndDifferenceWithTransaction(
      @Nullable CategoryItem newCategoryItem, Transaction transaction) {

    BigDecimal actualTransactionAmount = transaction.getAmount().abs();

    CategoryItem originalCategoryItem = transaction.getCategoryItem();
    Category originalCategory = null;

    if (originalCategoryItem != null) {
      BigDecimal newRealAmount =
          originalCategoryItem.getRealAmount().subtract(actualTransactionAmount);
      originalCategoryItem.setRealAmount(newRealAmount);
      originalCategoryItem.setDifference(
          originalCategoryItem.getPlannedAmount().subtract(newRealAmount));
      updateEntity(originalCategoryItem);
      originalCategory = originalCategoryItem.getCategory();
      categoryService.updateRealAmountAndDifferenceWhenDeletingTransaction(originalCategory, actualTransactionAmount);
    }

    if (newCategoryItem != null) {
      BigDecimal newRealAmount = newCategoryItem.getRealAmount().add(actualTransactionAmount);
      newCategoryItem.setRealAmount(newRealAmount);
      newCategoryItem.setDifference(newCategoryItem.getPlannedAmount().subtract(newRealAmount));

      if (newCategoryItem.getCategory() != null) {
        // todo - update category amount when updating categoryItem
        Category newCategory = categoryService.findOrCreateCategoryOthers();
        newCategoryItem.setCategory(newCategory);
        categoryService.updateRealAmountAndDifferenceWithCategoryItem(null, newCategoryItem, actualTransactionAmount);
        //        categoryService.updatePlannedAmountRealAmountAndDifference(actualCategory,
        // newCategoryItem);
      }

      updateEntity(newCategoryItem);
    }
  }

  @Override
  public CategoryItem updateCategoryItemKeywords(UUID id, String keyword) {
    CategoryItem categoryItemToUpdate = getEntityById(id);
    if (categoryItemToUpdate.getKeywords() == null)
      categoryItemToUpdate.setKeywords(new HashSet<>());
    categoryItemToUpdate.getKeywords().add(keyword);
    return categoryItemRepository.save(categoryItemToUpdate);
  }

  @Override
  public List<CategoryItem> assignCategoryItemsToCategories(
      List<AssignCategoryCommandDto> assignCategoryCommandDtos) {
    List<CategoryItem> result = new ArrayList<>();

    for (AssignCategoryCommandDto assignCategoryCommandDto : assignCategoryCommandDtos) {
      CategoryItem categoryItemToUpdate =
          findCategoryItemByCode(assignCategoryCommandDto.getCategoryItemCode());

      Category newCategory =
          categoryService.findCategoryByCode(assignCategoryCommandDto.getCategoryCode());

      categoryService.updatePlannedAmountRealAmountAndDifference(newCategory, categoryItemToUpdate);
      categoryItemToUpdate.setCategory(newCategory);

      CategoryItem persistedCategoryItem = updateEntity(categoryItemToUpdate);

      result.add(persistedCategoryItem);
    }

    return result;
  }
}
