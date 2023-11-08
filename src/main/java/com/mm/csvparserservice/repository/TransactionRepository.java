package com.mm.csvparserservice.repository;

import com.mm.csvparserservice.model.MainCategory;
import com.mm.csvparserservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.mainCategory = :mainCategory")
    BigDecimal sumAmountOfTransactionsForCategory(MainCategory mainCategory);
}
