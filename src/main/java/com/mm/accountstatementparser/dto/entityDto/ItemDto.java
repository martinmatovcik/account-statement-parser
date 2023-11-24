package com.mm.accountstatementparser.dto.entityDto;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.Transaction;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
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
  @Builder.Default private BigDecimal plannedAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal realAmount = BigDecimal.ZERO;
  @Builder.Default private BigDecimal difference = BigDecimal.ZERO;
  @Nullable @Builder.Default @ToString.Exclude
  private Set<String> keywords = new HashSet<>();
  @Nullable @ToString.Exclude private Category category;
  @Nullable @Builder.Default @ToString.Exclude
  private List<Transaction> transactions = new ArrayList<>();

  @Override
  public Item toEntity() {
    return Item.builder()
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

  @Override
  public List<Object> toData() {
    return List.of(this.name, this.plannedAmount, this.realAmount, this.difference);
  }
}
