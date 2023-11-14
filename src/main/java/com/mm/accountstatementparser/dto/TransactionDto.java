package com.mm.accountstatementparser.dto;

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
  private Long fioOperationId;
  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  private String recipientAccount;
  private String recipientAccountName;
  private Long bankCode;
  private String bankName;
  private String constantSymbol;
  private String variableSymbol;
  private String specificSymbol;
  private String transactionNote;
  private String recipientMessage;
  private String transactionType;
  private String carriedOut;
  private String transactionSpecification;
  private String note;
  private String bicCode;
  private Long fioInstructionId;
  private TransactionMainCategory transactionMainCategory;

  @Override
  public Transaction toEntity() {
    return Transaction.builder()
        .transactionId(this.transactionId)
        .fioOperationId(this.fioOperationId)
        .date(this.date)
        .amount(this.amount)
        .currency(this.currency)
        .recipientAccount(this.recipientAccount)
        .recipientAccountName(this.recipientAccountName)
        .bankCode(this.bankCode)
        .bankName(this.bankName)
        .constantSymbol(this.constantSymbol)
        .variableSymbol(this.variableSymbol)
        .specificSymbol(this.specificSymbol)
        .transactionNote(this.transactionNote)
        .recipientMessage(this.recipientMessage)
        .transactionType(this.transactionType)
        .carriedOut(this.carriedOut)
        .transactionSpecification(this.transactionSpecification)
        .note(this.note)
        .bicCode(this.bicCode)
        .fioInstructionId(this.fioInstructionId)
        .transactionMainCategory(this.transactionMainCategory)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(
        this.transactionId.toString(),
        this.fioInstructionId,
        this.date.toString(),
        this.amount,
        this.currency.toString(),
        this.recipientAccount,
        this.recipientAccountName,
        this.bankCode,
        this.bankName,
        this.constantSymbol,
        this.variableSymbol,
        this.specificSymbol,
        this.transactionNote,
        this.recipientMessage,
        this.transactionType,
        this.carriedOut,
        this.transactionSpecification,
        this.note,
        this.bicCode,
        this.fioInstructionId,
        this.transactionMainCategory.toString());
  }
}