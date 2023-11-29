package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignCategoryItemCommandDto;
import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Transaction;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionService extends CrudEntityService<Transaction> {
  TransactionProcessResultDto processTransaction(Transaction transaction);

  BigDecimal sumAmountOfTransactionsForCategoryAndMonth(
          Category category, Month month);

  Transaction updateFieldsInTransactionById(UUID id, Map<Object, Object> fields);

  List<Transaction> assignTransactionsToCategoryItemsById(List<AssignCategoryItemCommandDto> assignCategoryItemCommandDtos);

  List<Transaction> reassignAllUnassignedTransactions();
}
