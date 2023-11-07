package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.model.Transaction;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface TransactionService {
  List<TransactionDto> getTransactionDtoListFromCSV(String filePath);

  Transaction persistTransaction(Transaction transaction);

  List<TransactionDto> getAllTransactions();
}
