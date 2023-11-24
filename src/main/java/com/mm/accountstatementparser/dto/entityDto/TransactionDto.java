package com.mm.accountstatementparser.dto.entityDto;

import com.mm.accountstatementparser.entity.Transaction;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class TransactionDto extends DtoParent {
  @Nullable private UUID id;
  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  @Nullable private String variableSymbol;
  @Nullable private String recipientMessage;
  private String transactionNote;
  @Nullable private UUID itemId;

  @Override
  public Transaction toEntity() {
    return Transaction.builder()
        .id(this.id)
        .date(this.date)
        .amount(this.amount)
        .currency(this.currency)
        .variableSymbol(this.variableSymbol)
        .recipientMessage(this.recipientMessage)
        .transactionNote(this.transactionNote)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(
        this.id != null ? this.id.toString() : "",
        this.date.toString(),
        this.amount,
        this.currency.toString(),
        this.variableSymbol != null ? this.variableSymbol : "",
        this.recipientMessage != null ? this.recipientMessage : "",
        this.transactionNote != null ? this.transactionNote : "",
        this.itemId != null ? this.itemId.toString() : ""
    );
  }
}
