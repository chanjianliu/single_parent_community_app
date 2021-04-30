package com.team9.spda_team9.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.team9.spda_team9.R;

public class Profile extends AppCompatActivity {

    private TextView textView;

    private TextView fullname;

//    private TextView delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        textView=findViewById(R.id.password);
        fullname=findViewById(R.id.updatename);
//        delete=findViewById(R.id.deleteProfile);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangeFullName.class);
                startActivity(intent);
            }
        });
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Profile.this, Deleteprofile.class);
//                startActivity(intent);
//            }
//        });
    }
}
