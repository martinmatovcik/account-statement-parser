package com.mm.accountstatementparser.repository;

import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
  List<Item> findAllByItemCategory(TransactionMainCategory category);

  @Query("SELECT SUM(i.plannedAmount) FROM Item i")
  BigDecimal sumPlannedAmountOfItems();

  @Query("SELECT SUM(i.realAmount) FROM Item i")
  BigDecimal sumRealAmountOfItems();

  @Query("SELECT SUM(i.difference) FROM Item i")
  BigDecimal sumDifferenceOfItems();

  @Query("SELECT SUM(i.plannedAmount) FROM Item i WHERE i.itemCategory = :category")
  BigDecimal sumPlannedAmountOfItemsForCategory(TransactionMainCategory category);

  @Query("SELECT SUM(i.realAmount) FROM Item i WHERE i.itemCategory = :category")
  BigDecimal sumRealAmountOfItemsForCategory(TransactionMainCategory category);

  @Query("SELECT SUM(i.difference) FROM Item i WHERE i.itemCategory = :category")
  BigDecimal sumDifferenceOfItemsForCategory(TransactionMainCategory category);
}
