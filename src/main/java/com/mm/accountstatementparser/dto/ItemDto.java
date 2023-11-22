package com.mm.accountstatementparser.dto;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.Transaction;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Month;
import java.util.*;
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
  private Month month;
  @Builder.Default private BigDecimal plannedAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference;
  private Category category;
  @Nullable private List<Transaction> transactions;
  @Nullable private Set<String> keywords;

  public ItemDto(String name, BigDecimal plannedAmount, Category category) {
    this.name = name;
    this.plannedAmount = plannedAmount;
    this.category = category;
  }

  @Override
  public Item toEntity() {
    return new Item(
        this.id,
        this.code,
        this.name,
        this.month,
        this.plannedAmount,
        this.realAmount,
        this.difference,
        this.keywords,
        this.category,
        this.transactions);
  }

  @Override
  public List<Object> toData() {
    return List.of(this.name, this.plannedAmount, this.realAmount, this.difference);
  }
}
