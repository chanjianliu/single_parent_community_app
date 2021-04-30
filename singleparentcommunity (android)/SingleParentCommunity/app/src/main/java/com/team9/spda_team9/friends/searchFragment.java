package com.team9.spda_team9.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.List;

public class searchFragment extends Fragment implements Search_Adapter.OnListItemClick {

    private TextView no_result;
    private TextView description;
    private Search_Adapter adapter;
    private RecyclerView recyclerView;
    private String userId;
    private String username;
    private FirebaseFirestore firebaseFirestore;
    private EditText search;
    private boolean enter;
    private List<User> friends;
    private String key;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        View tabLayout1 = view.findViewById(R.id.tablayout1);
        tabLayout1.setVisibility(View.INVISIBLE);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences userDetail = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = userDetail.getString("username", "error");
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        no_result = view.findViewById(R.id.no_result);
        description = view.findViewById(R.id.msg_description);

        no_result.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);

        search = view.findViewById(R.id.search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search.performClick();
                    refreshRecyclerView();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void refreshRecyclerView() {
        key = search.getText().toString().trim();
        if (key != null && key.length() > 0) {
            Query query = firebaseFirestore.collection("User").whereEqualTo("username", key);

            firebaseFirestore.collection("User").whereEqualTo("username", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    friends = null;
                    if (task.getResult().getDocuments() != null && task.getResult().getDocuments().size() != 0 && task.isSuccessful()) {
                        friends = task.getResult().toObjects(User.class);
                        no_result.setVisibility(View.INVISIBLE);
                        description.setVisibility(View.INVISIBLE);
                    } else {
                        no_result.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);
                    }
                }
            });

            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class)
                    .build();

            adapter = new Search_Adapter(options, searchFragment.this);
            adapter.updateOptions(options);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(searchFragment.this.getContext()));
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(friends != null) {
            adapter.stopListening();
        }
    }

    public void onPause(){
        super.onPause();
        if(friends != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        refreshRecyclerView();
        if (friends != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshRecyclerView();
        if (friends != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onItemClick(int position) {
        User user = friends.get(position);
        Intent intent = new Intent(getContext(), UserProfileDetails.class);
        intent.putExtra("user", user);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User me = task.getResult().toObject(User.class);
                List<String> myFriends;
                if(me.getFriends() == null){
                    myFriends = new ArrayList<>();
                }else{
                    myFriends = me.getFriends();
                }
                boolean result = myFriends.contains(user.getUsername());
                intent.putExtra("isFriend", result);
                startActivity(intent);
            }
        });
    }
}