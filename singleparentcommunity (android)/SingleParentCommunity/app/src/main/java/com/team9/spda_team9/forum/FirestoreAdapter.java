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

public class FirestoreAdapter extends FirestoreRecyclerAdapter<Topic, FirestoreAdapter.TopicViewHolder> {

    private OnListItemClick onListItemClick;

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<Topic> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull TopicViewHolder holder, int position, @NonNull Topic model) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



        holder.list_topicTitle.setText(model.getTitle());
        holder.list_topicPostedBy.setText(model.getUsername()+"");

        //setting up unix to dateTime then String for display
        Date date = new Date(Long.parseLong(model.getDateTime()));
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Singapore"));
        String formattedDate = sdf.format(date);
        holder.list_topicPostedOn.setText(formattedDate);
//        holder.list_topicPostedOn.setText(model.getDateTime().format(formatter));

        holder.list_topicCategory.setText(model.getCategory() + "");


    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_topic_single, parent, false);
        return new TopicViewHolder(view);
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView list_topicTitle, list_topicPostedBy, list_topicPostedOn, list_topicCategory;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);

            list_topicTitle=itemView.findViewById(R.id.list_topicTitle);
            list_topicPostedBy=itemView.findViewById(R.id.list_topicPostedBy);
            list_topicPostedOn=itemView.findViewById(R.id.list_topicPostedOn);
            list_topicCategory=itemView.findViewById(R.id.list_topicCategory);

            itemView.setOnClickListener(this);

        }




        @Override
        public void onClick(View v) {

            onListItemClick.onItemClick(getAdapterPosition());


        }
    }

    public interface OnListItemClick{
        void onItemClick(int position);

    }

}
