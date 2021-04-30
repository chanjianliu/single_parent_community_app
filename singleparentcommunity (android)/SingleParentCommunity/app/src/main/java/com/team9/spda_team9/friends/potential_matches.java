package com.team9.spda_team9.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.team9.spda_team9.forum.Topic;
import com.team9.spda_team9.forum.TopicDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class potential_matches extends Fragment implements UserAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    private UserAdapter adapter;
    private RecyclerView recyclerView;
    List<User> users;
    List<String> potential_matches;
    List<User> matches;
    private Matches userMatch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends_pm, container, false);

        View tabLayout1 = view.findViewById(R.id.tablayout1);

        String username = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        getPotentialMatches();

        return view;
    }


    private void refreshRecyclerView() {
        Query query = firebaseFirestore.collection("User").whereIn("username", potential_matches);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                matches = task.getResult().toObjects(User.class);
            }
        });


        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserAdapter(options, com.team9.spda_team9.friends.potential_matches.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(com.team9.spda_team9.friends.potential_matches.this.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onPause(){
        super.onPause();
        if(userMatch != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (userMatch != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        getPotentialMatches();
        if (userMatch != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (userMatch != null) {
            refreshRecyclerView();
            adapter.startListening();
        }
    }

    public void getPotentialMatches(){
        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User currentUser = task.getResult().toObject(User.class);
                FirebaseFirestore.getInstance().collection("Matches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> result = task.getResult().getDocuments();

                        if (result != null && result.size() != 0 && task.isSuccessful()) {
                            List<DocumentSnapshot> userMatchDoc = result.stream().filter(x -> x.getId().equals(currentUser.getUsername())).collect(Collectors.toList());

                            if (userMatchDoc != null && userMatchDoc.size() != 0) {
                                potential_matches = new ArrayList<>();
                                userMatch = userMatchDoc.get(0).toObject(Matches.class);
                                Set<String> matches = userMatch.getMatches().keySet();
                                List<String> userKeys = new ArrayList<>();
                                matches.forEach(x -> userKeys.add(x));

                                int max = 0;
                                if (userKeys.size() < 10)
                                    max = userKeys.size();
                                else
                                    max = 10;
                                //limit to 10 potential matches only as firebase only allow 10 value in whereIn query
                                for(int i = 0; i < max; i++){
                                    potential_matches.add(userKeys.get(i));
                                }
                                refreshRecyclerView();
                            }
                        }
                    }

                });
            }
        });
    }




    @Override
    public void onItemClick(int position) {
        User user = matches.get(position);
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
