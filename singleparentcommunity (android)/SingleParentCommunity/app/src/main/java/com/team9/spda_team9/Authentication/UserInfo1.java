package com.team9.spda_team9.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo1 extends AppCompatActivity {
    private final String TAG = "UserInfo1";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button mButtonNextPage;
    private EditText mEditTextNumberOfKids;
    private EditText mEditTextKidsDescription;

    private String numberOfKids;
    private String kidsDescription;

    // Facebook Login
    private String registerMethod;

    private List<String> updateFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info1);

        updateFields.add("numberOfKids");
        updateFields.add("kidsDescription");

        mEditTextNumberOfKids = findViewById(R.id.numberOfKids);
        mEditTextKidsDescription = findViewById(R.id.kidsDescription);
        mButtonNextPage = findViewById(R.id.NextPage2);

        Intent intent = getIntent();
        User myUser = (User) intent.getSerializableExtra("user");
        registerMethod = intent.getStringExtra("registerMethod");

        Log.d(TAG, registerMethod);

        if (registerMethod.equals("edit")) {
            mButtonNextPage.setText("Confirm changes");
            mEditTextNumberOfKids.setText(String.valueOf(myUser.getNumberOfKids()));
            mEditTextKidsDescription.setText(myUser.getKidsDescription());
        }

        mButtonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numberOfKids = mEditTextNumberOfKids.getText().toString();
                kidsDescription = mEditTextKidsDescription.getText().toString();

                if (ValidationUtil.notEmptyValidation(numberOfKids)
                        && ValidationUtil.childrenValidation(numberOfKids)
                        && ValidationUtil.notEmptyValidation(kidsDescription)) {
                    myUser.setNumberOfKids(Integer.parseInt(numberOfKids));
                    myUser.setKidsDescription(kidsDescription);
                    if (registerMethod.equals("edit")) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("User").document(uid).set(myUser, SetOptions.mergeFields(updateFields));
                        saveUserInfo(myUser);
                        finish();
                    } else {
                        Intent intent2 = new Intent(UserInfo1.this, UserInfo2.class);
                        intent2.putExtra("user", myUser);
                        intent2.putExtra("registerMethod", registerMethod);
                        startActivity(intent2);
                    }
                } else {
                    if (!ValidationUtil.notEmptyValidation(numberOfKids)) {
                        mEditTextNumberOfKids.setError("Please enter the number of kids.");
                    } else if (!ValidationUtil.childrenValidation(numberOfKids)) {
                        mEditTextNumberOfKids.setError("Please enter a valid 1-digit number.");
                    }
                    if (!ValidationUtil.notEmptyValidation(kidsDescription)) {
                        mEditTextKidsDescription.setError("Please enter the number of kids.");
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
}