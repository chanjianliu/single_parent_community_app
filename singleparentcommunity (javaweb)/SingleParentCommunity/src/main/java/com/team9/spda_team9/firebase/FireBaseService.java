package com.team9.spda_team9.firebase;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FireBaseService {
	
	@PostConstruct //to start Firebase service when app starts
	public void initialize() {
		try {
//			//firebase test (realtime database) not successful, couldn't connect
//			FileInputStream serviceAccount =
//					  new FileInputStream("./fir-test-eeaf9-firebase-adminsdk-2czdn-038e822b76.json");
//
//					FirebaseOptions options = new FirebaseOptions.Builder()
//							  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//							  .setDatabaseUrl("https://fir-test-eeaf9-default-rtdb.firebaseio.com/")
//							  .build();
//
//					FirebaseApp.initializeApp(options);
			
			//Firestore test
			FileInputStream serviceAccount =
					  new FileInputStream("./FireStoreTest.json");

					FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .build();

					FirebaseApp.initializeApp(options);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}