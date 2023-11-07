package com.mm.csvparserservice.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.mm.csvparserservice.configuration.GoogleSheetsConfiguration;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class ReportServiceImplGoogle {
  private static String SPREADSHEET_ID = "1JSwxGLTXA8PSn6_dQrXEpPSsy6vEKIPPHT7sv62cOx8";

  public static void main(String[] args) throws GeneralSecurityException, IOException {
    Sheets sheetsService = GoogleSheetsConfiguration.getSheetsService();
    String range = "congress!A2:C21";

    ValueRange response =
        sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();

    List<List<Object>> values = response.getValues();

    if (values == null || values.isEmpty()) {
      System.out.println("No data found");
    } else {
      for (List row : values) {
        System.out.printf("%s %s from %s\n", row.get(2), row.get(1), row.get(0));
      }
    }
  }
}
