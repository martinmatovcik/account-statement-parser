package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.ReportItemDto;
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
public class ReportItem extends EntityParent {
  @Id @GeneratedValue private UUID id;

  private String code;
  private String name;
  private BigDecimal plannedAmount;
  private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference;
  private TransactionMainCategory reportItemCategory;

  @OneToMany(mappedBy = "reportItem")
  private List<Transaction> transactions;

  public ReportItem(
      String name, String code, BigDecimal plannedAmount, TransactionMainCategory reportItemCategory) {
    this.name = name;
    this.code = code;
    this.plannedAmount = plannedAmount;
    this.reportItemCategory = reportItemCategory;
    this.difference = calculateDifference();
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

  private BigDecimal calculateDifference() {
    if (this.plannedAmount == null) this.plannedAmount = BigDecimal.ZERO;
    return plannedAmount.subtract(realAmount);
  }
}
