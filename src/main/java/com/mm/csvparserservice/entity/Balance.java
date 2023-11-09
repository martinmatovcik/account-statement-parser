package com.mm.csvparserservice.entity;

import com.mm.csvparserservice.dto.BalanceDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Month;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance extends EntityParent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Nullable
  private Long id;

  private Month month;
  private BigDecimal amount;
  private BalanceCategory balanceCategory;

  @Override
  public BalanceDto toDto() {
    return new BalanceDto(this.id, this.month, this.amount, this.balanceCategory);
  }
}
