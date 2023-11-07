package com.mm.csvparserservice.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

public class ReportServiceImplGoogle {
  private static Sheets sheetsService;
  private static String APPLICATION_NAME = "Java-learning";
  private static String SPREADSHEET_ID = "1JSwxGLTXA8PSn6_dQrXEpPSsy6vEKIPPHT7sv62cOx8";
  private static String USER_ID = "desktop-user-01";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  private static Credential authorize() throws IOException, GeneralSecurityException {
    InputStream in = ReportServiceImplGoogle.class.getResourceAsStream("/credentials.json");
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
            .setAccessType("offline")
            .build();

    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(USER_ID);
  }

  public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
    return new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, authorize())
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  public static void main(String[] args) throws GeneralSecurityException, IOException {
    sheetsService = getSheetsService();
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
