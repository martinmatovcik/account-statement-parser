package com.mm.accountstatementparser.dto.entityDto;

import com.mm.accountstatementparser.entity.Item;
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
  @Nullable private UUID categoryId;
  @Nullable @Builder.Default @ToString.Exclude
  private List<TransactionDto> transactions = new ArrayList<>();

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
        .transactions(this.transactions != null ? this.transactions.stream().map(TransactionDto::toEntity).toList() : null)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(this.name, this.plannedAmount, this.realAmount, this.difference);
  }
}
