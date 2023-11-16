package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.ItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;

  @Override
  public List<Item> getAllItems() {
    return itemRepository.findAll();
  }

  @Override
  public Item getItemById(UUID id) {
    return itemRepository.findById(id).orElseThrow();
  }

  @Override
  public Item persistItem(Item item) {
    return itemRepository.save(item);
  }

  @Override
  public Item updateItemById(UUID id, Item updatedItem) {
    Item itemToUpdate = itemRepository.getReferenceById(id);
    BeanUtils.copyProperties(updatedItem, itemToUpdate, "id");
    return itemRepository.save(itemToUpdate);
  }

  @Override
  public void deleteItemById(UUID id) {
    itemRepository.deleteById(id);
  }

  @Override
  public List<ItemDto> findItemsByCategory(Category category) {
    return itemRepository.findAllByItemCategory(category).stream()
        .map(Item::toDto)
        .toList();
  }

  @Override
  public BigDecimal sumPlannedAmountOfItems() {
    return itemRepository.sumPlannedAmountOfItems();
  }

  @Override
  public BigDecimal sumRealAmountOfItems() {
    return itemRepository.sumRealAmountOfItems();
  }

  @Override
  public BigDecimal sumDifferenceOfItems() {
    return itemRepository.sumDifferenceOfItems();
  }

  @Override
  public BigDecimal sumPlannedAmountOfItemsForCategory(Category category) {
    return itemRepository.sumPlannedAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumRealAmountOfItemsForCategory(Category category) {
    return itemRepository.sumRealAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumDifferenceOfItemsForCategory(Category category) {
    return itemRepository.sumDifferenceOfItemsForCategory(category);
  }

  @Override
  public void createSampleItemsWhenNoExisting() {
    if (getAllItems().isEmpty()) {
      List<Item> items =
          List.of(
              new Item("Nájom", "rent", BigDecimal.valueOf(17300.00), Category.NEEDS),
              new Item(
                  "Elektrina", "energies", BigDecimal.valueOf(1000.00), Category.NEEDS),
              new Item("Internet", "internet", BigDecimal.valueOf(300.00), Category.NEEDS),
              new Item("Telefóny", "phones", BigDecimal.valueOf(960.00), Category.NEEDS),
              new Item("Lítačky", "mhd", BigDecimal.valueOf(680.00), Category.NEEDS),
              new Item("Jedlo", "eating", BigDecimal.valueOf(10000.00), Category.NEEDS),
              new Item(
                  "Greenfox - Mišovci", "greenfox-loan", BigDecimal.valueOf(0.00), Category.LOANS),
              new Item(
                  "Bývanie - rodičia", "living-loan", BigDecimal.valueOf(2500.00), Category.LOANS),
              new Item(
                  "Oblečenie", "clother", BigDecimal.valueOf(0.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Netflix", "netflix", BigDecimal.valueOf(120.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Spotify", "spotify", BigDecimal.valueOf(60.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Kultúra", "culture", BigDecimal.valueOf(0.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Rande", "date", BigDecimal.valueOf(0.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Eating out", "eating-out", BigDecimal.valueOf(0.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Cestovanie", "traveling",
                      BigDecimal.valueOf(0.00), Category.FUN_WANTS_GIFTS),
              new Item(
                  "Charita", "charity", BigDecimal.valueOf(0.00), Category.FUN_WANTS_GIFTS),
              new Item("Dôchodok", "pension", BigDecimal.valueOf(0.00), Category.SAVINGS),
              new Item(
                  "Krátkodobé", "short-term", BigDecimal.valueOf(0.00), Category.SAVINGS),
              new Item(
                  "Finančná rezerva", "reserve", BigDecimal.valueOf(0.00), Category.SAVINGS),
              new Item("Neznáme", "other", BigDecimal.valueOf(0.00), Category.OTHERS));

      for (Item item : items) {
        persistItem(item);
      }
    }
  }

  @Override
  public BigDecimal sumLivingExpenses(boolean isPlanned) {
    return isPlanned
        ? sumPlannedAmountOfItemsForCategory(Category.NEEDS)
            .add(sumPlannedAmountOfItemsForCategory(Category.LOANS))
        : sumRealAmountOfItemsForCategory(Category.NEEDS)
            .add(sumRealAmountOfItemsForCategory(Category.LOANS));
  }

  @Override
  public Item getItemByCode(String itemCode) {
    return itemRepository.findByCode(itemCode).orElseThrow(() -> new RuntimeException("Item wih given CODE does not exist"));
  }
}
