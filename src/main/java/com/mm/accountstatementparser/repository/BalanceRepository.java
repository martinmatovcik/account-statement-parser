package com.mm.accountstatementparser.repository;

import com.mm.accountstatementparser.entity.Balance;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, UUID> {
  List<Balance> findAllByMonth(Month month);
}
