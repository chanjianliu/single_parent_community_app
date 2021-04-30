package com.team9.spda_team9.forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.team9.spda_team9.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class personalised extends FirestoreRecyclerAdapter<Topic, personalised.ViewHolder> {
    private personalised.OnListItemClick onListItemClick;

    private personalised.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(personalised.OnItemClickListener listener) {
        mListener = listener;
    }

    public personalised(@NonNull FirestoreRecyclerOptions<Topic> options, personalised.OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @NonNull
    @Override
    public personalised.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_topic_single,
                parent,false);
        return new personalised.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull personalised.ViewHolder holder, int position, @NonNull Topic model) {
        holder.list_topicTitle.setText(model.getTitle());
        holder.list_topicPostedBy.setText(model.getUsername()+"");

        //setting up unix to dateTime then String for display
        Date date = new Date(Long.parseLong(model.getDateTime()));
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Singapore"));
        String formattedDate = sdf.format(date);
        holder.list_topicPostedOn.setText(formattedDate);

        holder.list_topicCategory.setText(model.getCategory() + "");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView list_topicTitle, list_topicPostedBy, list_topicPostedOn, list_topicCategory;

        public ViewHolder(@NonNull View itemView) {
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

    public interface OnListItemClick {
        void onItemClick(int position);
    }
}