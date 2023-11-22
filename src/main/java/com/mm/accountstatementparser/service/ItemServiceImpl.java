package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.ItemDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    return itemRepository.findById(id).orElseThrow();
  }

  @Override
  public Item persistItem(Item item) {
    return itemRepository.save(item);
  }

  @Override
  public Item updateItemById(UUID id, Item updatedItem) {
    Item itemToUpdate = findByIdOrElseThrow(id);
    BeanUtils.copyProperties(updatedItem, itemToUpdate, "id");
    return itemRepository.save(itemToUpdate);
  }

  private Item findByIdOrElseThrow(UUID id) {
    return itemRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public void deleteItemById(UUID id) {
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
//    return itemRepository.sumPlannedAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumRealAmountOfItemsForCategory(Category category) {
    return BigDecimal.valueOf(2);
//    return itemRepository.sumRealAmountOfItemsForCategory(category);
  }

  @Override
  public BigDecimal sumDifferenceOfItemsForCategory(Category category) {
    return BigDecimal.valueOf(3);
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
  }

  @Override
  public Item findItemByCode(String itemCode) {
    return itemRepository
        .findByCode(itemCode)
        .orElseThrow(() -> new RuntimeException("Item wih given CODE does not exist"));
  }

  @Override
  public Item findItemForTransactionKeyWords(List<String> transactionKeywords) {
    return transactionKeywords.stream()
        .map(
            keyword ->
                itemRepository.findAll().stream()
                    .filter(item -> item.getKeywords().contains(keyword))
                    .findFirst())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElse(new Item()); // todo: return item-unassigned
  }

  @Override
  public void updateRealAmountAndDifference(Item item) {
    item.setRealAmount(
        item.getTransactions().stream()
            .map(itemTransaction -> itemTransaction.getAmount().abs())
            .reduce(BigDecimal.ZERO, BigDecimal::add));

    item.setDifference(item.getPlannedAmount().subtract(item.getRealAmount()));

    Category category = item.getCategory();
    if (category == null)
      ; // todo: assign category

    Item updatedItem = updateItemById(item.getId(), item);

    categoryService.updatePlanedAmountRealAmountAndDifference(category);
  }
}
