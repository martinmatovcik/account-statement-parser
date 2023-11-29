package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.repository.CategoryItemRepository;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryItemServiceImpl implements CategoryItemService {
  private final CategoryItemRepository categoryItemRepository;
  private final CategoryService categoryService;

  @Override
  public List<CategoryItem> getAllCategoryItems() {
    return categoryItemRepository.findAll();
  }

  @Override
  public CategoryItem getCategoryItemById(UUID id) {
    return categoryItemRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public CategoryItem persistCategoryItem(CategoryItem categoryItem) {
    return categoryItemRepository.save(categoryItem);
  }

  @Override
  public CategoryItem updateCategoryItemById(UUID id, CategoryItem categoryItem) {
    CategoryItem categoryItemToUpdate = getCategoryItemById(id);
    BeanUtils.copyProperties(categoryItem, categoryItemToUpdate, "id");
    return categoryItemRepository.save(categoryItemToUpdate);
  }

  @Override
  public void deleteCategoryItemById(UUID id) {
    getCategoryItemById(id);
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
                persistCategoryItem(
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

      updateCategoryItemById(unassignedCategoryItem.getId(), unassignedCategoryItem);
    }

    categoryItem.setDifference(calculateDifferenceForCategoryItem(categoryItem));

    Category category = categoryItem.getCategory();
    if (category == null)
      ; // todo: assign category

    updateCategoryItemById(categoryItem.getId(), categoryItem);

    if (category != null) categoryService.updatePlanedAmountRealAmountAndDifference(category);
  }

  @Override
  public BigDecimal calculateDifferenceForCategoryItem(CategoryItem categoryItem) {
    return categoryItem.getPlannedAmount().subtract(categoryItem.getRealAmount());
  }

  @Override
  public CategoryItem updateCategoryItemKeywords(UUID id, String keyword) {
    CategoryItem categoryItemToUpdate = getCategoryItemById(id);
    if (categoryItemToUpdate.getKeywords() == null) categoryItemToUpdate.setKeywords(new HashSet<>());
    categoryItemToUpdate.getKeywords().add(keyword);
    return categoryItemRepository.save(categoryItemToUpdate);
  }
}
