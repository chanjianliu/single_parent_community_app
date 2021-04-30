package com.team9.spda_team9.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.Authentication.User;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    private UserAdapter.OnListItemClick onListItemClick;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private String userId;
    private User user;
    private String friendID;
    private List<String> friends;

    private String TAG = UserAdapter.class.getSimpleName();

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User>options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

        holder.name.setText(model.getUsername());
        holder.location.setText(model.getLocation());

        userId = FirebaseAuth.getInstance().getUid();

        DB.collection("User").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                user = task.getResult().toObject(User.class);
                friends = user.getFriends();

                if (friends != null){
                    if (friends.contains(model.getUsername())) {
                        holder.button.setBackgroundColor(R.color.orange);
                        holder.button.setText("-");
                    }
                    else {
                        holder.button.setBackgroundColor(R.color.teal_700);
                        holder.button.setText("+");
                    }
                }
                else{
                    holder.button.setBackgroundColor(R.color.teal_700);
                    holder.button.setText("+");

                }

            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "button for" + position + " " + model.getUsername());
                String buttonTag = holder.button.getText().toString();
                DB.collection("User").whereIn("username", Arrays.asList(model.getUsername(), user.getUsername())).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> bothUsers = task.getResult().getDocuments();

                        if (task.getResult().getDocuments() != null && task.getResult().getDocuments().size() != 0 && task.isSuccessful()) {
                            for (DocumentSnapshot d : bothUsers) {
                                if(!d.getId().equals(userId)) {
                                    friendID = d.getId();
                                }
                            }

                            if (buttonTag.equals("-")){
                                //updating currentUser's friend list
                                List<String> oldFriend = user.getFriends();
                                oldFriend.remove(model.getUsername());
                                DB.collection("User").document(userId).update("friends", oldFriend);

                                //updating currentUser's friend's friend list
                                oldFriend = model.getFriends();
                                oldFriend.remove(user.getUsername());
                                DB.collection("User").document(friendID).update("friends", oldFriend);


                                holder.button.setBackgroundColor(R.color.orange);
                                holder.button.setText("+");



                            } else {
                                //updating currentUser's friend list
                                List<String> newFriend = user.getFriends();
                                if (newFriend == null) {
                                    newFriend = new ArrayList<>();
                                }
                                newFriend.add(model.getUsername());
                                DB.collection("User").document(userId).update("friends", newFriend);

                                //updating currentUser's friend's friend list
                                newFriend = model.getFriends();
                                if (newFriend == null) {
                                    newFriend = new ArrayList<>();
                                }
                                newFriend.add(user.getUsername());
                                DB.collection("User").document(friendID).update("friends", newFriend);

                                holder.button.setBackgroundColor(R.color.teal_700);
                                holder.button.setText("-");
                            }
                        }
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friend, parent, false);
        return new UserViewHolder(view);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name, location;
        private Button button;

        public UserViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            button = itemView.findViewById(R.id.addFriendBtn);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) { onListItemClick.onItemClick(getAdapterPosition()); }

    }

    public interface OnListItemClick{
        void onItemClick(int position);
    }
}
