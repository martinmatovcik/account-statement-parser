package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignItemCommandDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.repository.TransactionRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final ItemService itemService;
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
  public Transaction updateTransactionById(UUID id, Transaction updatedTransaction) {
    Transaction transactionToUpdate = findByIdOrElseThrow(id);
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
          Category category, Month month) {
    return transactionRepository.getAllByCategory(category).stream()
        .filter(transaction -> transaction.getDate().getMonth() == month)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public Transaction updateFieldsInTransactionById(UUID id, Map<Object, Object> fields) {
    Transaction transactionToUpdate = findByIdOrElseThrow(id);
    fields.forEach(
        (key, value) -> {
          Field field = ReflectionUtils.findField(Transaction.class, (String) key);
          Objects.requireNonNull(field).setAccessible(true);
          if (key == "amount") value = BigDecimal.valueOf((double) value);
          ReflectionUtils.setField(field, transactionToUpdate, value);
        });
    return transactionRepository.save(transactionToUpdate);
  }

  @Override
  public Transaction assignItemToTransactionById(AssignItemCommandDto assignItemCommandDto) {
    Transaction transactionToUpdate = findByIdOrElseThrow(assignItemCommandDto.getTransactionId());
    transactionToUpdate.setItem(itemService.getItemByCode(assignItemCommandDto.getItemCode()));
    return null;
  }

  private Transaction findByIdOrElseThrow(UUID id) {
    return transactionRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }
}
