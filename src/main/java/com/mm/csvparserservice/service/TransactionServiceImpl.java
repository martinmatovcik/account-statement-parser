package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.entity.Transaction;
import com.mm.csvparserservice.entity.TransactionMainCategory;
import com.mm.csvparserservice.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  public Transaction persistTransaction(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  public List<TransactionDto> getAllTransactionDtos() {
    return transactionRepository.findAll().stream()
        .map(Transaction::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public BigDecimal sumAmountOfTransactionsForCategory(
      TransactionMainCategory transactionMainCategory) {
    return transactionRepository.sumAmountOfTransactionsForCategory(transactionMainCategory);
  }
}
