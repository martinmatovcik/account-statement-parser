package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignItemCommandDto;
import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Item;
import com.mm.accountstatementparser.entity.Transaction;
import com.mm.accountstatementparser.repository.TransactionRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.Month;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
    return transactionRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public Transaction persistTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  public TransactionProcessResultDto processTransaction(Transaction transaction) {
    Transaction persistedTransaction = assignItemOrUnassignedToTransactionAndPersist(transaction);

    if (persistedTransaction.getItem().getCode().equals("unassigned"))
      return new TransactionProcessResultDto(
          persistedTransaction.toDto(), parseTransactionNote(transaction.getTransactionNote()));
    else return new TransactionProcessResultDto(persistedTransaction.toDto());
  }

  @Override
  public Transaction updateTransactionById(UUID id, Transaction updatedTransaction) {
    Transaction transactionToUpdate = getTransactionById(id);
    BeanUtils.copyProperties(updatedTransaction, transactionToUpdate, "id");
    return transactionRepository.save(transactionToUpdate);
  }

  @Override
  public void deleteTransactionById(UUID id) {
    transactionRepository.deleteById(id);
  }

  @Override
  public BigDecimal sumAmountOfTransactionsForCategoryAndMonth(Category category, Month month) {
    return BigDecimal.valueOf(4);
    // todo: 4
    //    return transactionRepository.getAllByCategory(category).stream()
    //        .filter(transaction -> transaction.getDate().getMonth() == month)
    //        .map(Transaction::getAmount)
    //        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public Transaction updateFieldsInTransactionById(UUID id, Map<Object, Object> fields) {
    Transaction transactionToUpdate = getTransactionById(id);
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
  public List<Transaction> assignTransactionToItemById(
      List<AssignItemCommandDto> assignItemCommandDtos) {
    List<Transaction> result = new ArrayList<>();

    for (AssignItemCommandDto assignItemCommandDto : assignItemCommandDtos) {
      Item item = null;
      String keyword = assignItemCommandDto.getKeyword();
      if (keyword != null && !keyword.isEmpty())
        item = itemService.findItemByKeywords(List.of(keyword)).orElse(null);
      if (item == null) {
        item = itemService.findItemByCode(assignItemCommandDto.getItemCode());
        item = itemService.updateKeywords(item.getId(), keyword);
      }

      UUID id = assignItemCommandDto.getTransactionId();
      Transaction transactionToUpdate = getTransactionById(id);
      transactionToUpdate.setItem(item);

      Transaction updatedTransaction = updateTransactionById(id, transactionToUpdate);

      itemService.updateItemRealAmountAndDifferenceWithTransaction(true, updatedTransaction);
      result.add(updatedTransaction);
    }

    return result;
  }

  @Override
  public List<Transaction> reassignAllUnassignedTransactionsToItems() {
    return transactionRepository.findAllByItem(itemService.findOrCreateItemUnassigned()).stream()
        .map(this::assignItemOrUnassignedToTransactionAndPersist)
        .filter(transaction -> transaction.getItem() != itemService.findItemByCode("unassigned"))
        .toList();
  }

  private Transaction assignItemOrUnassignedToTransactionAndPersist(Transaction transaction) {
    boolean isOriginalTransactionItemUnassigned =
        transaction.getItem() != null && transaction.getItem().getCode().equals("unassigned");

    List<String> transactionKeywords = parseTransactionNote(transaction.getTransactionNote());
    Optional<Item> matchingItem = itemService.findItemByKeywords(transactionKeywords);

    if (!isOriginalTransactionItemUnassigned && matchingItem.isEmpty())
      transaction.setItem(itemService.findOrCreateItemUnassigned());
    else {
      matchingItem.ifPresent(transaction::setItem);
    }

    Transaction persistedTransaction = persistTransaction(transaction);
    itemService.updateItemRealAmountAndDifferenceWithTransaction(
        isOriginalTransactionItemUnassigned, persistedTransaction);
    return persistedTransaction;
  }

  private List<String> parseTransactionNote(String transactionNote) {
    String prefix = "Nákup: ";

    if (transactionNote.contains(prefix)) transactionNote = transactionNote.replace(prefix, "");

    transactionNote = transactionNote.toLowerCase();

    if (!Normalizer.isNormalized(transactionNote, Normalizer.Form.NFKD))
      transactionNote =
          Normalizer.normalize(transactionNote, Normalizer.Form.NFKD)
              .replaceAll("[^\\p{ASCII}]", "");

    return List.of(transactionNote.split(",")[0].split(" "));
  }
}
