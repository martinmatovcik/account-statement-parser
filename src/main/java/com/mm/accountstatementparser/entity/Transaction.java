package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.DtoParent;
import com.mm.accountstatementparser.dto.entityDto.TransactionDto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends EntityParent<TransactionDto> {
  @Id @GeneratedValue private UUID id;

  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  private String variableSymbol;
  private String recipientMessage;
  private String transactionNote;

  @ManyToOne
  @JoinColumn(name = "categoryItem_id")
  @ToString.Exclude
  private CategoryItem categoryItem;

  @Override
  public TransactionDto toDto() {
    return TransactionDto.builder()
        .id(this.id)
        .date(this.date)
        .amount(this.amount)
        .currency(this.currency)
        .variableSymbol(this.variableSymbol)
        .recipientMessage(this.recipientMessage)
        .transactionNote(this.transactionNote)
        .itemId(this.categoryItem != null ? this.categoryItem.getId() : null)
        .build();
  }
}
