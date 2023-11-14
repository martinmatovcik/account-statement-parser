package com.mm.csvparserservice.dto;

import com.mm.csvparserservice.entity.Balance;
import com.mm.csvparserservice.entity.BalanceCategory;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BalanceDto extends DtoParent {

  @Nullable private UUID id;
  private Month month;
  private BigDecimal amount;
  private BalanceCategory balanceCategory;

  @Override
  public Balance toEntity() {
    return new Balance(this.id, this.month, this.amount, this.balanceCategory);
  }

  @Override
  public List<Object> toData() {
    return List.of(this.id, this.month, this.amount, this.balanceCategory);
  }
}
