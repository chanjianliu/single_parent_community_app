package com.team9.spda_team9.Introduction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.R;
import com.team9.spda_team9.forum.ForumActivity;

import java.time.LocalDate;

public class StartActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (mAuth.getCurrentUser() != null) {

                    String email = mAuth.getCurrentUser().getEmail();
                    db.collection("User").whereEqualTo("email", email).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    User user = new User();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        user = documentSnapshot.toObject(User.class);
                                    }
                                    if (user.isSuspended()) {
                                        FirebaseAuth.getInstance().signOut();
                                        LoginManager.getInstance().logOut();

                                        SharedPreferences userDetail = getSharedPreferences("user", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = userDetail.edit();
                                        editor.clear();
                                        editor.commit();

                                        startActivity(new Intent(StartActivity.this, StartActivityPart2.class));
                                        Toast.makeText(StartActivity.this, "Your account is suspended.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), Redirect.class));
                                        finish();
                                    }
                                }
                            });
                } else {
                    startActivity(new Intent(StartActivity.this, IntroActivity.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}