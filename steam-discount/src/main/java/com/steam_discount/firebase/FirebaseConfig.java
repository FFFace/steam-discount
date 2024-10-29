package com.steam_discount.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account.path}")
    private String serviceAccountPath;

    @Bean
    public Storage storage() throws IOException {
        try (FileInputStream serviceAccount = new FileInputStream(serviceAccountPath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

    @PostConstruct
    public void init() throws IOException {
        try (FileInputStream serviceAccount = new FileInputStream(serviceAccountPath)) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
        }
    }
}
