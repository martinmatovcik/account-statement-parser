package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.ItemDto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Set;
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
  private Month month;
  private BigDecimal plannedAmount;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal difference = BigDecimal.ZERO;
  @Transient
  private Set<String> keywords;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "item")
  private List<Transaction> transactions;

  public Item(
      String name, String code, BigDecimal plannedAmount, Category category) {
    this.name = name;
    this.code = code;
    this.plannedAmount = plannedAmount;
    this.category = category;
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
        .category(this.category)
        .build();
  }

  private BigDecimal calculateDifference() {
    return plannedAmount.subtract(realAmount);
  }
}
