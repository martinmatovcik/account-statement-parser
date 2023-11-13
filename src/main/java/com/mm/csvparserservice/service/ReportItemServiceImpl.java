package com.mm.csvparserservice.service;

import com.mm.csvparserservice.dto.ReportItemDto;
import com.mm.csvparserservice.entity.ReportItem;
import com.mm.csvparserservice.entity.TransactionMainCategory;
import com.mm.csvparserservice.repository.ReportItemRepository;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportItemServiceImpl implements ReportItemService {
  private final ReportItemRepository reportItemRepository;

  @Override
  public ReportItem persistReportItem(ReportItem reportItem) {
    return reportItemRepository.save(reportItem);
  }

  @Override
  public ReportItem findReportItemByName(String name) {
    return reportItemRepository.findByName(name).orElseThrow();
  }

  @Override
  public List<ReportItemDto> findReportItemsByCategory(TransactionMainCategory category) {
    return reportItemRepository.findAllByReportItemCategory(category).stream()
        .map(ReportItem::toDto)
        .toList();
  }

  @Override
  public BigDecimal sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory category) {
    return reportItemRepository.sumPlannedAmountOfReportItemsForCategory(category);
  }

  @Override
  public BigDecimal sumDifferenceOfReportItemsForCategory(TransactionMainCategory category) {
    return reportItemRepository.sumDifferenceOfReportItemsForCategory(category);
  }


}
