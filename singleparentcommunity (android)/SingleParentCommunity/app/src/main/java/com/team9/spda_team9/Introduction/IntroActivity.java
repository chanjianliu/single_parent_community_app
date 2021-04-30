package com.team9.spda_team9.Introduction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabIndicator;
    private Button btnNext;
    private int position = 0;
    private Button btnGetStarted;
    private Animation btnAnim;
    private TextView Skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make sure activity displays to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // when this activity is about to be launch we need to check if has been opened before or not
        //for now this is commented out because I want to test my application.
        if (restorePrefData()) {
            startActivity(new Intent(getApplicationContext(), StartActivityPart2.class));
            finish();
        }

        setContentView(R.layout.activity_intro);

        // hide the action bar
        getSupportActionBar().hide();
        // views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_join);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        Skip = findViewById(R.id.text_skip);
        // fill list screen
        final List<DisplayItem> mList = new ArrayList<>();
        mList.add(new DisplayItem("Discover Solutions", "Share your problems with other single parents on our forum to discover new solutions to any issues you are facing in life.", R.drawable.findsolutions));
        mList.add(new DisplayItem("Unburden Yourself", "Find a listening ear anytime you want via our forum and chat with other single parents who understand you deeply.", R.drawable.unburden));
        mList.add(new DisplayItem("Build A Community", "Meet other single parents and have conversations with them to build a supportive community where you are loved and supported.", R.drawable.buildcommunity));
        // setup viewpager
        viewPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        viewPager.setAdapter(introViewPagerAdapter);
        // setup tab layout with viewpager
        tabIndicator.setupWithViewPager(viewPager);

        // next button click Listener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    viewPager.setCurrentItem(position);
                }
                if (position == mList.size() - 1) { // when we reach the last screen
                    loadLastScreen();
                }
            }
        });

        // tab layout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Get Started button click listener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open main activity
                Intent mainActivity = new Intent(IntroActivity.this, StartActivityPart2.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();
            }
        });
        // skip button click listener
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mList.size());
            }
        });
    }
    
    //this is to check if the user has been onboared before on the current phone. Not applicable for
    //as the restore pref data has been commented out above..REMEMBER to uncomment before submitting
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();
    }

    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        //on the last screen, no need to show the skip button anymore
        Skip.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnAnim);
    }
}

