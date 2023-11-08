package com.mm.csvparserservice.dto;

import com.mm.csvparserservice.model.Transaction;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class TransactionDto extends DtoParent {
  @Nullable private Long transactionId;
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
  private String bicCode;
  private Long fioInstructionId;

  @Override
  public Transaction toEntity() {
    return Transaction.builder()
        .transactionId(this.transactionId)
        .fioInstructionId(this.fioInstructionId)
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
        .bicCode(this.bicCode)
        .fioInstructionId(this.fioInstructionId)
        .build();
  }

  @Override
  public List<Object> toData() {
    return List.of(
        this.transactionId,
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
        this.bicCode,
        this.fioInstructionId);
  }

  @Override
  public String toString() {
    return "TransactionDto{" +
            "transactionId=" + transactionId +
            ", fioOperationId=" + fioOperationId +
            ", date=" + date +
            ", amount=" + amount +
            ", currency=" + currency +
            ", recipientAccount='" + recipientAccount + '\'' +
            ", recipientAccountName='" + recipientAccountName + '\'' +
            ", bankCode=" + bankCode +
            ", bankName='" + bankName + '\'' +
            ", constantSymbol='" + constantSymbol + '\'' +
            ", variableSymbol='" + variableSymbol + '\'' +
            ", specificSymbol='" + specificSymbol + '\'' +
            ", transactionNote='" + transactionNote + '\'' +
            ", recipientMessage='" + recipientMessage + '\'' +
            ", transactionType='" + transactionType + '\'' +
            ", carriedOut='" + carriedOut + '\'' +
            ", transactionSpecification='" + transactionSpecification + '\'' +
            ", bicCode='" + bicCode + '\'' +
            ", fioInstructionId=" + fioInstructionId +
            '}';
  }
}
