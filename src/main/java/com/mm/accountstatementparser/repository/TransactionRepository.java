package com.mm.accountstatementparser.repository;

import com.mm.accountstatementparser.entity.Category;
import com.mm.accountstatementparser.entity.Transaction;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  List<Transaction> getAllByCategory(Category category);
}
