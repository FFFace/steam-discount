package com.steam_discount.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    public Storage storage() throws IOException {
        try (FileInputStream serviceAccount = new FileInputStream("./firebase_service_account.json")) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

    @PostConstruct
    public void init() throws IOException {
        try (FileInputStream serviceAccount = new FileInputStream("./firebase_service_account.json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
        }
    }
}
