package com.mm.accountstatementparser.dto.result;

import com.mm.accountstatementparser.entity.Transaction;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class TransactionProcessResultDto {
  private Transaction savedTransaction;
  private List<String> transactionKeywords;

  public TransactionProcessResultDto(Transaction savedTransaction) {
    this.savedTransaction = savedTransaction;
  }
}
