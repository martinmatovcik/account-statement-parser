package com.mm.accountstatementparser.dto;

import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
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
  @Nullable private UUID transactionId;
  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  @Nullable private String variableSymbol;
  @Nullable private String recipientMessage;
  @Nullable private String transactionNote;
  private TransactionMainCategory transactionMainCategory;
  private Item item;

  @Override
  public Transaction toEntity() {
    return Transaction.builder()
        .transactionId(this.transactionId)
        .date(this.date)
        .amount(this.amount)
        .currency(this.currency)
        .variableSymbol(this.variableSymbol)
        .recipientMessage(this.recipientMessage)
        .transactionNote(this.transactionNote)
        .transactionMainCategory(this.transactionMainCategory)
        .item(this.item)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(
        this.transactionId.toString(),
        this.date.toString(),
        this.amount,
        this.currency.toString(),
        this.variableSymbol,
        this.recipientMessage,
        this.transactionNote,
        this.transactionMainCategory.toString());
  }
}
