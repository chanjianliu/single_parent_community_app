package com.team9.spda_team9.admin;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.team9.spda_team9.comment.Comment;

@Service
public class AdminService implements IAdminService {
	
	public static final String COL_NAME = "Admin";
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public String createAdmin(Admin admin) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		
		//encrypt admin password
		String pwd = admin.getPassword();
		String encryptedPwd = bCryptPasswordEncoder.encode(pwd);
		admin.setPassword(encryptedPwd);
		
		ApiFuture<DocumentReference> document = dbFirestore.collection(COL_NAME).add(admin);
		
		admin.setUserId(document.get().getId());
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(admin.getUserId())
				.set(admin);
		
		return "admin with Id " + document.get().getId().toString() + " has been saved.";
	}
	
	@Override
	public Admin getAdminByUsername(String username) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME).get();
		QuerySnapshot future = query.get();
		
		List<Admin> admins = null;
		Admin admin = null;
		
		if (!future.isEmpty()) {
			admins = future.toObjects(Admin.class);
			admin = admins.stream().filter(x -> x.getUsername().equals(username)).findFirst().get();
		}
		return admin;
	}
	
	@Override
	public boolean authenticate(LoginData admin) throws InterruptedException, ExecutionException {
		boolean success = false;
		
		String username = admin.getUsername();
		String pwd = admin.getPassword();
		
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> query = dbFirestore.collection(COL_NAME).get();
		QuerySnapshot future = query.get();
		
		List<Admin> admins = null;
		Admin dbAdmin = null;
		
		if (!future.isEmpty()) {
			admins = future.toObjects(Admin.class);
			dbAdmin = admins.stream().filter(x -> x.getUsername().equals(username)).findFirst().get();
			
			if (dbAdmin != null) {
				String encryptedPwd = dbAdmin.getPassword();
				success = bCryptPasswordEncoder.matches(pwd, encryptedPwd);
			}
		}
		
		return success;
	}
	
	@Override
	public String updateAdmin(Admin admin) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(admin.getUserId())
				.set(admin);
		return collectionsApiFuture.get().getUpdateTime().toString();
	}
	
	@Override
	public String deleteAdmin(String userId) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(userId).delete();
		return "Admin with id " + userId + " has been deleted\t" + writeResult.get().getUpdateTime().toString();
	}

}
