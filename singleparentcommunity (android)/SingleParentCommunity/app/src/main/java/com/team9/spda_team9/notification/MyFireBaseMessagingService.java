package com.team9.spda_team9.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.team9.spda_team9.R;
import com.team9.spda_team9.forum.Topic;
import com.team9.spda_team9.forum.TopicDetails;

import java.util.Random;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private FirebaseFirestore DB;
    private static final String TAG = "MyToken New";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload" + remoteMessage.getData());
        }

        String body = remoteMessage.getData().get("Message");
        String title = remoteMessage.getData().get("Title");
        String topicId = remoteMessage.getData().get("TopicId");
        Log.d(TAG,"Message Notification Title:" + title);
        Log.d(TAG,"Message Notification Body:" + body);
        Log.d(TAG,"Topic Id:" + topicId);

        DB = FirebaseFirestore.getInstance();
        DB.collection("Topic").document(topicId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    Topic topic = task.getResult().toObject(Topic.class);
                    showNotification(title, body, topic);
                }
            }
        });

    }

    public void showNotification(String title, String data, Topic topic)
    {
        String channelID = "fcmchannel";
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(channelID,"" +
                "MyFCMNotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("This is GCM notification");
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
            builder.setAutoCancel(true)
                    .setContentText(data)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Intent tapIntent = new Intent(this, TopicDetails.class);//.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            tapIntent.putExtra("topic", topic);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.setContentIntent(pendingIntent);
            notificationManager.notify(new Random().nextInt(), builder.build());

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                    .setContentTitle(title)
                    .setContentText(data);

            Intent tapIntent = new Intent(this, TopicDetails.class);//.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            tapIntent.putExtra("topic", topic);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            builder.setContentIntent(pendingIntent);

            notificationManager.notify(0, builder.build());
        }
    }

    @Override
    public void onNewToken(@NonNull String s){
        super.onNewToken(s);

        Log.d("TokenFireBase",s);
    }
}
