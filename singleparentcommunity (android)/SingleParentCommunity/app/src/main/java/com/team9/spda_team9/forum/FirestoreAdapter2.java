package com.team9.spda_team9.forum;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.team9.spda_team9.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FirestoreAdapter2 extends FirestoreRecyclerAdapter<Comment, FirestoreAdapter2.CommentViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapter2(@NonNull FirestoreRecyclerOptions<Comment> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        holder.list_commentPostedBy.setText(model.getUsername() + "");

        //setting up unix to dateTime then String for display
        Date date = new Date(Long.parseLong(model.getDateTime()));
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Singapore"));
        String formattedDate = sdf.format(date);
        holder.list_commentPostedOn.setText(formattedDate);
//        holder.list_commentPostedOn.setText(model.getDateTime().format(formatter));

        holder.list_commentBody.setText(model.getBody() + "");
        holder.list_commentReplyTo.setText(model.getReplyTo());

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_single, parent, false);
        return new CommentViewHolder(view);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView list_commentPostedBy, list_commentPostedOn, list_commentBody, list_commentReplyTo;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            list_commentPostedBy = itemView.findViewById(R.id.list_commentPostedBy);
            list_commentPostedOn = itemView.findViewById(R.id.list_commentPostedOn);
            list_commentBody = itemView.findViewById(R.id.list_commentBody);
            list_commentReplyTo = itemView.findViewById(R.id.list_commentReplyTo);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getAdapterPosition());

        }
    }

    public interface OnListItemClick {
        void onItemClick(int position);

    }


}
