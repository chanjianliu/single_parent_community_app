package com.team9.spda_team9.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.Authentication.Gender;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserProfileDetails extends AppCompatActivity implements View.OnClickListener, UserAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    Button button;
    User user;
    User currentUser;
    private String currentUserId;
    private String friendID;
    boolean isFriend;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        SharedPreferences userDetail = getSharedPreferences("user", Context.MODE_PRIVATE);
        username = userDetail.getString("username", "error");

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        isFriend = (Boolean) intent.getBooleanExtra("isFriend",false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getUid();

        TextView displayname = findViewById(R.id.fullnameProfile);
        ImageView imageView = findViewById(R.id.imageView9);
        TextView noofkids  = findViewById(R.id.noofkids);
        TextView profession = findViewById(R.id.professionProfile);
        TextView location = findViewById(R.id.location1);
        TextView aboutMe = findViewById(R.id.textView27);
        TextView aboutChildren = findViewById(R.id.textView31);
        TextView hobbiesInterest = findViewById(R.id.textView33);
        button = findViewById(R.id.addfriend);

        //set the text from user profile

        if (user.getGender() == Gender.female) {

           int imageResource = getResources().getIdentifier("@drawable/ic_woman", null, getPackageName());
           imageView = findViewById(R.id.imageView9);
           Drawable res=getResources().getDrawable(imageResource);
           imageView.setImageDrawable(res);
        }
        if (user.getGender() == Gender.male) {

            int imageResource = getResources().getIdentifier("@drawable/ic_man", null, getPackageName());
            imageView = findViewById(R.id.imageView9);
            Drawable res=getResources().getDrawable(imageResource);
            imageView.setImageDrawable(res);
        }

        String kids = String.valueOf(user.getNumberOfKids());
        displayname.setText(user.getFullName());
        profession.setText(user.getProfession());
        location.setText(user.getLocation());
        noofkids.setText(kids);
        aboutMe.setText(user.getSelfDescription());
        aboutChildren.setText(user.getKidsDescription());
        hobbiesInterest.setText(user.getInterest());
        if(isFriend){
            button.setText("Remove Friend");
        }
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        firebaseFirestore.collection("User").whereIn("username", Arrays.asList(user.getUsername(), username)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> bothUsers = task.getResult().getDocuments();

                if (task.getResult().getDocuments() != null && task.getResult().getDocuments().size() != 0 && task.isSuccessful()) {
                    for (DocumentSnapshot d : bothUsers) {
                        if(!d.getId().equals(currentUserId)) {
                            friendID = d.getId();
                        } else {
                            currentUser = d.toObject(User.class);
                        }
                    }

                    if (isFriend){
                        //updating currentUser's friend list
                        List<String> oldFriend = currentUser.getFriends();
                        oldFriend.remove(user.getUsername());
                        firebaseFirestore.collection("User").document(currentUserId).update("friends", oldFriend);

                        //updating currentUser's friend's friend list
                        oldFriend = user.getFriends();
                        oldFriend.remove(username);
                        firebaseFirestore.collection("User").document(friendID).update("friends", oldFriend);

                        changeStatus();
                    } else {
                        //updating currentUser's friend list
                        List<String> newFriend = currentUser.getFriends();
                        if (newFriend == null) {
                            newFriend = new ArrayList<>();
                        }
                        newFriend.add(user.getUsername());
                        firebaseFirestore.collection("User").document(currentUserId).update("friends", newFriend);

                        //updating currentUser's friend's friend list
                        newFriend = user.getFriends();
                        if (newFriend == null) {
                            newFriend = new ArrayList<>();
                        }
                        newFriend.add(username);
                        firebaseFirestore.collection("User").document(friendID).update("friends", newFriend);
                        changeStatus();
                    }
                }
            }
        });
    }

    public void changeStatus(){
        if(isFriend){
            CharSequence text = "User successfully removed from your Friends";
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            button.setText("Add Friend");
            isFriend = !isFriend;
        }else{
            CharSequence text = "User successfully added to your Friends";
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            button.setText("Remove Friend");
            isFriend = !isFriend;
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}
