package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.ItemDto;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.TransactionMainCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ItemService {
  List<Item> getAllItems();

  Item getItemById(UUID id);

  Item persistItem(Item item);

  Item updateItemById(UUID id, Item entity);

  void deleteItemById(UUID id);

  List<ItemDto> findItemsByCategory(TransactionMainCategory category);

  BigDecimal sumPlannedAmountOfItems();

  BigDecimal sumRealAmountOfItems();

  BigDecimal sumDifferenceOfItems();

  BigDecimal sumPlannedAmountOfItemsForCategory(TransactionMainCategory category);

  BigDecimal sumRealAmountOfItemsForCategory(TransactionMainCategory category);

  BigDecimal sumDifferenceOfItemsForCategory(TransactionMainCategory category);

  void createSampleItemsWhenNoExisting();

  BigDecimal sumLivingExpenses(boolean isPlanned);
}
