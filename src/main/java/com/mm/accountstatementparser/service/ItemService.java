package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.entityDto.ItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemService {
  List<Item> getAllItems();

  Item getItemById(UUID id);

  Item persistItem(Item item);

  Item updateItemById(UUID id, Item entity);

  void deleteItemById(UUID id);

  List<ItemDto> findItemsByCategory(Category category);

  BigDecimal sumPlannedAmountOfItems();

  BigDecimal sumRealAmountOfItems();

  BigDecimal sumDifferenceOfItems();

  BigDecimal sumPlannedAmountOfItemsForCategory(Category category);

  BigDecimal sumRealAmountOfItemsForCategory(Category category);

  BigDecimal sumDifferenceOfItemsForCategory(Category category);

  void createSampleItemsWhenNoExisting();

  BigDecimal sumLivingExpenses(boolean isPlanned);

  Item findItemByCode(String itemCode);

  Optional<Item> findItemByKeywords(List<String> transactionKeywords);

  Item findOrCreateItemUnassigned();

  void updateItemRealAmountAndDifferenceWithTransaction(boolean wasUnassigned, Transaction transaction);

  BigDecimal calculateAndSetRealAmountForItem(Item item);

  BigDecimal calculateDifferenceForItem(Item item);

  Item updateKeywords(UUID id, String keyword);
}
