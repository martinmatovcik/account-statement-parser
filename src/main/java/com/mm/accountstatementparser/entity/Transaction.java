package com.mm.accountstatementparser.entity;

import com.mm.accountstatementparser.dto.entityDto.TransactionDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.UUID;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends EntityParent {
  @Id @GeneratedValue private UUID transactionId;

  private LocalDate date;
  private BigDecimal amount;
  private Currency currency;
  private String variableSymbol;
  private String recipientMessage;
  private String transactionNote;

//  private Category category;

  @ManyToOne
  @JoinColumn(name = "item_code")
  @ToString.Exclude
  private Item item;

  @Override
  public TransactionDto toDto() {
    return TransactionDto.builder()
        .transactionId(this.transactionId)
        .date(this.date)
        .amount(this.amount)
        .currency(this.currency)
        .variableSymbol(this.variableSymbol)
        .recipientMessage(this.recipientMessage)
        .transactionNote(this.transactionNote)
//        .category(this.category)
        .build();
  }

//  public Category findMainCategory() {
//
//    String[] needsKeywords = {
//      "najom",
//      "lidl",
//      "kaufland",
//      "kaufla",
//      "operator ict",
//      "albert",
//      "lavor",
//      "wc",
//      "lekarna",
//      "dr.max",
//      "elektrina",
//      "pekarna",
//      "krusta",
//      "dm"
//    };
//
//    String[] savingsKeywords = {"portu", "investice"};
//    String[] loansKeywords = {"splatka", "pozicka"};
//
//    String[] funKeywords = {
//      "fruitisimo",
//      "paulhl.n.",
//      "primark",
//      "mcd",
//      "mcdonalds",
//      "forno",
//      "zalando",
//      "geco",
//      "datart",
//      "hm",
//      "hennesmauritz",
//      "pull & bear",
//      "kfc",
//      "kvetinarstvo",
//      "jysk",
//      "qerko",
//      "mad rabbit",
//      "hrad",
//      "dish belgicka",
//      "nyx",
//      "cd.cz",
//      "studentagency.cz",
//      "leoexpress",
//      "stradivarius"
//    };
//
//    if (Objects.requireNonNull(this.amount).compareTo(BigDecimal.ZERO) > 0) {
//      return Category.INCOME;
//    } else if ((stringContainsItemFromList(this.transactionNote, needsKeywords))
//        || (!this.variableSymbol.isEmpty() && Integer.parseInt(this.variableSymbol) == 45625608)) {
//      return Category.NEEDS;
//    } else if (stringContainsItemFromList(this.transactionNote, savingsKeywords)
//        || stringContainsItemFromList(this.transactionNote, savingsKeywords)) {
//      return Category.SAVINGS;
//    } else if (stringContainsItemFromList(this.transactionNote, loansKeywords)) {
//      return Category.LOANS;
//    } else if (stringContainsItemFromList(this.transactionNote, funKeywords)) {
//      return Category.FUN_WANTS_GIFTS;
//    }
//
//    return Category.OTHERS;
//  }

}
