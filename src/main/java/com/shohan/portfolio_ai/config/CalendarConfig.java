package com.shohan.portfolio_ai.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class CalendarConfig {

    private static final String APPLICATION_NAME = "Appointment Booking API";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String clientCredentialsPath;
    private final String serviceCredentialsPath;
    private final String tokensDirectoryPath;
    private final boolean enableServiceAccount;
    private final ResourceLoader resourceLoader;

    public CalendarConfig(
            @Value("${app.calender.clientCredentialsPath}") String clientCredentialsPath,
            @Value("${app.calender.serviceCredentialsPath}") String serviceCredentialsPath,
            @Value("${app.calender.tokensDirectoryPath}") String tokensDirectoryPath,
            @Value("${app.calender.enableServiceAccount}") boolean enableServiceAccount,
            ResourceLoader resourceLoader
    ) {
        this.clientCredentialsPath = clientCredentialsPath;
        this.serviceCredentialsPath = serviceCredentialsPath;
        this.tokensDirectoryPath = tokensDirectoryPath;
        this.enableServiceAccount = enableServiceAccount;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Calendar serviceAccountCalendar() throws IOException, GeneralSecurityException {
        // Load service account credentials from JSON key file
        Resource resource = resourceLoader.getResource(serviceCredentialsPath);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(resource.getInputStream())
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        // Initialize HTTP transport
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Build the Calendar client
        return new Calendar
                .Builder(httpTransport, GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Bean
    public Calendar userAccountCalender() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        InputStream credentialsStream = resourceLoader.getResource(clientCredentialsPath).getInputStream();
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

    @Bean
    @Primary
    public Calendar googleCalender(Calendar serviceAccountCalendar, Calendar userAccountCalender) {
        return enableServiceAccount ? serviceAccountCalendar : userAccountCalender;
    }
}