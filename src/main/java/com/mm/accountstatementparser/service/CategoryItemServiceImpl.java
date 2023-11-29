package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.repository.CategoryItemRepository;

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
  public CategoryItem updateEntityById(UUID id, CategoryItem updatedEntity) {
    CategoryItem entityToUpdate = getEntityById(id);
    BeanUtils.copyProperties(updatedEntity, entityToUpdate, "id");
    return categoryItemRepository.save(entityToUpdate);
  }

  @Override
  public CategoryItem updateFieldsInEntityById(UUID id, Map<Object, Object> fields) {
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
    return categoryItemRepository.findAllByCategory(category).stream().map(CategoryItem::toDto).toList();
  }

  @Override
  public BigDecimal sumPlannedAmountOfCategoryItems() {
    return categoryItemRepository.sumPlannedAmountOfCategoryItems();
  }

  @Override
  public BigDecimal sumRealAmountOfCategoryItems() {
    return categoryItemRepository.sumRealAmountOfCategoryItems();
  }

  @Override
  public BigDecimal sumDifferenceOfCategoryItems() {
    return categoryItemRepository.sumDifferenceOfCategoryItems();
  }

  @Override
  public BigDecimal sumPlannedAmountOfCategoryItemsForCategory(Category category) {
    return BigDecimal.valueOf(1);
    // todo: 1
    //    return itemRepository.sumPlannedAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumRealAmountOfCategoryItemsForCategory(Category category) {
    return BigDecimal.valueOf(2);
    // todo: 2
    //    return itemRepository.sumRealAmountOfItemsForCategory(category);
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
  public void updateCategoryItemRealAmountAndDifferenceWithTransaction(boolean wasUnassigned, Transaction transaction) {
    CategoryItem categoryItem = transaction.getCategoryItem();
    BigDecimal transactionAmount = transaction.getAmount().abs();

    categoryItem.setRealAmount(categoryItem.getRealAmount().add(transactionAmount));

    if (wasUnassigned) {
      CategoryItem unassignedCategoryItem = findOrCreateCategoryItemUnassigned();
      unassignedCategoryItem.setRealAmount(unassignedCategoryItem.getRealAmount().subtract(transactionAmount));
      unassignedCategoryItem.setDifference(calculateDifferenceForCategoryItem(unassignedCategoryItem));

      updateEntityById(unassignedCategoryItem.getId(), unassignedCategoryItem);
    }

    categoryItem.setDifference(calculateDifferenceForCategoryItem(categoryItem));

    Category category = categoryItem.getCategory();
    if (category == null)
      ; // todo: assign category

    updateEntityById(categoryItem.getId(), categoryItem);

    if (category != null) categoryService.updatePlanedAmountRealAmountAndDifference(category);
  }

  @Override
  public BigDecimal calculateDifferenceForCategoryItem(CategoryItem categoryItem) {
    return categoryItem.getPlannedAmount().subtract(categoryItem.getRealAmount());
  }

  @Override
  public CategoryItem updateCategoryItemKeywords(UUID id, String keyword) {
    CategoryItem categoryItemToUpdate = getEntityById(id);
    if (categoryItemToUpdate.getKeywords() == null) categoryItemToUpdate.setKeywords(new HashSet<>());
    categoryItemToUpdate.getKeywords().add(keyword);
    return categoryItemRepository.save(categoryItemToUpdate);
  }
}
