package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.command.AssignCategoryItemCommandDto;
import com.mm.accountstatementparser.dto.result.TransactionProcessResultDto;
import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.CategoryItem;
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

  private final CategoryItemService categoryItemService;
  private final TransactionRepository transactionRepository;

  @Override
  public List<Transaction> getAll() {
    return transactionRepository.findAll();
  }

  @Override
  public Transaction getEntityById(UUID id) {
    return transactionRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Transaction with given ID does not exist"));
  }

  @Override
  public Transaction persistEntity(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  public TransactionProcessResultDto processTransaction(Transaction transaction) {
    Transaction persistedTransaction = assignItemOrUnassignedToTransactionAndPersist(transaction);

    if (persistedTransaction.getCategoryItem().getCode().equals("unassigned"))
      return new TransactionProcessResultDto(
          persistedTransaction.toDto(), parseTransactionNote(transaction.getTransactionNote()));
    else return new TransactionProcessResultDto(persistedTransaction.toDto());
  }

  @Override
  public Transaction updateEntity(Transaction updatedEntity) {
    return updateEntityById(updatedEntity.getId(), updatedEntity);
  }

  @Override
  public Transaction updateEntityById(UUID id, Transaction updatedTransaction) {
    Transaction transactionToUpdate = getEntityById(id);
    BeanUtils.copyProperties(updatedTransaction, transactionToUpdate, "id");
    return transactionRepository.save(transactionToUpdate);
  }

  @Override
  public Transaction updateEntityFieldsById(UUID id, Map<Object, Object> fields) {
    Transaction entityToUpdate = getEntityById(id);
    fields.forEach(
        (key, value) -> {
          Field field = ReflectionUtils.findField(Transaction.class, (String) key);
          Objects.requireNonNull(field).setAccessible(true);
        });
    return transactionRepository.save(entityToUpdate);
  }

  @Override
  public void deleteEntityById(UUID id) {
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
  public List<Transaction> assignTransactionsToCategoryItems(
      List<AssignCategoryItemCommandDto> assignCategoryItemCommandDtos) {
    List<Transaction> result = new ArrayList<>();

    for (AssignCategoryItemCommandDto assignCategoryItemCommandDto :
        assignCategoryItemCommandDtos) {

      CategoryItem categoryItem = null;
      String keyword = assignCategoryItemCommandDto.getKeyword();

      if (keyword != null && !keyword.isEmpty())
        categoryItem =
            categoryItemService.findCategoryItemByKeywords(List.of(keyword)).orElse(null);

      if (categoryItem == null) {
        categoryItem =
            categoryItemService.findCategoryItemByCode(
                assignCategoryItemCommandDto.getCategoryItemCode());
        categoryItem =
            categoryItemService.updateCategoryItemKeywords(categoryItem.getId(), keyword);
      }

      Transaction transactionToUpdate =
          getEntityById(assignCategoryItemCommandDto.getTransactionId());
      categoryItemService.updateCategoryItemRealAmountAndDifferenceWithTransaction(
          categoryItem, transactionToUpdate);
      transactionToUpdate.setCategoryItem(categoryItem);

      result.add(updateEntity(transactionToUpdate));
    }

    return result;
  }

  @Override
  public List<Transaction> reassignAllUnassignedTransactions() {
    return transactionRepository
        .findAllByCategoryItem(categoryItemService.findOrCreateCategoryItemUnassigned())
        .stream()
        .map(this::assignItemOrUnassignedToTransactionAndPersist)
        .filter(
            transaction ->
                transaction.getCategoryItem()
                    != categoryItemService.findCategoryItemByCode("unassigned"))
        .toList();
  }

  private Transaction assignItemOrUnassignedToTransactionAndPersist(Transaction transaction) {
    CategoryItem originalCategoryItem = transaction.getCategoryItem();

    List<String> transactionKeywords = parseTransactionNote(transaction.getTransactionNote());
    Optional<CategoryItem> matchingItem =
        categoryItemService.findCategoryItemByKeywords(transactionKeywords);

    if (originalCategoryItem != null
        && originalCategoryItem.getCode().equals("unassigned")
        && matchingItem.isEmpty())
      transaction.setCategoryItem(categoryItemService.findOrCreateCategoryItemUnassigned());
    else {
      matchingItem.ifPresent(transaction::setCategoryItem);
    }

    Transaction persistedTransaction = persistEntity(transaction);
    categoryItemService.updateCategoryItemRealAmountAndDifferenceWithTransaction(
        originalCategoryItem, persistedTransaction);
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
