package com.mm.accountstatementparser.repository;

import com.mm.accountstatementparser.entity.CategoryItem;
import com.mm.accountstatementparser.entity.Category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryItemRepository extends JpaRepository<CategoryItem, UUID> {
  List<CategoryItem> findAllByCategory(Category category);

  @Query("SELECT SUM(i.plannedAmount) FROM CategoryItem i")
  BigDecimal sumPlannedAmountOfCategoryItems();

  @Query("SELECT SUM(i.realAmount) FROM CategoryItem i")
  BigDecimal sumRealAmountOfCategoryItems();

  @Query("SELECT SUM(i.difference) FROM CategoryItem i")
  BigDecimal sumDifferenceOfCategoryItems();

//  @Query("SELECT SUM(i.plannedAmount) FROM CategoryItem i WHERE i.itemCategory = :category")
//  BigDecimal sumPlannedAmountOfItemsForCategory(Category category);
//
//  @Query("SELECT SUM(i.realAmount) FROM CategoryItem i WHERE i.itemCategory = :category")
//  BigDecimal sumRealAmountOfItemsForCategory(Category category);
//
//  @Query("SELECT SUM(i.difference) FROM CategoryItem i WHERE i.itemCategory = :category")
//  BigDecimal sumDifferenceOfItemsForCategory(Category category);

  Optional<CategoryItem> findByCode(String code);
}
