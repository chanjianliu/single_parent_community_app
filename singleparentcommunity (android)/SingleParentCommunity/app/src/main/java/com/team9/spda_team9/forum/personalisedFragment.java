package com.team9.spda_team9.forum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class personalisedFragment extends Fragment implements personalised.OnListItemClick {

    private FirebaseFirestore DB;
    private RecyclerView recyclerView;
    private personalised adapter;
    private List<Topic> topics;

    private TextView no_sub;
    private TextView description;
    private String userId;
    private Subscription userSub;
    private Query query;

    private static final String TAG = personalisedFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personalised, container, false);

        View tabLayout1 = root.findViewById(R.id.tablayout1);
        tabLayout1.setVisibility(View.INVISIBLE);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        no_sub = root.findViewById(R.id.no_sub);
        description = root.findViewById(R.id.msg_description);


        DB = FirebaseFirestore.getInstance();
        refreshRecyclerView();
        return root;
    }

    @Override
    public void onItemClick(int position) {
        Topic topic = topics.get(position);
        Intent intent = new Intent(getContext(), TopicDetails.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (userSub != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        refreshRecyclerView();
        if (userSub != null) {
            adapter.startListening();
        }
    }

    private void refreshRecyclerView() {
        userSub = null;
        userId = FirebaseAuth.getInstance().getUid();
        DB.collection("Subscription").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Subscription> allSubs = task.getResult().toObjects(Subscription.class);
                if (allSubs != null && allSubs.size() != 0 && task.isSuccessful()) {
                    List<Subscription> result = allSubs.stream().filter(x -> x.getUserId().equals(userId)).collect(Collectors.toList());

                    if (result != null && result.size() != 0) {
                        //hiding the "No Subscription" msg
                        no_sub.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        //will only retrieve one result if there is a result as userId search is unique
                        userSub = result.get(0);
                        List<Category> subInCat = userSub.getSubscription();
                        query = DB.collection("Topic").whereIn("category", subInCat).orderBy("dateTime", Query.Direction.DESCENDING);

                        DB.collection("Topic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<Topic> allTopic = task.getResult().toObjects(Topic.class);
                                List<Topic> filter = allTopic.stream().filter(x -> userSub.getSubscription().contains(x.getCategory())).collect(Collectors.toList());
                                List<Topic> ascending = filter.stream().sorted((t1, t2) -> t1.getDateTime().compareTo(t2.getDateTime())).collect(Collectors.toList());
                                topics = Lists.reverse(ascending);
                            }
                        });

                        FirestoreRecyclerOptions<Topic> options = new FirestoreRecyclerOptions.Builder<Topic>()
                                .setQuery(query, Topic.class)
                                .build();

                        adapter = new personalised(options, personalisedFragment.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(personalisedFragment.this.getContext()));
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();
                    }
                } else {
                    no_sub.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}