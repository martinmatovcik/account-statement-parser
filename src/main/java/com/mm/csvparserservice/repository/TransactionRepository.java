package com.mm.csvparserservice.repository;

import com.mm.csvparserservice.entity.Transaction;
import com.mm.csvparserservice.entity.TransactionMainCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> getAllByTransactionMainCategory(
      TransactionMainCategory transactionMainCategory);
}
