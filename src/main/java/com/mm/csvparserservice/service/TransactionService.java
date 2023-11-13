package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.entity.Transaction;
import com.mm.csvparserservice.entity.TransactionMainCategory;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

public interface TransactionService {
  Transaction persistTransaction(Transaction transaction);

  List<TransactionDto> getAllTransactionDtos();

  BigDecimal sumAmountOfTransactionsForCategoryAndMonth(TransactionMainCategory transactionMainCategory, Month month);
}
