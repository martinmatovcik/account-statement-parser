package com.mm.csvparserservice.configuration;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

public class GoogleSheetsConfiguration {
  private static final String APPLICATION_NAME = "Java-learning";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  private static Credential authorize() throws IOException, GeneralSecurityException {
    InputStream in = GoogleSheetsConfiguration.class.getResourceAsStream("/credentials.json");
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(Objects.requireNonNull(in)));

    List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
            .setAccessType("offline")
            .build();

    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
        .authorize("Desktop client 2");
  }

  public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
    return new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, authorize())
        .setApplicationName(APPLICATION_NAME)
        .build();
  }
}
