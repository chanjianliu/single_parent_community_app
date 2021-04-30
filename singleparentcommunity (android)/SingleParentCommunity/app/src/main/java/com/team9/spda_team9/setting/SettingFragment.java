package com.team9.spda_team9.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.team9.spda_team9.Authentication.Gender;
import com.team9.spda_team9.Authentication.Profile;
import com.team9.spda_team9.Authentication.Register;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.Authentication.UserInfo1;
import com.team9.spda_team9.Authentication.UserInfo2;
import com.team9.spda_team9.Authentication.UserInfo3;
import com.team9.spda_team9.Introduction.StartActivityPart2;
import com.team9.spda_team9.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preferences);
        findPreference("userProfile");
        Preference preference = (Preference) findPreference("userProfile");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), Register.class);
                startActivity(intent);

                return true;
            }
        });

        findPreference("account_settings");
        Preference preference5=(Preference) findPreference("account_settings") ;
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent=new Intent(getContext(), Profile.class);
                startActivity(intent);
                return true;
            }
        });

        findPreference("changeKids");
        Preference preference1 = (Preference) findPreference("changeKids");
        preference1.setOnPreferenceClickListener(preference12 -> {

            Intent intent = new Intent(getContext(), UserInfo1.class);
            User user = getUserInfo();
            intent.putExtra("user", user);
            intent.putExtra("registerMethod", "edit");
            startActivity(intent);

            return true;
        });

        findPreference("changeProf");
        Preference preference2 = (Preference) findPreference("changeProf");
        preference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getContext(), UserInfo2.class);
                User user = getUserInfo();
                intent.putExtra("user", user);
                intent.putExtra("registerMethod", "edit");
                startActivity(intent);

                return true;
            }
        });

        findPreference("changeInterest");
        Preference preference3 = (Preference) findPreference("changeInterest");
        preference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getContext(), UserInfo3.class);

                User user = getUserInfo();
                intent.putExtra("user", user);
                intent.putExtra("registerMethod", "edit");
                startActivity(intent);

                return true;
            }
        });

        findPreference("aboutUs");
        Preference preference4 = findPreference("aboutUs");
        preference4.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), aboutUsFragment.class);
                startActivity(intent);

                return true;
            }
        });

        findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                SharedPreferences userDetail = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = userDetail.edit();
                editor.clear();
                editor.commit();

                startActivity(new Intent(getContext(), StartActivityPart2.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            }
        });

        findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Uri uri = Uri.parse("mailto: sa51.team9@gmail.com");

                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public User getUserInfo() {
        User user = new User();
        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        user.setFullName(pref.getString("fullName", null));
        user.setUsername(pref.getString("username", null));
        user.setEmail(pref.getString("email", null));
        user.setPassword(pref.getString("password", null));

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