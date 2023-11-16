package com.mm.accountstatementparser.dto;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.Transaction;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto extends DtoParent {
  @Nullable private UUID id;
  private String code;
  private String name;
  private BigDecimal plannedAmount;
  private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference = calculateDifference();
  private Category itemCategory;
  private List<Transaction> transactions = new ArrayList<>();

  public ItemDto(
      String name, BigDecimal plannedAmount, Category itemCategory) {
    this.name = name;
    this.plannedAmount = plannedAmount;
    this.itemCategory = itemCategory;
  }

  @Override
  public Item toEntity() {
    return new Item(
        this.id,
        this.code,
        this.name,
        this.plannedAmount,
        this.realAmount,
        calculateDifference(),
        this.itemCategory,
        this.transactions);
  }

  @Override
  public List<Object> toData() {
    return List.of(this.name, this.plannedAmount, this.realAmount, this.difference);
  }

  private BigDecimal calculateDifference() {
    if (this.plannedAmount == null) this.plannedAmount = BigDecimal.ZERO;
    return plannedAmount.subtract(realAmount);
  }
}
