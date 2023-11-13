package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import com.mm.csvparserservice.dto.BalanceDto;
import com.mm.csvparserservice.dto.ReportItemDto;
import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.entity.BalanceCategory;
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

  private static final String DATA_SHEET_NAME =
      "Data"; // todo: method to generate it, base on month given

  @Override
  public void generateReport(Month month) {
    generateDataSheet();
    generateReportSheetForGivenMonth(month);
    generateTemplate(month);
  }

  @Override
  public void generateTemplate(Month month) {
    List<List<Object>> data = new LinkedList<>();

    double plannedIncome = 43270.00;

    //    Line 1
    data.add(
        List.of(
            "Stav účtu k prvému dňu:",
            cellName(DATA_SHEET_NAME, "B1"),
            "+/-",
            "Plánované náklady na život / mes:",
            "=B24+B30"));
    //    Line 2
    data.add(
        List.of(
            "Stav účtu k poslednému dňu:",
            cellName(DATA_SHEET_NAME, "B2"),
            cellName(DATA_SHEET_NAME, "B9"),
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
    data.add(List.of("=SUM(B5:B6)"));
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
    data.add(List.of(cellName(DATA_SHEET_NAME, "B7")));
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
            cellName(DATA_SHEET_NAME, "B9")));
    //    Line 15
    data.add(List.of());

    //    SECTIONS
    for (TransactionMainCategory category : TransactionMainCategory.values()) {
      if (category != TransactionMainCategory.INCOME) data.addAll(generateSection(category));
    }

    insertDataToSheet(generateSheetNameForGivenMonth(month), data);
  }

  private List<List<Object>> generateSection(TransactionMainCategory category) {
    String sectionHeading;
    String cell;
    switch (category) {
      default -> {
        sectionHeading = "";
        cell = "";
      }
      case NEEDS -> {
        sectionHeading = "BÝVANIE, KOMUNIKÁCIA a INÉ POTREBY";
        cell = "B3";
      }
      case LOANS -> {
        sectionHeading = "PÔŽIČKY";
        cell = "B4";
      }
      case FUN_WANTS_GIFTS -> {
        sectionHeading = "RADOSTI, VOĽNÝ ČAS, ZÁBAVA, DARY";
        cell = "B5";
      }
      case SAVINGS -> {
        sectionHeading = "SPORENIE";
        cell = "B6";
      }
      case OTHERS -> {
        sectionHeading = "OSTATNÉ";
        cell = "B8";
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

    var x = reportItemService.sumPlannedAmountOfReportItemsForCategory(category);
    var y = reportItemService.sumDifferenceOfReportItemsForCategory(category);
    var z = cellName(DATA_SHEET_NAME, cell);

    section.add(
        List.of(
            "Medzisúčet",
            reportItemService.sumPlannedAmountOfReportItemsForCategory(category),
            cellName(DATA_SHEET_NAME, cell),
            reportItemService.sumDifferenceOfReportItemsForCategory(category)));
    section.add(List.of());

    return section;
  }

  private String cellName(String dataSheetName, String cell) {
    return "=" + dataSheetName + "!" + cell;
  }

  private String expenses(boolean isPlanned) {
    String collumn = "C";
    if (isPlanned) collumn = "B";
    return "=" + collumn + "24+" + collumn + "30" + collumn + "42" + collumn + "52" + collumn
        + "57";
  }

  private String balances(boolean isPlanned) {
    String balance = "=B1+";
    balance += (isPlanned) ? "B7-E4" : "B12-E9";
    return balance;
  }

  private void generateReportSheetForGivenMonth(Month month) {

    List<List<Object>> data = List.of(List.of("Something"));
    String sheetName = generateSheetNameForGivenMonth(month);

    insertDataToSheet(sheetName, data);
  }

  private String generateSheetNameForGivenMonth(Month month) {
    return "Report_" + month.name();
  }

  private void generateDataSheet() {
    List<BalanceDto> balanceDtos = balanceService.getAllBalanceDtosForMonth(Month.OCTOBER);

    List<Object> initialBalance =
        List.of(
            "Initial balance",
            getBalanceAmountOrZero(BalanceCategory.INITIAL_BALANCE, balanceDtos));
    List<Object> finalBalance =
        List.of(
            "Final balance", getBalanceAmountOrZero(BalanceCategory.FINAL_BALANCE, balanceDtos));

    List<Object> needsSum = createSumForCategoryData(TransactionMainCategory.NEEDS);
    List<Object> loansSum = createSumForCategoryData(TransactionMainCategory.LOANS);
    List<Object> funSum = createSumForCategoryData(TransactionMainCategory.FUN_WANTS_GIFTS);
    List<Object> savingsSum = createSumForCategoryData(TransactionMainCategory.SAVINGS);
    List<Object> incomeSum = createSumForCategoryData(TransactionMainCategory.INCOME);
    List<Object> othersSum = createSumForCategoryData(TransactionMainCategory.OTHERS);

    List<List<Object>> data =
        transactionService.getAllTransactionDtos().stream()
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

    insertDataToSheet("Data", data);
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

  private List<Object> createSumForCategoryData(TransactionMainCategory transactionMainCategory) {
    BigDecimal sumValue =
        transactionService.sumAmountOfTransactionsForCategory(transactionMainCategory);
    if (sumValue == null) sumValue = BigDecimal.ZERO;
    return List.of(transactionMainCategory.toString(), sumValue);
  }
}
