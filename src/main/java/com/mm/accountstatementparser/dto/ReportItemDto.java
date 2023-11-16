package com.mm.accountstatementparser.dto;

import com.mm.accountstatementparser.entity.ReportItem;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportItemDto extends DtoParent {
  @Nullable private UUID id;
  private String code;
  private String name;
  private BigDecimal plannedAmount;
  private BigDecimal realAmount = BigDecimal.ZERO;
  private BigDecimal difference = calculateDifference();
  private TransactionMainCategory reportItemCategory;
  private List<Transaction> transactions = new ArrayList<>();

  public ReportItemDto(
      String name, BigDecimal plannedAmount, TransactionMainCategory reportItemCategory) {
    this.name = name;
    this.plannedAmount = plannedAmount;
    this.reportItemCategory = reportItemCategory;
  }

  @Override
  public ReportItem toEntity() {
    return new ReportItem(
        this.id,
        this.code,
        this.name,
        this.plannedAmount,
        this.realAmount,
        calculateDifference(),
        this.reportItemCategory,
        this.transactions);
  }

  @Override
  public List<Object> toData() {
    return List.of(this.name, this.plannedAmount, this.realAmount, this.difference);
  }

  private BigDecimal calculateDifference() {
    if (this.plannedAmount == null) this.plannedAmount = BigDecimal.ZERO;
    return plannedAmount.subtract(realAmount);
  }
}
