package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.ReportItemDto;
import com.mm.csvparserservice.entity.EntityParent;
import com.mm.csvparserservice.entity.ReportItem;
import com.mm.csvparserservice.entity.TransactionMainCategory;

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
  BigDecimal sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory category);
  BigDecimal sumDifferenceOfReportItemsForCategory(TransactionMainCategory category);
}
