package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import com.mm.accountstatementparser.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }

  @Override
  public List<Transaction> getAllTransactionsForMonth(Month month) {
    return transactionRepository.findAll().stream()
        .filter(transaction -> transaction.getDate().getMonth() == month)
        .toList();
  }

  @Override
  public Transaction getTransactionById(UUID id) {
    return transactionRepository.findById(id).orElseThrow();
  }

  @Override
  @Transactional
  public Transaction persistTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  @Transactional
  public Transaction updateTransaction(UUID id, Transaction updatedTransaction) {
    Transaction transactionToUpdate = transactionRepository.getReferenceById(id);
    BeanUtils.copyProperties(updatedTransaction, transactionToUpdate, "transactionId");
    return transactionRepository.save(transactionToUpdate);
  }

  @Override
  @Transactional
  public void deleteTransactionById(UUID id) {
    transactionRepository.deleteById(id);
  }

  @Override
  public BigDecimal sumAmountOfTransactionsForCategoryAndMonth(
      TransactionMainCategory transactionMainCategory, Month month) {
    return transactionRepository.getAllByTransactionMainCategory(transactionMainCategory).stream()
        .filter(transaction -> transaction.getDate().getMonth() == month)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
