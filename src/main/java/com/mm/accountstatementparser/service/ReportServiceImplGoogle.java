package com.mm.accountstatementparser.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.accountstatementparser.configuration.GoogleSheetsConfiguration;
import com.mm.accountstatementparser.dto.entityDto.BalanceDto;
import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.entity.Balance;
import com.mm.accountstatementparser.entity.BalanceCategory;
import com.mm.accountstatementparser.entity.Category;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImplGoogle implements ReportService {

  private static final String SPREADSHEET_ID = "1JSwxGLTXA8PSn6_dQrXEpPSsy6vEKIPPHT7sv62cOx8";
  //  private static final int DATA_SHEET_ID = 1096477276;
  private final TransactionService transactionService;
  private final BalanceService balanceService;
  private final CategoryItemService categoryItemService;
  private final CategoryService categoryService;

  @Override
  public void generateReport(Month month) {
    generateDataSheet(month);
    generateTemplate(month);
  }

  private void generateDataSheet(Month month) {
//    List<Object> initialBalance = getBalanceDataLine(BalanceCategory.INITIAL_BALANCE, month);
//    List<Object> finalBalance = getBalanceDataLine(BalanceCategory.FINAL_BALANCE, month);
//    List<Object> needsSum = getSumForCategoryDataLine(Category.NEEDS, month);
//    List<Object> loansSum = getSumForCategoryDataLine(Category.LOANS, month);
//    List<Object> funSum = getSumForCategoryDataLine(Category.FUN_WANTS_GIFTS, month);
//    List<Object> savingsSum = getSumForCategoryDataLine(Category.SAVINGS, month);
//    List<Object> incomeSum = getSumForCategoryDataLine(Category.INCOME, month);
//    List<Object> othersSum = getSumForCategoryDataLine(Category.OTHERS, month);
    List<Object> initialBalance = getBalanceDataLine(BalanceCategory.INITIAL_BALANCE, month);
    List<Object> finalBalance = getBalanceDataLine(BalanceCategory.FINAL_BALANCE, month);
    List<Object> needsSum = getSumForCategoryDataLine(categoryService.findCategoryByCode("NEEDS"), month);
    List<Object> loansSum = getSumForCategoryDataLine(categoryService.findCategoryByCode("LOANS"), month);
    List<Object> funSum = getSumForCategoryDataLine(categoryService.findCategoryByCode("FUN"), month);
    List<Object> savingsSum = getSumForCategoryDataLine(categoryService.findCategoryByCode("SAVINGS"), month);
    List<Object> incomeSum = getSumForCategoryDataLine(categoryService.findCategoryByCode("INCOME"), month);
    List<Object> othersSum = getSumForCategoryDataLine(categoryService.findCategoryByCode("OTHERS"), month);
    List<Object> metaData =
        List.of(
            "Transaction Id",
            "Date",
            "Amount",
            "Currency",
            "Variable Symbol",
            "Recipient Message",
            "Transaction Note",
            "Main Category");

    List<List<Object>> data =
        new LinkedList<>(
            List.of(
                initialBalance,
                finalBalance,
                needsSum,
                loansSum,
                funSum,
                savingsSum,
                incomeSum,
                othersSum,
                List.of("SUM", "=SUM(B3:B8)"),
                metaData));

//    data.addAll(
//        transactionService.getAllTransactionsForMonth(month).stream()
//            .map(t -> t.toDto().toData())
//            .toList());

    insertDataToSheet(generateSheetNameForGivenMonth(month, false), data);
  }

  private void generateTemplate(Month month) {
    List<List<Object>> data = new LinkedList<>();

    double plannedIncome = 43270.00;
    String finalBalanceDataCell = dataCellName(month, "B2", false);

    //    Line 1
    data.add(
        List.of(
            "Stav účtu k prvému dňu:",
            dataCellName(month, "B1", false),
            "'+/-",
            "Plánované náklady na život / mes:",
            categoryItemService.sumLivingExpenses(true)));
    //    Line 2
    data.add(
        List.of(
            "Stav účtu k poslednému dňu:",
            finalBalanceDataCell,
            dataCellName(month, "B9", false),
            "Skutočné náklady na život / mes:",
            dataCellName(month, "B3", true) + "+" + dataCellName(month, "B4", true).substring(1)));
    //    Line 3
    data.add(List.of());
    //    Line 4
    data.add(
        List.of(
            "Plánované príjmy",
            "",
            "",
            "Plánované výdaje",
            categoryItemService.sumPlannedAmountOfCategoryItems(),
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
            realExpenses(month),
            "",
            "Skutočný zostatok",
            finalBalanceDataCell));
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
    for (Category category : categoryService.getAll()) {
      if (!Objects.equals(category.getCode(), "INCOME")) data.addAll(generateSection(month, category));
    }

    insertDataToSheet(generateSheetNameForGivenMonth(month, true), data);
  }

  private List<List<Object>> generateSection(Month month, Category category) {
    String sectionHeader = "Section header";
    String dataSheetCellIndex = "B2";


    List<Object> sectionMetaData =
        List.of("Položka", "Plánované náklady", "Skutočné náklady", "Rozdiel");

    List<List<Object>> section = new LinkedList<>();
    section.add(List.of(sectionHeader));
    section.add(sectionMetaData);
    section.addAll(
        categoryItemService.findCategoryItemsByCategory(category).stream()
            .map(CategoryItemDto::toData)
            .toList());

    section.add(
        List.of(
            "Medzisúčet",
            categoryItemService.sumPlannedAmountOfCategoryItemsForCategory(category),
            dataCellName(month, dataSheetCellIndex, true),
            categoryItemService.sumDifferenceOfCategoryItemsForCategory(category)));
    section.add(List.of());

    return section;
  }

  private String dataCellName(Month month, String cellIndex, boolean negate) {
    String cell = generateSheetNameForGivenMonth(month, false) + "!" + cellIndex;
    return "=" + (negate ? "-(" + cell + ")" : cell);
  }

  private String realExpenses(Month month) {
    String cell = generateSheetNameForGivenMonth(month, false) + "!B";
    return "=-(" + cell + "3+" + cell + "4+" + cell + "5+" + cell + "6+" + cell + "8)";
  }

  private String balances(boolean isPlanned) {
    String balance = "=B1+";
    balance += (isPlanned) ? "B7-E4" : "B12-E9";
    return balance;
  }

  private String generateSheetNameForGivenMonth(Month month, boolean is) {
    return (is ? "_" : "Data_") + month.name();
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

  private String createSheetIfDontExist(String sheetName, Sheets sheetsService) throws IOException {
    Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();

    boolean sheetExists =
        (spreadsheet.getSheets().stream().map(sheet -> sheet.getProperties().getTitle()).toList())
            .contains(sheetName);

    String range = sheetName + "!1:1000";

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

  private List<Object> getBalanceDataLine(BalanceCategory balanceCategory, Month month) {
    List<BalanceDto> balanceDtos =
        balanceService.getAllBalancesForMonth(month).stream().map(Balance::toDto).toList();

    String balanceType = (balanceCategory == BalanceCategory.FINAL_BALANCE ? "Final" : "Initial");

    BigDecimal balanceAmount =
        balanceDtos.isEmpty()
            ? BigDecimal.ZERO
            : balanceDtos.stream()
                .filter(balanceDto -> balanceDto.getBalanceCategory().equals(balanceCategory))
                .findFirst()
                .orElseThrow()
                .getAmount();

    return List.of(balanceType + " balance", balanceAmount);
  }

  private List<Object> getSumForCategoryDataLine(
          Category category, Month month) {
    BigDecimal sumValue =
        transactionService.sumAmountOfTransactionsForCategoryAndMonth(
                category, month);
    if (sumValue == null) sumValue = BigDecimal.ZERO;
    return List.of(category.toString(), sumValue);
  }
}
