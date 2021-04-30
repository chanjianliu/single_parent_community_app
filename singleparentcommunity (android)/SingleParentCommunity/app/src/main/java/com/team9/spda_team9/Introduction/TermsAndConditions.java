package com.team9.spda_team9.Introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.team9.spda_team9.Introduction.StartActivityPart2;
import com.team9.spda_team9.R;

public class TermsAndConditions extends AppCompatActivity {

    private ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        btn = (ImageButton) findViewById(R.id.bt_close);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}