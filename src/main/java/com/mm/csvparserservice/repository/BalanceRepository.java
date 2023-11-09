package com.mm.csvparserservice.repository;

import com.mm.csvparserservice.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
  List<Balance> findAllByMonth(Month month);
}
