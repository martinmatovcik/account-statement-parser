package com.mm.csvparserservice.repository;

import com.mm.csvparserservice.entity.TransactionMainCategory;
import com.mm.csvparserservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionMainCategory = :transactionMainCategory")
    BigDecimal sumAmountOfTransactionsForCategory(TransactionMainCategory transactionMainCategory);
}
