package com.team9.spda_team9.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.team9.spda_team9.R;
import com.team9.spda_team9.forum.ForumActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo3 extends AppCompatActivity {
    private final String TAG = "UserInfo3";

    private EditText mEditTextInterests;
    private EditText mEditTextSelfDescription;
    private Button mButtonRegister;

    private String interest;
    private String selfDescription;

    // Facebook Login
    private String registerMethod;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<String> updateFields = new ArrayList<>();
    private List<String> storeFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info3);

        updateFields.add("interest");
        updateFields.add("selfDescription");

        String[] fields = {"fullName", "username", "email", "gender",
                "numberOfKids", "kidsDescription",
                "location", "profession",
                "interest", "selfDescription"};
        for (String field : fields) {
            storeFields.add(field);
        }

        mEditTextInterests = findViewById(R.id.interests);
        mEditTextSelfDescription = findViewById(R.id.selfDescription);
        mButtonRegister = findViewById(R.id.register);

        Intent intent = getIntent();
        User myUser = (User) intent.getSerializableExtra("user");
        registerMethod = intent.getStringExtra("registerMethod");

        Log.d(TAG, registerMethod);

        if (registerMethod.equals("edit")) {
            mButtonRegister.setText("Confirm changes");
            mEditTextInterests.setText(myUser.getInterest());
            mEditTextSelfDescription.setText(myUser.getSelfDescription());
        }

        mAuth = FirebaseAuth.getInstance();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interest = mEditTextInterests.getText().toString();
                selfDescription = mEditTextSelfDescription.getText().toString();

                if (ValidationUtil.notEmptyValidation(interest)
                        && ValidationUtil.notEmptyValidation(selfDescription)) {
                    myUser.setInterest(interest);
                    myUser.setSelfDescription(selfDescription);
                    myUser.setRegDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));

                    if (registerMethod.equals("edit")) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("User").document(uid).set(myUser, SetOptions.mergeFields(updateFields));
                        saveUserInfo(myUser);
                        finish();
                    } else if (registerMethod.equals("normal")) {
                        mAuth.createUserWithEmailAndPassword(myUser.getEmail(), myUser.getPassword()).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        String userId = mAuth.getCurrentUser().getUid(); //authentication Id

                                        DocumentReference documentReference = db.collection("User").document(userId);
                                        documentReference.set(myUser, SetOptions.mergeFields(storeFields)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "OnSuccess: user profile is created for" + userId);
                                            }
                                        });
                                        saveUserInfo(myUser);
                                        startActivity(new Intent(UserInfo3.this, ForumActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                });
                    } else if (registerMethod.equals("facebook")) {
                        String userId = mAuth.getCurrentUser().getUid(); //authentication Id

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        currentUser.updateEmail(myUser.getEmail());
                        currentUser.updatePassword(myUser.getPassword());

                        DocumentReference documentReference = db.collection("User").document(userId);
                        documentReference.set(myUser, SetOptions.mergeFields(storeFields)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "OnSuccess: user profile is created for" + userId);
                            }
                        });
                        saveUserInfo(myUser);
                        startActivity(new Intent(UserInfo3.this, ForumActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        Log.d(TAG, "Nothing happened");
                    }
                } else {
                    if (!ValidationUtil.notEmptyValidation(interest)) {
                        mEditTextInterests.setError("Please enter your interest.");
                    }
                    if (!ValidationUtil.notEmptyValidation(selfDescription)) {
                        mEditTextSelfDescription.setError("Please enter your self description.");
                    }
                }
            }
        });
    }

    public void saveUserInfo(User user) {
        SharedPreferences userDetail = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetail.edit();
        editor.putString("fullName", user.getFullName());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        //editor.putString("password", user.getPassword());

        editor.putInt("numberOfKids", user.getNumberOfKids());
        editor.putString("kidsDescription", user.getKidsDescription());

        editor.putString("location", user.getLocation());
        editor.putString("profession", user.getProfession());

        editor.putString("interests", user.getInterest());
        editor.putString("selfDescription", user.getSelfDescription());
        editor.putString("gender", user.getGender().toString());

        editor.commit();
    }

    public User getUserInfo() {
        User user = new User();
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        user.setFullName(pref.getString("fullName", null));
        user.setUsername(pref.getString("username", null));
        user.setEmail(pref.getString("email", null));
        //user.setPassword(pref.getString("password", null));

        user.setNumberOfKids(pref.getInt("numberOfKids", 0));
        user.setKidsDescription(pref.getString("kidsDescription", null));

        user.setLocation(pref.getString("location", null));
        user.setProfession(pref.getString("profession", null));

        user.setInterest(pref.getString("interests", null));
        user.setSelfDescription(pref.getString("selfDescription", null));
        user.setGender(pref.getString("gender", null) == "male" ? Gender.male : Gender.female);
        return user;
    }
}