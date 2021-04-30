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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.List;

public class UserInfo2 extends AppCompatActivity {
    private final String TAG = "UserInfo2";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText mEditTextLocation;
    private EditText mEditTextProfession;
    private Button mButtonNextPage;

    private String location;
    private String profession;
    // Facebook Login
    private String registerMethod;

    private List<String> updateFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info2);

        updateFields.add("location");
        updateFields.add("profession");

        mEditTextLocation = findViewById(R.id.location);
        mEditTextProfession = findViewById(R.id.Profession);
        mButtonNextPage = findViewById(R.id.NextPage3);

        Intent intent = getIntent();
        User myUser = (User) intent.getSerializableExtra("user");
        registerMethod = intent.getStringExtra("registerMethod");

        Log.d(TAG, registerMethod);

        if (registerMethod.equals("edit")) {
            mButtonNextPage.setText("Confirm changes");
            mEditTextLocation.setText(myUser.getLocation());
            mEditTextProfession.setText(myUser.getProfession());
        }

        mButtonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = mEditTextLocation.getText().toString();
                profession = mEditTextProfession.getText().toString();

                if (ValidationUtil.notEmptyValidation(location)
                        && ValidationUtil.notEmptyValidation(profession)) {
                    myUser.setLocation(location);
                    myUser.setProfession(profession);

                    if (registerMethod.equals("edit")) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("User").document(uid).set(myUser, SetOptions.mergeFields(updateFields));
                        saveUserInfo(myUser);
                        finish();
                    } else {
                        Intent intent2 = new Intent(UserInfo2.this, UserInfo3.class);
                        intent2.putExtra("user", myUser);
                        intent2.putExtra("registerMethod", registerMethod);
                        startActivity(intent2);
                    }

                } else {
                    if (!ValidationUtil.notEmptyValidation(location)) {
                        mEditTextLocation.setError("Please enter a validated location.");
                    }
                    if (!ValidationUtil.notEmptyValidation(profession)) {
                        mEditTextProfession.setError("Please enter your profession.");
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