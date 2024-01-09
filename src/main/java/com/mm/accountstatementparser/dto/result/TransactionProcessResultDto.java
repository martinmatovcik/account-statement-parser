package com.mm.accountstatementparser.dto.result;

import com.mm.accountstatementparser.dto.entityDto.TransactionDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionProcessResultDto extends TransactionDto {
  private List<String> transactionKeywords;

  public TransactionProcessResultDto(TransactionDto transactionDto) {
    super(
        transactionDto.getId(),
        transactionDto.getDate(),
        transactionDto.getAmount(),
        transactionDto.getCurrency(),
        transactionDto.getVariableSymbol(),
        transactionDto.getRecipientMessage(),
        transactionDto.getTransactionNote(),
        transactionDto.getCategoryItemId());
  }
  public TransactionProcessResultDto(TransactionDto transactionDto, List<String> transactionKeywords) {
    super(
        transactionDto.getId(),
        transactionDto.getDate(),
        transactionDto.getAmount(),
        transactionDto.getCurrency(),
        transactionDto.getVariableSymbol(),
        transactionDto.getRecipientMessage(),
        transactionDto.getTransactionNote(),
        transactionDto.getCategoryItemId());
    this.transactionKeywords = transactionKeywords;
  }
}
