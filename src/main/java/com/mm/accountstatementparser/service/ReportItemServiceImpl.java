package com.mm.accountstatementparser.service;

import com.mm.accountstatementparser.dto.ReportItemDto;
import com.mm.accountstatementparser.entity.ReportItem;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import com.mm.accountstatementparser.repository.ReportItemRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportItemServiceImpl implements ReportItemService {
  private final ReportItemRepository reportItemRepository;

  @Override
  public List<ReportItem> getAllReportItems() {
    return reportItemRepository.findAll();
  }

  @Override
  public ReportItem getReportItemById(UUID id) {
    return reportItemRepository.findById(id).orElseThrow();
  }

  @Override
  public ReportItem persistReportItem(ReportItem reportItem) {
    return reportItemRepository.save(reportItem);
  }

  @Override
  public ReportItem updateReportItemById(UUID id, ReportItem updatedReportItem) {
    ReportItem reportItemToUpdate = reportItemRepository.getReferenceById(id);
    BeanUtils.copyProperties(updatedReportItem, reportItemToUpdate, "id");
    return reportItemRepository.save(reportItemToUpdate);
  }

  @Override
  public void deleteReportItemById(UUID id) {
    reportItemRepository.deleteById(id);
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
