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
  public BigDecimal sumPlannedAmountOfReportItems() {
    return reportItemRepository.sumPlannedAmountOfReportItems();
  }

  @Override
  public BigDecimal sumRealAmountOfReportItems() {
    return reportItemRepository.sumRealAmountOfReportItems();
  }

  @Override
  public BigDecimal sumDifferenceOfReportItems() {
    return reportItemRepository.sumDifferenceOfReportItems();
  }

  @Override
  public BigDecimal sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory category) {
    return reportItemRepository.sumPlannedAmountOfReportItemsForCategory(category);
  }

  @Override
  public BigDecimal sumRealAmountOfReportItemsForCategory(TransactionMainCategory category) {
    return reportItemRepository.sumRealAmountOfReportItemsForCategory(category);
  }

  @Override
  public BigDecimal sumDifferenceOfReportItemsForCategory(TransactionMainCategory category) {
    return reportItemRepository.sumDifferenceOfReportItemsForCategory(category);
  }

  @Override
  public void createSampleReportItemsWhenNoExisting() {
    if (getAllReportItems().isEmpty()) {
      List<ReportItem> reportItems =
          List.of(
              new ReportItem("Nájom", "rent", BigDecimal.valueOf(17300.00), TransactionMainCategory.NEEDS),
              new ReportItem(
                  "Elektrina", "energies", BigDecimal.valueOf(1000.00), TransactionMainCategory.NEEDS),
              new ReportItem("Internet", "internet", BigDecimal.valueOf(300.00), TransactionMainCategory.NEEDS),
              new ReportItem("Telefóny", "phones", BigDecimal.valueOf(960.00), TransactionMainCategory.NEEDS),
              new ReportItem("Lítačky", "mhd", BigDecimal.valueOf(680.00), TransactionMainCategory.NEEDS),
              new ReportItem("Jedlo", "eating", BigDecimal.valueOf(10000.00), TransactionMainCategory.NEEDS),
              new ReportItem(
                  "Greenfox - Mišovci", "greenfox-loan", BigDecimal.valueOf(0.00), TransactionMainCategory.LOANS),
              new ReportItem(
                  "Bývanie - rodičia", "living-loan", BigDecimal.valueOf(2500.00), TransactionMainCategory.LOANS),
              new ReportItem(
                  "Oblečenie", "clother", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Netflix", "netflix", BigDecimal.valueOf(120.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Spotify", "spotify", BigDecimal.valueOf(60.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Kultúra", "culture", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Rande", "date", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Eating out", "eating-out", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Cestovanie", "traveling",
                      BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem(
                  "Charita", "charity", BigDecimal.valueOf(0.00), TransactionMainCategory.FUN_WANTS_GIFTS),
              new ReportItem("Dôchodok", "pension", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem(
                  "Krátkodobé", "short-term", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem(
                  "Finančná rezerva", "reserve", BigDecimal.valueOf(0.00), TransactionMainCategory.SAVINGS),
              new ReportItem("Neznáme", "other", BigDecimal.valueOf(0.00), TransactionMainCategory.OTHERS));

      for (ReportItem reportItem : reportItems) {
        persistReportItem(reportItem);
      }
    }
  }

  @Override
  public BigDecimal sumLivingExpenses(boolean isPlanned) {
    return isPlanned
        ? sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory.NEEDS)
            .add(sumPlannedAmountOfReportItemsForCategory(TransactionMainCategory.LOANS))
        : sumRealAmountOfReportItemsForCategory(TransactionMainCategory.NEEDS)
            .add(sumRealAmountOfReportItemsForCategory(TransactionMainCategory.LOANS));
  }
}
