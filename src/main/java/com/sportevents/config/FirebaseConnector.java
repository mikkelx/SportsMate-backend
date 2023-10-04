package com.sportevents.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseConnector {

    public FirebaseConnector() throws IOException {
        InputStream serviceAccount = getClass()
                .getResourceAsStream("/sportevents-firebase-adminsdk-connection-string.json");

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket("sportevents-20a98.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);
    }

}