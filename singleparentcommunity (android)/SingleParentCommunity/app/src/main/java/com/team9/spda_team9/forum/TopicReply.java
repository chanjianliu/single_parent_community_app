package com.team9.spda_team9.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.R;
import com.team9.spda_team9.notification.APIService;
import com.team9.spda_team9.notification.Client;
import com.team9.spda_team9.notification.Data;
import com.team9.spda_team9.notification.MyResponse;
import com.team9.spda_team9.notification.NotificationSender;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicReply extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore DB;
    Button button;
    Topic topic;
    Comment newComment,comment;
    String replyToId, replyToUserId;
    EditText et;
    final String ReplyToComment = "ReplyToComment";
    final String ReplyToTopic = "ReplyToTopic";
    Boolean replyToComment = false;
    private APIService apiService;
    String username;
    String originMode;

    private static final String TAG = Topic_Fragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_reply);
        DB = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        topic = (Topic) intent.getSerializableExtra("topic");

        if(intent.getBooleanExtra("replyto",false)) { //replying to comment
            comment = (Comment) intent.getSerializableExtra("comment");
            replyToComment = true;
            replyToUserId = comment.getUserId();
            replyToId = comment.getUsername();
            originMode = "comment";

            TextView tv1 = findViewById(R.id.topicTitle);
            TextView tv2 = findViewById(R.id.topicPostedBy);
            TextView tv3 = findViewById(R.id.topicPostedOn);
            TextView tv4 = findViewById(R.id.topicBody);
            TextView tv5 = findViewById(R.id.topicCategory);
            TextView tv6 = findViewById(R.id.topicNoComments);
            et = findViewById(R.id.comment);
            tv1.setText("Comment on: " + topic.getTitle());
            tv2.setText(comment.getUsername());

            //setting up unix to dateTime then String for display
            Date date = new Date(Long.parseLong(comment.getDateTime()));
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Singapore"));
            String formattedDate = sdf.format(date);
            tv3.setText(formattedDate);

            tv4.setText(comment.getBody());
            tv5.setText(topic.getCategory().toString());
        } else {
            replyToUserId = topic.getUserId();
            replyToId = topic.getUsername();
            originMode = "topic";

            TextView tv1 = findViewById(R.id.topicTitle);
            TextView tv2 = findViewById(R.id.topicPostedBy);
            TextView tv3 = findViewById(R.id.topicPostedOn);
            TextView tv4 = findViewById(R.id.topicBody);
            TextView tv5 = findViewById(R.id.topicCategory);
            TextView tv6 = findViewById(R.id.topicNoComments);
            et = findViewById(R.id.comment);
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
        }

        button = findViewById(R.id.submit);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == button){
            newComment = new Comment();

            //getting the epoch milli of current time
            long dt = System.currentTimeMillis();
            newComment.setDateTime(Long.toString(dt));

            newComment.setBody(et.getText().toString());
            newComment.setUserId(FirebaseAuth.getInstance().getUid());
            newComment.setTopicId(topic.getTopicId());
            newComment.setReplyTo(replyToId);

            SharedPreferences userDetail = getSharedPreferences("user", Context.MODE_PRIVATE);
            username = userDetail.getString("username", "anonymous");
            newComment.setUsername(username);

            //sending notification to receiver
            apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);
            DB.collection("User").document(replyToUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String userToken = task.getResult().toObject(User.class).getToken();
                        sendNotifications(userToken, username + " replied to your " + originMode + "!", newComment.getBody(), topic.getTopicId());
                        Toast.makeText(TopicReply.this, "notification sent", Toast.LENGTH_LONG).show();
                    }
                }
            });
            UpdateToken();

            //saving into DB
            DB.collection("Comment").add(newComment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    DB.collection("Comment").document(documentReference.getId()).update("commentId", documentReference.getId());
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
            //Toast.makeText(this,"Successful",Toast.LENGTH_LONG);
            finish();
        }
    }

    private void UpdateToken() {

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()){
                    String token = task.getResult().getToken();
                    String userId = FirebaseAuth.getInstance().getUid();
                    FirebaseFirestore.getInstance().collection("User").document(userId).update("token", token);
                    Log.d("MyToken",token);
                }
            }
        });
    }

    public void sendNotifications(String usertoken, String title, String message, String topicId) {

        Data data = new Data(title, message, topicId);
        NotificationSender sender = new NotificationSender(data, usertoken);

        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(TopicReply.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(TopicReply.this, "Failed ", Toast.LENGTH_LONG);
            }
        });
    }



}