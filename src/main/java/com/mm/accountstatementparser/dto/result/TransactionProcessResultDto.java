package com.mm.accountstatementparser.dto.result;

import com.mm.accountstatementparser.dto.entityDto.TransactionDto;
import com.mm.accountstatementparser.entity.Transaction;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionProcessResultDto {
  private TransactionDto savedTransactionDto;
  private List<String> transactionKeywords;

  public TransactionProcessResultDto(TransactionDto savedTransactionDto) {
    this.savedTransactionDto = savedTransactionDto;
  }
}
