package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import com.mm.csvparserservice.model.MainCategory;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImplGoogle implements ReportService {

  private static final String SPREADSHEET_ID = "1JSwxGLTXA8PSn6_dQrXEpPSsy6vEKIPPHT7sv62cOx8";
  //  private static final int DATA_SHEET_ID = 1096477276;
  private final TransactionService transactionService;

  @Override
  public void generateReport() {
    try {
      Sheets sheetsService = GoogleSheetsConfiguration.getSheetsService();

      List<Object> needsSum = createSumForCategoryData(MainCategory.NEEDS);
      List<Object> loansSum = createSumForCategoryData(MainCategory.LOANS);
      List<Object> funSum = createSumForCategoryData(MainCategory.FUN_WANTS_GIFTS);
      List<Object> savingsSum = createSumForCategoryData(MainCategory.SAVINGS);
      List<Object> incomeSum = createSumForCategoryData(MainCategory.INCOME);
      List<Object> othersSum = createSumForCategoryData(MainCategory.OTHERS);

      List<List<Object>> data = transactionService.getAllTransactionDtosAsData();
      data.add(0, needsSum);
      data.add(1, loansSum);
      data.add(2, funSum);
      data.add(3, savingsSum);
      data.add(4, incomeSum);
      data.add(5, othersSum);
      data.add(6, List.of("SUM", "=SUM(B1:B6)"));

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

      data.add(7, metaData);

      sheetsService
          .spreadsheets()
          .values()
          .update(SPREADSHEET_ID, "Data!A1:Z100", new ValueRange().setValues(data))
          .setValueInputOption("USER_ENTERED")
          .execute();
    } catch (IOException | GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Object> createSumForCategoryData(MainCategory mainCategory) {
    BigDecimal sumValue = transactionService.sumAmountOfTransactionsForCategory(mainCategory);
    if (sumValue == null) sumValue = BigDecimal.ZERO;
    return List.of(mainCategory.toString(), sumValue);
  }
}
