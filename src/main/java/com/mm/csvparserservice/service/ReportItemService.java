package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.ReportItemDto;
import com.mm.csvparserservice.entity.ReportItem;
import com.mm.csvparserservice.entity.TransactionMainCategory;

import java.math.BigDecimal;
import java.util.List;

public interface ReportItemService {
  ReportItem persistReportItem(ReportItem reportItem);

  ReportItem findReportItemByName(String name);
  List<ReportItemDto> findReportItemsByCategory(TransactionMainCategory category);
  BigDecimal sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory category);
  BigDecimal sumDifferenceOfReportItemsForCategory(TransactionMainCategory category);
}
