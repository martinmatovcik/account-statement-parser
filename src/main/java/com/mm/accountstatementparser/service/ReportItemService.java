package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.ReportItemDto;
import com.mm.accountstatementparser.entity.ReportItem;
import com.mm.accountstatementparser.entity.TransactionMainCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ReportItemService {
  List<ReportItem> getAllReportItems();

  ReportItem getReportItemById(UUID id);

  ReportItem persistReportItem(ReportItem reportItem);

  ReportItem updateReportItemById(UUID id, ReportItem entity);

  void deleteReportItemById(UUID id);

  List<ReportItemDto> findReportItemsByCategory(TransactionMainCategory category);

  BigDecimal sumPlannedAmountOfReportItems();

  BigDecimal sumRealAmountOfReportItems();

  BigDecimal sumDifferenceOfReportItems();

  BigDecimal sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory category);

  BigDecimal sumRealAmountOfReportItemsForCategory(TransactionMainCategory category);

  BigDecimal sumDifferenceOfReportItemsForCategory(TransactionMainCategory category);

  void createSampleReportItemsWhenNoExisting();

  BigDecimal sumLivingExpenses(boolean isPlanned);
}
