package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignCategoryItemCommandDto;
import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Transaction;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

public interface TransactionService extends CrudEntityService<Transaction> {
  TransactionProcessResultDto processTransaction(Transaction transaction);

  BigDecimal sumAmountOfTransactionsForCategoryAndMonth(
          Category category, Month month);

  List<Transaction> assignTransactionsToCategoryItems(List<AssignCategoryItemCommandDto> assignCategoryItemCommandDtos);

  List<Transaction> reassignAllUnassignedTransactions();
}
