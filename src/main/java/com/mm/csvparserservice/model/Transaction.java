package com.mm.csvparserservice.model;

import com.mm.csvparserservice.dto.TransactionDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.Objects;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  private String note;
  private String bicCode;
  private Long fioInstructionId;
  @Nullable private MainCategory mainCategory;

  @Override
  public TransactionDto toDto() {
    return TransactionDto.builder()
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
        .mainCategory(this.mainCategory)
        .build();
  }

  public MainCategory findMainCategory() {

    String[] needsKeywords = {
      "najom",
      "lidl",
      "kaufland",
      "kaufla",
      "operator ict",
      "albert",
      "lavor",
      "wc",
      "lekarna",
      "dr.max",
      "elektrina",
      "pekarna",
      "krusta",
      "dm"
    };

    String[] savingsKeywords = {"portu", "investice"};
    String[] loansKeywords = {"splatka", "pozicka"};

    String[] funKeywords = {
      "fruitisimo",
      "paulhl.n.",
      "primark",
      "mcd",
      "mcdonalds",
      "forno",
      "zalando",
      "geco",
      "datart",
      "hm",
      "hennesmauritz",
      "pull & bear",
      "kfc",
      "kvetinarstvo",
      "jysk",
      "qerko",
      "mad rabbit",
      "hrad",
      "dish belgicka",
      "nyx",
      "cd.cz",
      "studentagency.cz",
      "leoexpress",
      "stradivarius"
    };

    if (Objects.requireNonNull(this.amount).compareTo(BigDecimal.ZERO) > 0) {
      return MainCategory.INCOME;
    } else if ((stringContainsItemFromList(this.note, needsKeywords))
        || (!this.variableSymbol.isEmpty() && Integer.parseInt(this.variableSymbol) == 45625608)) {
      return MainCategory.NEEDS;
    } else if (stringContainsItemFromList(this.transactionNote, savingsKeywords)
        || stringContainsItemFromList(this.note, savingsKeywords)) {
      return MainCategory.SAVINGS;
    } else if (stringContainsItemFromList(this.transactionNote, loansKeywords)) {
      return MainCategory.LOANS;
    } else if (stringContainsItemFromList(this.note, funKeywords)) {
      return MainCategory.FUN_WANTS_GIFTS;
    }

    return MainCategory.OTHERS;
  }

  private boolean stringContainsItemFromList(String input, String[] array) {
    input = input.toLowerCase();
    boolean stringIsNormalized = Normalizer.isNormalized(input, Normalizer.Form.NFKD);
    if (!stringIsNormalized) {
      input = Normalizer.normalize(input, Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", "");
    }

    return Arrays.stream(array).anyMatch(input::contains);
  }
}
