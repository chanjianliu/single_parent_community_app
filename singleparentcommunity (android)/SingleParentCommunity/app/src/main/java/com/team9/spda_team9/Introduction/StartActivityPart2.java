package com.team9.spda_team9.Introduction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9.spda_team9.Authentication.Login;
import com.team9.spda_team9.Authentication.Register;
import com.team9.spda_team9.R;
import com.team9.spda_team9.forum.ForumActivity;


public class StartActivityPart2 extends AppCompatActivity {

    private final String TAG = "StartActivityPart2";
    private Button mButtonLogin;
    private Button register;
    private TextView mTextView1;
    private TextView mTextView2;
    //Facebook
    private CallbackManager mCallbackManager;
    private LoginButton mButtonFacebook;
    private FirebaseAuth mAuth;
    private String registerMethod;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity_part2);

        mButtonLogin = findViewById(R.id.login);
        register = findViewById(R.id.register);
        mTextView1 = findViewById(R.id.textView5);
        mTextView2 = findViewById(R.id.textView6);

        mTextView1.setPaintFlags(mTextView1.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        mTextView2.setPaintFlags(mTextView2.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivityPart2.this, Login.class);
                startActivity(intent);
            }
        });

        mTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivityPart2.this, TermsAndConditions.class);
                startActivity(intent);

            }
        });
        mTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivityPart2.this, PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivityPart2.this, Register.class);
                registerMethod = "normal";
                intent.putExtra("registerMethod", registerMethod);
                startActivity(intent);
            }
        });

        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mButtonFacebook = findViewById(R.id.button_facebook);
        mAuth = FirebaseAuth.getInstance();
        //Facebook
        mButtonFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Login successfully" + loginResult);
                AuthCredential authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String uid = currentUser.getUid();
                            db.collection("User")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (!documentSnapshot.exists()) {
                                                // If no info in database, go to register page to add information
                                                Intent intent = new Intent(StartActivityPart2.this, Register.class);
                                                registerMethod = "facebook";
                                                intent.putExtra("registerMethod", registerMethod);
                                                startActivity(intent);
                                                Log.d(TAG, registerMethod);
                                            } else {
                                                startActivity(new Intent(StartActivityPart2.this, ForumActivity.class)
                                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(StartActivityPart2.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Login error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}