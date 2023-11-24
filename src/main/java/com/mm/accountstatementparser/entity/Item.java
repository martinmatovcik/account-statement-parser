package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.ItemDto;
import jakarta.persistence.*;
import java.math.BigDecimal;
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
  @Builder.Default private BigDecimal plannedAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal difference = BigDecimal.ZERO;

  @ElementCollection @Builder.Default @ToString.Exclude
  private Set<String> keywords = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "category_id")
  @ToString.Exclude
  private Category category;

  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
  @Builder.Default
  @ToString.Exclude
  private List<Transaction> transactions = new ArrayList<>();

  @Override
  public ItemDto toDto() {
    return ItemDto.builder()
            .id(this.id)
            .code(this.code)
            .name(this.name)
            .plannedAmount(this.plannedAmount)
            .realAmount(this.realAmount)
            .difference(this.difference)
            .keywords(this.keywords)
            .category(this.category)
            .transactions(this.transactions)
            .build();
  }
}
