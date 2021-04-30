package com.team9.spda_team9.topic;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ForumService implements IForumService {

	public static final String COL_NAME = "Topic";
	
	public String saveTopic(Topic topic) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<DocumentReference> document = dbFirestore.collection(COL_NAME).add(topic);
		
		//saving topicId into cloud
		topic.setTopicId(document.get().getId());
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(topic.getTopicId())
				.set(topic);
		
		return document.get().getId().toString() + " " + collectionsApiFuture.get().getUpdateTime().toString();
	}

	//the user's username is the userId/documentId
	public List<Topic> getTopicByCategory(String category) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		CollectionReference collection = dbFirestore.collection(COL_NAME);
		ApiFuture<QuerySnapshot> future = collection.get();
		QuerySnapshot document = future.get();
		
		if (!document.isEmpty()) {
			List<Topic> allTopics = null;
			List<Topic> topics = null;
			
			allTopics = document.toObjects(Topic.class);
			topics = allTopics.stream().filter(x -> x.getCategory().toString().equals(category)).collect(Collectors.toList());
			
			return topics;
		} else {
			return null;
		}
	}

	public String updateTopic(Topic topic) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(topic.getTopicId())
				.set(topic);
		return collectionsApiFuture.get().getUpdateTime().toString();
	}

	public String deleteTopic(String topicId) {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(topicId).delete();
		return "Topic with id " + topicId + " has been deleted";
	}
}
