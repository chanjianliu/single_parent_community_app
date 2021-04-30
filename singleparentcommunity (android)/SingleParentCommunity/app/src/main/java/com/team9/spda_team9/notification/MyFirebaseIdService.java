package com.team9.spda_team9.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.team9.spda_team9.Authentication.User;

public class MyFirebaseIdService extends FirebaseMessagingService {

    FirebaseFirestore DB;
    FirebaseMessaging messaging;
    String username = "James";
    User user;
    String token;

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);

        DB.collection("User").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                user = task.getResult().toObject(User.class);
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            Log.d("MyToken",token);
                        }
                    }
                });
                updateToken(token);
            }
        });
    }

    private void updateToken(String refreshToken) {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseFirestore.getInstance().collection("User").document(user.getUsername()).update("token", refreshToken);
    }
}
