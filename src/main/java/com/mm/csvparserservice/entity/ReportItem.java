package com.mm.csvparserservice.entity;

import com.mm.csvparserservice.dto.ReportItemDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportItem extends EntityParent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Nullable
  private Long id;

  private String name;
  private BigDecimal plannedAmount = BigDecimal.ZERO;
  private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference = plannedAmount.subtract(realAmount);
  private TransactionMainCategory reportItemCategory;

  public ReportItem(String name, BigDecimal plannedAmount, TransactionMainCategory reportItemCategory) {
    this.name = name;
    this.plannedAmount = plannedAmount;
    this.reportItemCategory = reportItemCategory;
  }

  @Override
  public ReportItemDto toDto() {
    return ReportItemDto.builder()
        .id(this.id)
        .name(this.name)
        .plannedAmount(this.plannedAmount)
        .realAmount(this.realAmount)
        .difference(this.difference)
        .reportItemCategory(this.reportItemCategory)
        .build();
  }
}
