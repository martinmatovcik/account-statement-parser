package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignCategoryCommandDto;
import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.EntityParent;
import com.mm.accountstatementparser.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface CategoryItemService extends CrudEntityService<CategoryItem> {
  List<CategoryItemDto> findCategoryItemsByCategory(Category category); //todo

  BigDecimal sumPlannedAmountOfCategoryItems();

  BigDecimal sumRealAmountOfCategoryItems();

  BigDecimal sumDifferenceOfCategoryItems();

  BigDecimal sumPlannedAmountOfCategoryItemsForCategory(Category category);

  BigDecimal sumRealAmountOfCategoryItemsForCategory(Category category);

  BigDecimal sumDifferenceOfCategoryItemsForCategory(Category category);

  BigDecimal sumLivingExpenses(boolean isPlanned);

  CategoryItem findCategoryItemByCode(String categoryItemCode);

  Optional<CategoryItem> findCategoryItemByKeywords(List<String> transactionKeywords);

  CategoryItem findOrCreateCategoryItemUnassigned();

  void updateCategoryItemRealAmountAndDifferenceWithTransaction(boolean wasUnassigned, Transaction transaction);

  BigDecimal calculateDifferenceForCategoryItem(CategoryItem categoryItem);

  CategoryItem updateCategoryItemKeywords(UUID id, String keyword);
  List<CategoryItem> assignCategoryItemToCategoryById(List<AssignCategoryCommandDto> assignCategoryCommandDtos);
}
