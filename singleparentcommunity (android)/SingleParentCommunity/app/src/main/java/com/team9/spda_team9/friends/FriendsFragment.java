package com.team9.spda_team9.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {

    View friendFragment;
    TabLayout tabLayout;
    ViewPager viewPager;

    public FriendsFragment() {
    }

    public static FriendsFragment getInstance(){return new FriendsFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        friendFragment = inflater.inflate(R.layout.fragment_friends, container, false);
        viewPager = friendFragment.findViewById(R.id.viewPager1);
        tabLayout = friendFragment.findViewById(R.id.tablayout2);

        return friendFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new friends_fragment(), "Friends");
        adapter.addFragment(new potential_matches(), "Potential Matches");
        adapter.addFragment(new searchFragment(), "Search");

        viewPager.setAdapter(adapter);
    }


    public static class SectionPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> titleList = new ArrayList<>();

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        public void addFragment(Fragment fragment, String title)    {
            fragmentList.add(fragment);
            titleList.add(title);
        }
    }

}