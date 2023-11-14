package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import com.mm.csvparserservice.dto.BalanceDto;
import com.mm.csvparserservice.dto.ReportItemDto;
import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.entity.Balance;
import com.mm.csvparserservice.entity.BalanceCategory;
import com.mm.csvparserservice.entity.Transaction;
import com.mm.csvparserservice.entity.TransactionMainCategory;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImplGoogle implements ReportService {

  private static final String SPREADSHEET_ID = "1JSwxGLTXA8PSn6_dQrXEpPSsy6vEKIPPHT7sv62cOx8";
  //  private static final int DATA_SHEET_ID = 1096477276;
  private final TransactionService transactionService;
  private final BalanceService balanceService;
  private final ReportItemService reportItemService;

  @Override
  public void generateReport(Month month) {
    generateDataSheet(month);
    generateTemplate(month);
  }

  @Override
  public void generateTemplate(Month month) {
    generateSheetForGivenMonth(month, true);
    List<List<Object>> data = new LinkedList<>();

    double plannedIncome = 43270.00;

    //    Line 1
    data.add(
        List.of(
            "Stav účtu k prvému dňu:",
            dataCellName(month, "B1", false),
            "'+/-",
            "Plánované náklady na život / mes:",
            "=B24+B30"));
    //    Line 2
    data.add(
        List.of(
            "Stav účtu k poslednému dňu:",
            dataCellName(month, "B2", false),
            dataCellName(month, "B9", false),
            "Skutočné náklady na život / mes:",
            "=C24+C30"));
    //    Line 3
    data.add(List.of());
    //    Line 4
    data.add(
        List.of(
            "Plánované príjmy",
            "",
            "",
            "Plánované výdaje",
            expenses(true),
            "",
            "Plánovaný zostatok",
            balances(true)));
    //    Line 5
    data.add(List.of("Výplata", plannedIncome));
    //    Line 6
    data.add(List.of("Iný príjem", 0.00));
    //    Line 7
    data.add(List.of("Celkový príjem", "=SUM(B5:B6)"));
    //    Line 8
    data.add(List.of());
    //    Line 9
    data.add(
        List.of(
            "Skutočné príjmy",
            "",
            "",
            "Skutočné výdaje",
            expenses(false),
            "",
            "Skutočný zostatok",
            balances(false)));
    //    Line 10
    data.add(List.of("Výplata", 0.00));
    //    Line 11
    data.add(List.of("Iný príjem", 0.00));
    //    Line 12
    data.add(List.of("Celkový príjem", dataCellName(month, "B7", false)));
    //    Line 13
    data.add(List.of());
    //    Line 14
    data.add(
        List.of(
            "Rozdiel príjem",
            "=B12-B7",
            "",
            "Rozdiel výdaje",
            "=E9-E4",
            "",
            "Rozdiel zostatok",
            dataCellName(month, "B9", false)));
    //    Line 15
    data.add(List.of());

    //    SECTIONS
    for (TransactionMainCategory category : TransactionMainCategory.values()) {
      if (category != TransactionMainCategory.INCOME) data.addAll(generateSection(month, category));
    }

    insertDataToSheet(generateSheetNameForGivenMonth(month, true), data);
  }

  private List<List<Object>> generateSection(Month month, TransactionMainCategory category) {
    String sectionHeading;
    String dataSheetCellIndex;
    switch (category) {
      default -> {
        sectionHeading = "";
        dataSheetCellIndex = "";
      }
      case NEEDS -> {
        sectionHeading = "BÝVANIE, KOMUNIKÁCIA a INÉ POTREBY";
        dataSheetCellIndex = "B3";
      }
      case LOANS -> {
        sectionHeading = "PÔŽIČKY";
        dataSheetCellIndex = "B4";
      }
      case FUN_WANTS_GIFTS -> {
        sectionHeading = "RADOSTI, VOĽNÝ ČAS, ZÁBAVA, DARY";
        dataSheetCellIndex = "B5";
      }
      case SAVINGS -> {
        sectionHeading = "SPORENIE";
        dataSheetCellIndex = "B6";
      }
      case OTHERS -> {
        sectionHeading = "OSTATNÉ";
        dataSheetCellIndex = "B8";
      }
    }

    List<Object> sectionHeader =
        List.of("Položka", "Plánované náklady", "Skutočné náklady", "Rozdiel");

    List<List<Object>> section = new LinkedList<>();
    section.add(List.of(sectionHeading));
    section.add(sectionHeader);
    section.addAll(
        reportItemService.findReportItemsByCategory(category).stream()
            .map(ReportItemDto::toData)
            .toList());

    section.add(
        List.of(
            "Medzisúčet",
            reportItemService.sumPlannedAmountOfReportItemsForCategory(category),
            dataCellName(month, dataSheetCellIndex, true),
            reportItemService.sumDifferenceOfReportItemsForCategory(category)));
    section.add(List.of());

    return section;
  }

  private String dataCellName(Month month, String cellIndex, boolean negate) {
    String cell = generateSheetNameForGivenMonth(month, false) + "!" + cellIndex;
    return "=" + (negate ? "-(" + cell + ")" : cell);
  }

  private String expenses(boolean isPlanned) {
    String collumn = "C";
    if (isPlanned) collumn = "B";
    return "=" + collumn + "24+" + collumn + "30+" + collumn + "42+" + collumn + "52+" + collumn
        + "57";
  }

  private String balances(boolean isPlanned) {
    String balance = "=B1+";
    balance += (isPlanned) ? "B7-E4" : "B12-E9";
    return balance;
  }

  private void generateSheetForGivenMonth(Month month, boolean isReport) {

    List<List<Object>> data = List.of(List.of("Something"));
    String sheetName = generateSheetNameForGivenMonth(month, isReport);

    insertDataToSheet(sheetName, data);
  }

  private String generateSheetNameForGivenMonth(Month month, boolean isReport) {
    return (isReport ? "Report_" : "Data_") + month.name();
  }

  private void generateDataSheet(Month month) {
    List<BalanceDto> balanceDtos =
        balanceService.getAllBalancesForMonth(month).stream().map(Balance::toDto).toList();

    List<Object> initialBalance =
        List.of(
            "Initial balance",
            getBalanceAmountOrZero(BalanceCategory.INITIAL_BALANCE, balanceDtos));
    List<Object> finalBalance =
        List.of(
            "Final balance", getBalanceAmountOrZero(BalanceCategory.FINAL_BALANCE, balanceDtos));

    List<Object> needsSum = createSumForCategoryData(TransactionMainCategory.NEEDS, month);
    List<Object> loansSum = createSumForCategoryData(TransactionMainCategory.LOANS, month);
    List<Object> funSum = createSumForCategoryData(TransactionMainCategory.FUN_WANTS_GIFTS, month);
    List<Object> savingsSum = createSumForCategoryData(TransactionMainCategory.SAVINGS, month);
    List<Object> incomeSum = createSumForCategoryData(TransactionMainCategory.INCOME, month);
    List<Object> othersSum = createSumForCategoryData(TransactionMainCategory.OTHERS, month);

    List<List<Object>> data =
        transactionService.getAllTransactions().stream()
            .map(Transaction::toDto)
            .map(TransactionDto::toData)
            .collect(Collectors.toList());

    List<Object> metaData =
        List.of(
            "Transaction Id",
            "Fio Operation Id",
            "Date",
            "Amount",
            "Currency",
            "Recipient Account",
            "Recipient Account Name",
            "Bank Code",
            "Bank Name",
            "Constant Symbol",
            "Variable Symbol",
            "Specific Symbol",
            "Transaction Note",
            "Recipient Message",
            "Transaction Type",
            "Carried Out",
            "Transaction Specification",
            "Transaction Note",
            "BIC Code",
            "Fio Instruction Id",
            "Main Category");

    data.addFirst(metaData);
    data.addFirst(List.of("SUM", "=SUM(B3:B8)"));
    data.addFirst(othersSum);
    data.addFirst(incomeSum);
    data.addFirst(savingsSum);
    data.addFirst(funSum);
    data.addFirst(loansSum);
    data.addFirst(needsSum);
    data.addFirst(finalBalance);
    data.addFirst(initialBalance);

    insertDataToSheet(generateSheetNameForGivenMonth(month, false), data);
  }

  private void insertDataToSheet(String sheetName, List<List<Object>> data) {
    try {
      Sheets sheetsService = GoogleSheetsConfiguration.getSheetsService();
      sheetsService
          .spreadsheets()
          .values()
          .update(
              SPREADSHEET_ID,
              createSheetIfDontExist(sheetName, sheetsService),
              new ValueRange().setValues(data))
          .setValueInputOption("USER_ENTERED")
          .execute();
    } catch (IOException | GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  private BigDecimal getBalanceAmountOrZero(
      BalanceCategory balanceCategory, List<BalanceDto> balanceDtos) {

    if (balanceDtos.isEmpty()) return BigDecimal.ZERO;

    return balanceDtos.stream()
        .filter(balanceDto -> balanceDto.getBalanceCategory().equals(balanceCategory))
        .findFirst()
        .orElseThrow()
        .getAmount();
  }

  private String createSheetIfDontExist(String sheetName, Sheets sheetsService) throws IOException {

    Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();

    boolean sheetExists =
        (spreadsheet.getSheets().stream()
                .map(Sheet::getProperties)
                .map(SheetProperties::getTitle)
                .toList())
            .contains(sheetName);

    String range = sheetName + "!A1:Z100";

    if (!sheetExists) {
      List<Request> requests = new ArrayList<>();
      requests.add(
          new Request()
              .setAddSheet(
                  new AddSheetRequest().setProperties(new SheetProperties().setTitle(sheetName))));
      BatchUpdateSpreadsheetRequest body =
          new BatchUpdateSpreadsheetRequest().setRequests(requests);
      sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
    }

    return range;
  }

  private List<Object> createSumForCategoryData(
      TransactionMainCategory transactionMainCategory, Month month) {
    BigDecimal sumValue =
        transactionService.sumAmountOfTransactionsForCategoryAndMonth(
            transactionMainCategory, month);
    if (sumValue == null) sumValue = BigDecimal.ZERO;
    return List.of(transactionMainCategory.toString(), sumValue);
  }
}
