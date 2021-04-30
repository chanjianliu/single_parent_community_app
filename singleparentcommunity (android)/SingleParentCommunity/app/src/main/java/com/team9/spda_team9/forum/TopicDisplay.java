package com.team9.spda_team9.forum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.R;

import java.util.List;
import java.util.stream.Collectors;

public class TopicDisplay extends AppCompatActivity implements FirestoreAdapter.OnListItemClick {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreAdapter adapter;

    Category category;
    List<Topic> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent categoryIntent = getIntent();
        String cat = categoryIntent.getStringExtra("category");
        category = Enum.valueOf(Category.class, cat);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.firestorelist);

        refreshRecycleView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecycleView();
        adapter.startListening();
    }

    @Override
    public void onItemClick(int position) {
        Log.d("ITEM_CLICK", "Clicked the item: " + position);
        Topic topic = topics.get(position);
        Log.d("clicked topic", "clicked on " + position + " row" + "topic detail: " + topic.getTitle() + " " + topic.getBody() + " " + topic.getDateTime() + " " + topic.getCategory());
        Intent intent = new Intent(this, TopicDetails.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    public void refreshRecycleView() {
        //Step1: Query the firestore for what you want to populate
        Query query = firebaseFirestore.collection("Topic").whereEqualTo("category", category);
        firebaseFirestore.collection("Topic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Topic> allTopics = task.getResult().toObjects(Topic.class);
                topics = allTopics.stream().filter(x -> x.getCategory() == category).collect(Collectors.toList());
            }
        });

        //Step 2: Requires recycleroptions which contains the model
        /*
         * FirestoreRecyclerAdapter is a subclass of the normal RecyclerView.Adapter
         * takes care of most of the complicated stuff like reacting to dataset changes a
         * needs a ViewHolder and has an onCreateViewHolder and onBindViewHolder method where it binds data from the model class to their corresponding views in the itemView layout.
         * instantiate the FirestoreRecyclerAdapter, pass it a FirestoreRecyclerOptions object which contains a Firestore Query.
         * With orderBy we can order the items in this query by a field in ascending or descending order.
         * With startListening and stopListening we tell our adapter when to start and stop
         * updating it’s dataset, for example in the onStart and onStop activity lifecycle callback method.
         * Refer to: https://firebaseopensource.com/projects/firebase/firebaseui-android/
         * */
        FirestoreRecyclerOptions<Topic> options = new FirestoreRecyclerOptions.Builder<Topic>()
                .setQuery(query, Topic.class)
                .build();

        //Step 3: Add your RecyclerAdapter which requires a viewholder
        adapter = new FirestoreAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}