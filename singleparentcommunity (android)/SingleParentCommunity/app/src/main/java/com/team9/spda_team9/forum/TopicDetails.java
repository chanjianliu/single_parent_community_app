package com.team9.spda_team9.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;
import com.team9.spda_team9.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TopicDetails extends AppCompatActivity implements View.OnClickListener,FirestoreAdapter2.OnListItemClick{
    final String ReplyToComment = "ReplyToComment";
    final String ReplyToTopic = "ReplyToTopic";
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreAdapter2 adapter;
    Button button;
    Topic topic;
    List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);

        Intent intent = getIntent();
        topic = (Topic) intent.getSerializableExtra("topic");

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.firestorelist);

        TextView tv1 = findViewById(R.id.topicTitle);
        TextView tv2 = findViewById(R.id.topicPostedBy);
        TextView tv3 = findViewById(R.id.topicPostedOn);
        TextView tv4 = findViewById(R.id.topicBody);
        TextView tv5 = findViewById(R.id.topicCategory);
        tv1.setText(topic.getTitle());
        tv2.setText(topic.getUsername());

        //setting up unix to dateTime then String for display
        Date date = new Date(Long.parseLong(topic.getDateTime()));
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Singapore"));
        String formattedDate = sdf.format(date);
        tv3.setText(formattedDate);

        tv4.setText(topic.getBody());
        tv5.setText(topic.getCategory().toString());

        button = findViewById(R.id.reply);
        button.setOnClickListener(this);

        refreshRecycleView();
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart(){
        super.onStart();
        refreshRecycleView();
        adapter.startListening();
    }

    @Override
    public void onItemClick(int position) {
        Log.d("ITEM_CLICK", "Clicked the item: " + position );
        Comment comment = comments.get(position);
        Log.d("clicked topic", "clicked on "+ position + " row" + "topic detail: " + topic.getTitle() + " " + topic.getBody() + " " + topic.getDateTime() + " " + topic.getCategory());
        Intent intent = new Intent(this, TopicReply.class);
        intent.putExtra("replyto", true);
        intent.putExtra("topic", topic);
        intent.putExtra("comment", comment);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        if(v == button){
            Intent intent = new Intent(this, TopicReply.class);
            intent.putExtra("topic", topic);
            intent.putExtra("replyto", false);
            startActivity(intent);
        }
    }

    public void refreshRecycleView(){
        //Recylcer View
        Query query = firebaseFirestore.collection("Comment").whereEqualTo("topicId", topic.getTopicId()).orderBy("dateTime", Query.Direction.DESCENDING);

        firebaseFirestore.collection("Comment").whereEqualTo("topicId", topic.getTopicId()).orderBy("dateTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()){
                    comments = task.getResult().toObjects(Comment.class);
                }
            }
        });

        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();
        adapter = new FirestoreAdapter2(options, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}