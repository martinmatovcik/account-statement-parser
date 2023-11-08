package com.mm.csvparserservice.model;

import com.mm.csvparserservice.dto.TransactionDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction extends EntityParent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Nullable
  private Long transactionId;

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
  private String transactionType; // Todo: enum
  private String carriedOut;
  private String transactionSpecification;
  private String bicCode;
  private Long fioInstructionId;

  // Todo: delete this YAGNI
//  public Transaction(
//      Long fioOperationId,
//      LocalDate date,
//      BigDecimal amount,
//      Currency currency,
//      String recipientAccount,
//      String recipientAccountName,
//      Long bankCode,
//      String bankName,
//      String constantSymbol,
//      String variableSymbol,
//      String specificSymbol,
//      String transactionNote,
//      String recipientMessage,
//      String transactionType,
//      String carriedOut,
//      String transactionSpecification,
//      String bicCode,
//      Long fioInstructionId) {
//    this.fioOperationId = fioOperationId;
//    this.date = date;
//    this.amount = amount;
//    this.currency = currency;
//    this.recipientAccount = recipientAccount;
//    this.recipientAccountName = recipientAccountName;
//    this.bankCode = bankCode;
//    this.bankName = bankName;
//    this.constantSymbol = constantSymbol;
//    this.variableSymbol = variableSymbol;
//    this.specificSymbol = specificSymbol;
//    this.transactionNote = transactionNote;
//    this.recipientMessage = recipientMessage;
//    this.transactionType = transactionType;
//    this.carriedOut = carriedOut;
//    this.transactionSpecification = transactionSpecification;
//    this.bicCode = bicCode;
//    this.fioInstructionId = fioInstructionId;
//  }

  @Override
  public TransactionDto toDto() {
    return TransactionDto.builder()
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
}
