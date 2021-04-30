package com.team9.spda_team9.Authentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.R;
import com.team9.spda_team9.forum.ForumActivity;

public class Login extends AppCompatActivity {

    private EditText mEditTextEmail, mEditTextPassword;
    private Button mLoginBtn;
    private TextView mForgetPwd;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = findViewById(R.id.Email);
        mEditTextPassword = findViewById(R.id.Password);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mForgetPwd = findViewById(R.id.forget);

        mAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEditTextEmail.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();

                if (ValidationUtil.emailValidation(email)) {
                    db.collection("User").whereEqualTo("email", email).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    User user = new User();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        user = documentSnapshot.toObject(User.class);
                                    }
                                    if (user.isSuspended()) {
                                        mEditTextEmail.setError("Your account is suspended.");
                                        Toast.makeText(Login.this, "Your account is suspended.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                                                new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    User user = task.getResult().toObject(User.class);

                                                                    saveUserInfo(user);

                                                                }
                                                            });
                                                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Login.this, ForumActivity.class)
                                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        } else {
                                                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                        );
                                    }
                                }
                            });
                } else {
                    mEditTextEmail.setError("Please enter a validated email address");
                    Toast.makeText(Login.this, "Please enter corrected email and password. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email To Receive Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and send rest link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link sent To Your Email",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset Link not sent"
                                                + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }

    public void saveUserInfo(User user) {
        SharedPreferences userDetail = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetail.edit();
        editor.putString("fullName", user.getFullName());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());

        editor.putInt("numberOfKids", user.getNumberOfKids());
        editor.putString("kidsDescription", user.getKidsDescription());

        editor.putString("location", user.getLocation());
        editor.putString("profession", user.getProfession());

        editor.putString("interests", user.getInterest());
        editor.putString("selfDescription", user.getSelfDescription());
        editor.putString("gender", user.getGender().toString());

        editor.commit();
    }
}