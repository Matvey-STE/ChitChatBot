package com.matveyvs.chitchatbot.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Log4j2
@Service
public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    public static final String SPREDSHEETS_ID = "1QDH4LHRIIjkpk1agePRxMQ64OgImYP1FsEi3nrGQTKs";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GoogleSheetsService.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
    public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public List<List<String>> getLineGoogleSheet(String sheetWithRange){
        List<List<String>> list = new ArrayList<>();
        try {
            Sheets sheetsService = getSheetsService();
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(SPREDSHEETS_ID, sheetWithRange)
                    .execute();
            List<List<Object>>  values = response.getValues();
            if (values == null || values.isEmpty()) {
                log.error("No data found");
            } else {
                for (List row : values) {
                    list.add(row);
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}