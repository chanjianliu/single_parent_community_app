package com.team9.spda_team9.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.stream.Collectors;

public class friends_fragment extends Fragment implements Friends_Adapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    User user;
    List<User> friends;
    private Friends_Adapter adapter;
    private RecyclerView recyclerView;
    private String userId;
    private String username;
    private TextView no_friends;
    private TextView description;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends_friends, container, false);

        View tabLayout1 = view.findViewById(R.id.tablayout1);
        tabLayout1.setVisibility(View.INVISIBLE);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences userDetail = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = userDetail.getString("username", "error");
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        no_friends = view.findViewById(R.id.no_friends);
        description = view.findViewById(R.id.msg_description);

        refreshRecyclerView();
        return view;
    }

    private void refreshRecyclerView() {
        Query query = firebaseFirestore.collection("User").whereArrayContains("friends", username);

        firebaseFirestore.collection("User").whereArrayContains("friends", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                friends = null;
                if (task.getResult().getDocuments() != null && task.getResult().getDocuments().size() != 0 && task.isSuccessful()){
                    friends = task.getResult().toObjects(User.class);
                    no_friends.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                } else {
                    no_friends.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                }
            }
        });

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new Friends_Adapter(options, friends_fragment.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(friends_fragment.this.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
