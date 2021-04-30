package com.team9.spda_team9.sa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;


@Service
public class SAService implements ISAService {
	public static final String COL_NAME = "SA_Score";

	@Override
	public HashMap<String, Score> getSAScores() throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> collectionsApiFuture = dbFirestore.collection(COL_NAME).get();
		List<QueryDocumentSnapshot> documents = collectionsApiFuture.get().getDocuments();
		HashMap<String, Score> data = new HashMap<String, Score>();
		for (QueryDocumentSnapshot document : documents) {
			System.out.println(document.getId() + " => " + document.toObject(Score.class));
			data.put(document.getId(),document.toObject(Score.class));
			}
		
		return data;
	}

	@Override
	public String getTimestamp() throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> collectionsApiFuture = dbFirestore.collection(COL_NAME).get();
		List<QueryDocumentSnapshot> documents = collectionsApiFuture.get().getDocuments();
		String timestamp = documents.get(0).toObject(Score.class).getTimestamp();
		return timestamp;
	}

}
