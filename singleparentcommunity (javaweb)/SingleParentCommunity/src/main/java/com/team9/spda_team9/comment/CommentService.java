package com.team9.spda_team9.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class CommentService implements ICommentService {
	public static final String COL_NAME = "Comment";

	public String saveForum(Comment comment) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<DocumentReference> document = dbFirestore.collection(COL_NAME).add(comment);
		
		//saving commentId into cloud
		comment.setCommentId(document.get().getId());
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(comment.getCommentId())
				.set(comment);
		
		return "Comment " + document.get().getId().toString() + " saved." + " " 
				+ collectionsApiFuture.get().getUpdateTime().toString();
	} 

	//get all comments by 
	public List<String> getCommentsByUserId(String userId) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		
		CollectionReference collectionReference = dbFirestore.collection(COL_NAME);
		ApiFuture<QuerySnapshot> future = collectionReference.get();
		QuerySnapshot document = future.get();
		
		if (!document.isEmpty()) {
			List<Comment> allComments = document.toObjects(Comment.class);
			List<Comment> comments = allComments.stream().filter(x -> x.getUserId().equals(userId)).collect(Collectors.toList());
			
			List<String> text = new ArrayList<>();
			
			comments.forEach(x -> {
				text.add(x.getBody());
			});
			
			return text;
		} else {
			return null;
		}
	}

	public String updateComment(Comment comment) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(comment.getCommentId())
				.set(comment);
		return collectionsApiFuture.get().getUpdateTime().toString();
	}

	public String deleteComment(String commentId) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(commentId).delete();
		return "Comment with id " + commentId + " has been deleted\t" + writeResult.get().getUpdateTime().toString();
	}
	
	@Override
	public List<Comment> getAllUserComment() throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> collectionsApiFuture = dbFirestore.collection(COL_NAME).get();
		List<QueryDocumentSnapshot> documents = collectionsApiFuture.get().getDocuments();
		List<Comment> toBeSent = new ArrayList<Comment>();
		for (QueryDocumentSnapshot document : documents) {
			  //System.out.println(document.getId() + " => " + document.toObject(Comment.class));
			toBeSent.add(document.toObject(Comment.class));
			}
		
		return toBeSent;
	}
}
