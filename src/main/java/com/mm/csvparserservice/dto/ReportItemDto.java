package com.mm.csvparserservice.dto;

import com.mm.csvparserservice.entity.ReportItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.mm.csvparserservice.entity.TransactionMainCategory;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportItemDto extends DtoParent {
  private UUID id;
  private String name;
  private BigDecimal plannedAmount = BigDecimal.ZERO;
  private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference = plannedAmount.subtract(realAmount);
  private TransactionMainCategory reportItemCategory;

  @Override
  public ReportItem toEntity() {
    return ReportItem.builder()
        .id(this.id)
        .name(this.name)
        .plannedAmount(this.plannedAmount)
        .realAmount(this.realAmount)
        .difference(this.difference)
        .reportItemCategory(this.reportItemCategory)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(this.name, this.plannedAmount, this.realAmount, this.difference);
  }
}
