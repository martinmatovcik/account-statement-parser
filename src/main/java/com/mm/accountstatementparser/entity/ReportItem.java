package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.ReportItemDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportItem extends EntityParent {
  @Id
  @GeneratedValue
  private UUID id;

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
