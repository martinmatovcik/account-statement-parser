package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignCategoryCommandDto;
import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Transaction;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryItemService extends CrudEntityService<CategoryItem> {
  List<CategoryItemDto> findCategoryItemsByCategory(Category category); //todo

  BigDecimal sumPlannedAmountOfCategoryItems();

  BigDecimal sumPlannedAmountOfCategoryItemsForCategory(Category category);

  BigDecimal sumDifferenceOfCategoryItemsForCategory(Category category);

  BigDecimal sumLivingExpenses(boolean isPlanned);

  CategoryItem findCategoryItemByCode(String categoryItemCode);

  Optional<CategoryItem> findCategoryItemByKeywords(List<String> transactionKeywords);

  CategoryItem findOrCreateCategoryItemUnassigned();

  void updateCategoryItemRealAmountAndDifferenceWithTransaction(@Nullable CategoryItem newCategoryItem, Transaction transaction);

  CategoryItem updateCategoryItemKeywords(UUID id, String keyword);
  List<CategoryItem> assignCategoryItemsToCategories(List<AssignCategoryCommandDto> assignCategoryCommandDtos);
}
