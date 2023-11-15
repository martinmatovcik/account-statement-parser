package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.BalanceDto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Month;
import java.util.UUID;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance extends EntityParent {
  @Id @GeneratedValue private UUID id;

  private Month month;
  private BigDecimal amount;
  private BalanceCategory balanceCategory;

  @Override
  public BalanceDto toDto() {
    return new BalanceDto(this.id, this.month, this.amount, this.balanceCategory);
  }
}
