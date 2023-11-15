package com.mm.accountstatementparser.repository;

import com.mm.accountstatementparser.entity.ReportItem;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, UUID> {
  List<ReportItem> findAllByReportItemCategory(TransactionMainCategory category);

  @Query("SELECT SUM(r.plannedAmount) FROM ReportItem r")
  BigDecimal sumPlannedAmountOfReportItems();

  @Query("SELECT SUM(r.realAmount) FROM ReportItem r")
  BigDecimal sumRealAmountOfReportItems();

  @Query("SELECT SUM(r.difference) FROM ReportItem r")
  BigDecimal sumDifferenceOfReportItems();

  @Query("SELECT SUM(r.plannedAmount) FROM ReportItem r WHERE r.reportItemCategory = :category")
  BigDecimal sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory category);

  @Query("SELECT SUM(r.realAmount) FROM ReportItem r WHERE r.reportItemCategory = :category")
  BigDecimal sumRealAmountOfReportItemsForCategory(TransactionMainCategory category);

  @Query("SELECT SUM(r.difference) FROM ReportItem r WHERE r.reportItemCategory = :category")
  BigDecimal sumDifferenceOfReportItemsForCategory(TransactionMainCategory category);
}
