package com.team9.spda_team9.Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.R;

import java.util.List;

public class Register extends AppCompatActivity {

    private final String TAG = "Register";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText mEditTextFullName;
    private EditText mEditTextUserName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonNextPage;

    private String email;
    private String password;
    private String fullName;
    private String userName;
    private Gender gender;

    private RadioGroup radioGroup;

    // Facebook Login
    private String registerMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        radioGroup = findViewById(R.id.gender);

        mEditTextFullName = findViewById(R.id.fullname);
        mEditTextUserName = findViewById(R.id.username);
        mEditTextEmail = findViewById(R.id.email);
        mEditTextPassword = findViewById(R.id.password);
        mButtonNextPage = findViewById(R.id.NextPage1);

        //facebook
        Intent intent = getIntent();
        registerMethod = intent.getStringExtra("registerMethod");
        Log.d(TAG, registerMethod);
        Log.d(TAG, Boolean.toString(registerMethod.equals("normal")));

        mButtonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEditTextEmail.getText().toString().trim();
                password = mEditTextPassword.getText().toString().trim();
                fullName = mEditTextFullName.getText().toString();
                userName = mEditTextUserName.getText().toString().toLowerCase();
                gender = (returnGender() == R.id.radioMale) ? Gender.male : Gender.female;

                if (ValidationUtil.emailValidation(email)
                        && ValidationUtil.passwordValidation(password)
                        && ValidationUtil.fullNameValidation(fullName)
                        && ValidationUtil.notEmptyValidation(userName)) {
                    Task task1 = db.collection("User")
                            .whereEqualTo("email", email)
                            .get();
                    Task task2 = db.collection("User")
                            .whereEqualTo("username", userName)
                            .get();
                    Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(task1, task2);
                    allTasks
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "checking failed");
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                                @Override
                                public void onSuccess(List<QuerySnapshot> querySnapshots) {
                                    if (!querySnapshots.get(0).isEmpty() || !querySnapshots.get(1).isEmpty()) {
                                        if (!querySnapshots.get(0).isEmpty()) {
                                            Log.d(TAG, "This email has already been used.");
                                            mEditTextEmail.setError("This email has already been used.");
                                        }
                                        if (!querySnapshots.get(1).isEmpty()) {
                                            Log.d(TAG, "This user name has already been used.");
                                            mEditTextUserName.setError("This user name has already been used.");
                                        }
                                    } else {
                                        Log.d(TAG, "GoGoGo");
                                        User myUser = new User();
                                        myUser.setEmail(email);
                                        myUser.setPassword(password);
                                        myUser.setFullName(fullName);
                                        myUser.setUsername(userName);
                                        myUser.setGender(gender);
//                                        myUser.setSuspended(false);

                                        Intent intent = new Intent(Register.this, UserInfo1.class);
                                        intent.putExtra("user", myUser);
                                        intent.putExtra("registerMethod", registerMethod);
                                        startActivity(intent);
                                    }
                                }
                            });
                } else {
                    if (!ValidationUtil.emailValidation(email)) {
                        mEditTextEmail.setError("Please enter a valid email address.");
                    }
                    if (!ValidationUtil.passwordValidation(password)) {
                        mEditTextPassword.setError("Password must include Uppercase, Lowercase, Symbols and Numbers.");
                    }
                    if (!ValidationUtil.fullNameValidation(fullName)) {
                        mEditTextFullName.setError("Please enter your full name");
                    }
                    if (!ValidationUtil.userNameValidation(userName)) {
                        mEditTextUserName.setError("Please enter your username");
                    }
                    Toast.makeText(Register.this, "Information cannot be validated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int returnGender() {
        radioGroup = findViewById(R.id.gender);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        return selectedId;
    }
}