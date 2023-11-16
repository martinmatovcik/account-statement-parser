package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.ItemDto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item extends EntityParent {
  @Id @GeneratedValue private UUID id;

  private String code;
  private String name;
  private BigDecimal plannedAmount;
  private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference;
  private TransactionMainCategory itemCategory;

  @OneToMany(mappedBy = "item")
  private List<Transaction> transactions;

  public Item(
      String name, String code, BigDecimal plannedAmount, TransactionMainCategory itemCategory) {
    this.name = name;
    this.code = code;
    this.plannedAmount = plannedAmount;
    this.itemCategory = itemCategory;
    this.difference = calculateDifference();
  }

  @Override
  public ItemDto toDto() {
    return ItemDto.builder()
        .id(this.id)
        .name(this.name)
        .plannedAmount(this.plannedAmount)
        .realAmount(this.realAmount)
        .difference(this.difference)
        .itemCategory(this.itemCategory)
        .build();
  }

  private BigDecimal calculateDifference() {
    if (this.plannedAmount == null) this.plannedAmount = BigDecimal.ZERO;
    return plannedAmount.subtract(realAmount);
  }
}
