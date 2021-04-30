package com.team9.spda_team9.forum;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.team9.spda_team9.R;

import java.util.ArrayList;

public class allcats extends Fragment {

    private ArrayList<String> categories = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        View tabLayout1 = view.findViewById(R.id.tablayout1);
        tabLayout1.setVisibility(View.INVISIBLE);

        categories.clear();
        categories.add("Dating and Relationship");
        categories.add("Teenagers");
        categories.add("Parenting");
        categories.add("Child Care");
        categories.add("Finances");
        categories.add("Mental Health Support");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CategoryAdapter(categories, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
