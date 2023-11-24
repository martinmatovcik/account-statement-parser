package com.mm.accountstatementparser.dto.entityDto;

import com.mm.accountstatementparser.entity.Balance;
import com.mm.accountstatementparser.entity.BalanceCategory;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDto extends DtoParent {

  @Nullable private UUID id;
  private Month month;
  private BigDecimal amount;
  private BalanceCategory balanceCategory;

  @Override
  public Balance toEntity() {
    return Balance.builder()
        .id(this.id)
        .month(this.month)
        .amount(this.amount)
        .balanceCategory(this.balanceCategory)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(this.id, this.month, this.amount, this.balanceCategory);
  }
}
