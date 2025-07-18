package com.shohan.portfolio_ai.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class CalendarConfig {

    private static final String APPLICATION_NAME = "Appointment Booking API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String credentialsPath;
    private final String tokensDirectoryPath;
    private final ResourceLoader resourceLoader;

    public CalendarConfig(
            @Value("${app.calender.credentialsPath}") String credentialsPath,
            @Value("${app.calender.tokensDirectoryPath}") String tokensDirectoryPath,
            ResourceLoader resourceLoader
    ) {
        this.credentialsPath = credentialsPath;
        this.tokensDirectoryPath = tokensDirectoryPath;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @Primary
    public Calendar googleCalender() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        InputStream credentialsStream = resourceLoader.getResource(credentialsPath).getInputStream();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsStream));

        // Build flow and trigger user authorization request.
        File file = new File(tokensDirectoryPath);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR))
                .setDataStoreFactory(new FileDataStoreFactory(file))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        Credential userCredentials = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, userCredentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}