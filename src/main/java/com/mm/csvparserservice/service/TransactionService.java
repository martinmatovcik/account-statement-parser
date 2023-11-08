package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.model.Transaction;

import java.util.List;

public interface TransactionService {
  List<TransactionDto> parseCSV(String filePath);

  Transaction persistTransaction(Transaction transaction);

  List<TransactionDto> getAllTransactionDtos();

  List<List<Object>> getAllTransactionDtosAsData();
}
