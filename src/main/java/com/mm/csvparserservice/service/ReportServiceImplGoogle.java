package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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

      List<List<Object>> data = transactionService.getAllTransactionDtosAsData();
      List<Object> headerData =
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

      data.add(0, headerData);

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
}
