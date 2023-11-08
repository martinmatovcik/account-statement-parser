package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImplGoogle implements ReportService {

  private final TransactionService transactionService;
  private static final String SPREADSHEET_ID = "1JSwxGLTXA8PSn6_dQrXEpPSsy6vEKIPPHT7sv62cOx8";
  private static final int DATA_SHEET_ID = 1096477276;

  @Override
  public void generateReport(HttpServletResponse httpServletResponse) {
    try {
      Sheets sheetsService = GoogleSheetsConfiguration.getSheetsService();
      insertHeaderRow(sheetsService);
      insertOtherRows(sheetsService);
    } catch (IOException | GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  private void insertData(Sheets sheetsService, String dataRange, ValueRange data)
      throws IOException {

    deleteCellsDataIfExist(sheetsService, dataRange);

    sheetsService
        .spreadsheets()
        .values()
        .append(SPREADSHEET_ID, dataRange, data)
        .setValueInputOption("USER_ENTERED")
        .setIncludeValuesInResponse(true)
        .execute();
  }

  private void insertOtherRows(Sheets sheetsService) throws IOException {
    List<List<Object>> transactionDtosAsData = transactionService.getAllTransactionDtosAsData();

    System.out.println(transactionDtosAsData.get(1).get(2).toString());

    ValueRange data =
        new ValueRange().setValues(transactionDtosAsData);
    String dataRange = generateCellsRange(null, "A2", "Z100");

    insertData(sheetsService, dataRange, data);
  }

  private void insertHeaderRow(Sheets sheetsService) throws IOException {
    ValueRange data =
        new ValueRange()
            .setValues(
                List.of(
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
                        "BIC Code",
                        "Fio Instruction Id")));

    String dataRange = generateCellsRange("Data", "A1", null);

    insertData(sheetsService, dataRange, data);
  }

  private String generateCellsRange(
      @Nullable String sheetName, String topLeftCell, @Nullable String bottomRightCell) {
    if (sheetName == null) sheetName = "Data";
    if (bottomRightCell == null) bottomRightCell = topLeftCell;
    return sheetName + "!" + topLeftCell + ":" + bottomRightCell;
  }

  private void deleteCellsDataIfExist(Sheets sheetsService, String dataRange) throws IOException {
    int startIndex = Integer.parseInt(dataRange.split("!")[1].split(":")[0].substring(1)) - 1;
    int endIndex = Integer.parseInt(dataRange.split("!")[1].split(":")[1].substring(1));

    ValueRange response =
        sheetsService.spreadsheets().values().get(SPREADSHEET_ID, dataRange).execute();

    List<List<Object>> values = response.getValues();

    if (!org.springframework.util.ObjectUtils.isEmpty(values)) {
      DeleteDimensionRequest deleteRequest =
          new DeleteDimensionRequest()
              .setRange(
                  new DimensionRange()
                      .setSheetId(DATA_SHEET_ID)
                      .setDimension("ROWS")
                      .setStartIndex(startIndex).setEndIndex(endIndex));
      List<Request> requests = List.of(new Request().setDeleteDimension(deleteRequest));
      BatchUpdateSpreadsheetRequest body =
          new BatchUpdateSpreadsheetRequest().setRequests(requests);
      sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
    }
  }
}
