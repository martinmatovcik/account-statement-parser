package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignItemCommandDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Transaction;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionService {
  List<Transaction> getAllTransactions();

  List<Transaction> getAllTransactionsForMonth(Month month);

  Transaction getTransactionById(UUID id);

  Transaction persistTransaction(Transaction transactionToSave);

  Transaction updateTransactionById(UUID id, Transaction updatedTransaction);

  void deleteTransactionById(UUID id);

  BigDecimal sumAmountOfTransactionsForCategoryAndMonth(
          Category category, Month month);

  Transaction updateFieldsInTransactionById(UUID id, Map<Object, Object> fields);

  Transaction assignItemToTransactionById(AssignItemCommandDto assignItemCommandDto);
}
