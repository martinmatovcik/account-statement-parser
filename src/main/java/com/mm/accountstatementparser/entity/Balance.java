package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.BalanceDto;
import com.mm.accountstatementparser.dto.entityDto.DtoParent;
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
public class Balance extends EntityParent<BalanceDto> {
  @Id @GeneratedValue private UUID id;

  private Month month;
  private BigDecimal amount;
  private BalanceCategory balanceCategory;

  @Override
  public BalanceDto toDto() {
    return BalanceDto.builder()
        .id(this.id)
        .month(this.month)
        .amount(this.amount)
        .balanceCategory(this.balanceCategory)
        .build();
  }
}
