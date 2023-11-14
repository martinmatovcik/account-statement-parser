package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
  List<Transaction> getAllTransactions();
  List<Transaction> getAllTransactionsForMonth(Month month);
  Transaction getTransactionById(UUID id);
  Transaction persistTransaction(Transaction transactionToSave);
  Transaction updateTransaction(UUID id, Transaction updatedTransaction);
  void deleteTransactionById(UUID id);
  BigDecimal sumAmountOfTransactionsForCategoryAndMonth(TransactionMainCategory transactionMainCategory, Month month);

}
