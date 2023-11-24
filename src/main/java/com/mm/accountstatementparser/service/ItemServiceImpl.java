package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.entityDto.ItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final CategoryService categoryService;

  @Override
  public List<Item> getAllItems() {
    return itemRepository.findAll();
  }

  @Override
  public Item getItemById(UUID id) {
    return itemRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public Item persistItem(Item item) {
    return itemRepository.save(item);
  }

  @Override
  public Item updateItemById(UUID id, Item updatedItem) {
    Item itemToUpdate = getItemById(id);
    BeanUtils.copyProperties(updatedItem, itemToUpdate, "id");
    return itemRepository.save(itemToUpdate);
  }

  @Override
  public void deleteItemById(UUID id) {
    getItemById(id);
    itemRepository.deleteById(id);
  }

  @Override
  public List<ItemDto> findItemsByCategory(Category category) {
    return itemRepository.findAllByCategory(category).stream().map(Item::toDto).toList();
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
    return BigDecimal.valueOf(1);
    // todo: 1
    //    return itemRepository.sumPlannedAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumRealAmountOfItemsForCategory(Category category) {
    return BigDecimal.valueOf(2);
    // todo: 2
    //    return itemRepository.sumRealAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumDifferenceOfItemsForCategory(Category category) {
    return BigDecimal.valueOf(3);
    // todo: 3
    //    return itemRepository.sumDifferenceOfItemsForCategory(category);
  }

  @Override
  public void createSampleItemsWhenNoExisting() {
    if (getAllItems().isEmpty()) {
      List<Item> items =
          List.of(
              new Item("Nájom", "rent", BigDecimal.valueOf(17300.00), new Category()),
              new Item("Elektrina", "energies", BigDecimal.valueOf(1000.00), new Category()),
              new Item("Internet", "internet", BigDecimal.valueOf(300.00), new Category()),
              new Item("Telefóny", "phones", BigDecimal.valueOf(960.00), new Category()),
              new Item("Lítačky", "mhd", BigDecimal.valueOf(680.00), new Category()),
              new Item("Jedlo", "eating", BigDecimal.valueOf(10000.00), new Category()),
              new Item(
                  "Greenfox - Mišovci", "greenfox-loan", BigDecimal.valueOf(0.00), new Category()),
              new Item(
                  "Bývanie - rodičia", "living-loan", BigDecimal.valueOf(2500.00), new Category()),
              new Item("Oblečenie", "clother", BigDecimal.valueOf(0.00), new Category()),
              new Item("Netflix", "netflix", BigDecimal.valueOf(120.00), new Category()),
              new Item("Spotify", "spotify", BigDecimal.valueOf(60.00), new Category()),
              new Item("Kultúra", "culture", BigDecimal.valueOf(0.00), new Category()),
              new Item("Rande", "date", BigDecimal.valueOf(0.00), new Category()),
              new Item("Eating out", "eating-out", BigDecimal.valueOf(0.00), new Category()),
              new Item("Cestovanie", "traveling", BigDecimal.valueOf(0.00), new Category()),
              new Item("Charita", "charity", BigDecimal.valueOf(0.00), new Category()),
              new Item("Dôchodok", "pension", BigDecimal.valueOf(0.00), new Category()),
              new Item("Krátkodobé", "short-term", BigDecimal.valueOf(0.00), new Category()),
              new Item("Finančná rezerva", "reserve", BigDecimal.valueOf(0.00), new Category()),
              new Item("Neznáme", "other", BigDecimal.valueOf(0.00), new Category()));

      for (Item item : items) {
        persistItem(item);
      }
    }
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
  public Item findItemByCode(String itemCode) {
    return itemRepository
        .findByCode(itemCode)
        .orElseThrow(() -> new RuntimeException("Item wih given CODE does not exist"));
  }

  @Override
  public Optional<Item> findItemByKeywords(List<String> transactionKeywords) {
    List<Item> items = itemRepository.findAll();

    for (Item item : items) {
      if (item == null) break;
      for (String keyword : transactionKeywords)
        if (item.getKeywords().contains(keyword)) return Optional.of(item);
    }
    return Optional.empty();

    //    return items.stream()
    //            .filter(item -> item.getKeywords() != null)
    //            .filter(item -> !Collections.disjoint(item.getKeywords(), transactionKeywords))
    //            .findFirst();

    //    List<Item> items = itemRepository.findAll();
    //
    //    for (Item item : items) {
    //      if (item.getKeywords() == null) item.setKeywords(new HashSet<>());
    //    }
  }

  @Override
  public Item findOrCreateItemUnassigned() {
    return itemRepository
        .findByCode("unassigned")
        .orElseGet(
            () ->
                persistItem(
                    new Item(
                        "Nezaradané výdavky",
                        "unassigned",
                        categoryService.findOrCreateCategoryOthers())));
  }

  @Override
  public void updateRealAmountAndDifference(Item item) {
    if (!CollectionUtils.isEmpty(item.getTransactions()))
      item.setRealAmount(
          item.getTransactions().stream()
              .map(itemTransaction -> itemTransaction.getAmount().abs())
              .reduce(BigDecimal.ZERO, BigDecimal::add));

    item.setDifference(item.getPlannedAmount().subtract(item.getRealAmount()));

    Category category = item.getCategory();
    if (category == null)
      ; // todo: assign category

    Item updatedItem = updateItemById(item.getId(), item);

    if (category != null) categoryService.updatePlanedAmountRealAmountAndDifference(category);
  }

  @Override
  public Item updateKeywords(UUID id, String keyword) {
    Item itemToUpdate = getItemById(id);
    if (itemToUpdate.getKeywords() == null) itemToUpdate.setKeywords(new HashSet<>());
    itemToUpdate.getKeywords().add(keyword);
    return itemRepository.save(itemToUpdate);
  }
}
