package com.team9.spda_team9.Introduction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.R;
import com.team9.spda_team9.forum.ForumActivity;

public class Redirect extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User currentuser = task.getResult().toObject(User.class);
                String name = currentuser.getFullName();
                tv1 = findViewById(R.id.welcome);
                tv2 = findViewById(R.id.welcome2);
                tv1.setText("Welcome " + name + "!");
                tv2.setText("You are already logged in. Redirecting...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Redirect.this, ForumActivity.class));
                        finish();
                    }
                }, 2000);

            }
        });






    }
}