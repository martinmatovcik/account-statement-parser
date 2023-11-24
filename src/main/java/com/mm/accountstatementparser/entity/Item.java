package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.ItemDto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Month;
import java.util.*;

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
  @Builder.Default private BigDecimal plannedAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal difference = BigDecimal.ZERO;
  @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "item_keywords", joinColumns = @JoinColumn(name = "id"))
  @Column(name = "keywords")
  @ToString.Exclude
  private Set<String> keywords;

  @ManyToOne
  @JoinColumn(name = "category_id")
  @ToString.Exclude
  private Category category;

  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Transaction> transactions;

  public Item(
      String name, String code, BigDecimal plannedAmount, Category category) {
    this.name = name;
    this.code = code;
    this.plannedAmount = plannedAmount;
    this.category = category;
    this.difference = BigDecimal.valueOf(111);
  }

  public Item(
          String name, String code, Category category) {
    this.name = name;
    this.code = code;
    this.plannedAmount = BigDecimal.ZERO;
    this.realAmount = BigDecimal.ZERO;
    this.difference = BigDecimal.ZERO;
    this.keywords = new HashSet<>();
    this.category = category;
    this.transactions = new ArrayList<>();
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
