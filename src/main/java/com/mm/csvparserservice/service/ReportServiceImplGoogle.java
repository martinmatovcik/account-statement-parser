package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import com.mm.csvparserservice.dto.BalanceDto;
import com.mm.csvparserservice.dto.TransactionDto;
import com.mm.csvparserservice.entity.BalanceCategory;
import com.mm.csvparserservice.entity.TransactionMainCategory;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.Month;
import java.util.ArrayList;
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

  @Override
  public void generateReport(Month month) {
    generateDataSheet();
    generateReportSheetForGivenMonth(month);
  }

  private void generateReportSheetForGivenMonth(Month month) {

    List<List<Object>> data = List.of(List.of("Something"));
    String sheetName = "Report_" + month.name();

    insertDataToSheet(sheetName, data);
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

    data.add(0, initialBalance);
    data.add(1, finalBalance);
    data.add(2, needsSum);
    data.add(3, loansSum);
    data.add(4, funSum);
    data.add(5, savingsSum);
    data.add(6, incomeSum);
    data.add(7, othersSum);
    data.add(8, List.of("SUM", "=SUM(B3:B8)"));

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

    data.add(9, metaData);

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
