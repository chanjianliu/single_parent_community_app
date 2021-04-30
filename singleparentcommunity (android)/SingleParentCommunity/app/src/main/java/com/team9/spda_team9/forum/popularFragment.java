package com.team9.spda_team9.forum;

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
import com.google.common.collect.Lists;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.R;

import java.util.List;
import java.util.stream.Collectors;

public class popularFragment extends Fragment implements popular.OnListItemClick {

    private FirebaseFirestore DB;
    private RecyclerView recyclerView;
    private popular adapter;
    private List<Topic> topics;

    private static final String TAG = popularFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_popular, container, false);

        View tabLayout1 = root.findViewById(R.id.tablayout1);
        tabLayout1.setVisibility(View.INVISIBLE);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

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
        adapter.stopListening();
    }

    @Override
    public void onStart(){
        super.onStart();
        refreshRecyclerView();
        adapter.startListening();
    }

    private void refreshRecyclerView() {

        Query query = DB.collection("Topic").orderBy("dateTime", Query.Direction.DESCENDING);
        DB.collection("Topic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Topic> allTopic = task.getResult().toObjects(Topic.class);
                List<Topic> ascending = allTopic.stream().sorted((t1, t2) -> t1.getDateTime().compareTo(t2.getDateTime())).collect(Collectors.toList());
                topics = Lists.reverse(ascending);
            }
        });

        FirestoreRecyclerOptions<Topic> options = new FirestoreRecyclerOptions.Builder<Topic>()
                .setQuery(query, Topic.class)
                .build();

        adapter = new popular(options, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(popularFragment.this.getContext()));
        recyclerView.setAdapter(adapter);
    }
}
