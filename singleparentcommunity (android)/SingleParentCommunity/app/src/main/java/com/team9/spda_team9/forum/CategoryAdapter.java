package com.team9.spda_team9.forum;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9.spda_team9.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<String> categories;
    private Context context;
    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private OnItemClickListener mListener;
    private String userId;
    private String category = "";
    private Subscription userSub;
    private final String TAG = CategoryAdapter.class.getSimpleName();

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public CategoryAdapter(ArrayList<String> categories, Context context) {
        this.categories = categories;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fragment,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        final String cat = categories.get(position);
        holder.titleTextView.setText(categories.get(position));
        userId = FirebaseAuth.getInstance().getUid();
        userSub = null;

        DB.collection("Subscription").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Subscription> allSubs = task.getResult().toObjects(Subscription.class);
                if (allSubs != null && allSubs.size() != 0) {
                    List<Subscription> result = allSubs.stream().filter(x -> x.getUserId().equals(userId)).collect(Collectors.toList());
                    String enumCat = getCategoryByPosition(position);

                    if (result != null && result.size() != 0) {
                        //will only retrieve one result if there is a result as userId search is unique
                        userSub = result.get(0);

                        if (userSub.getSubscription().contains(Enum.valueOf(Category.class, enumCat))) {
                            holder.subscribe.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                            holder.subscribe.setText("SUBSCRIBED");
                        }
                    }
                }
            }
        });

        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + position);
                String sub = holder.subscribe.getText().toString();
                DB = FirebaseFirestore.getInstance();

                category = getCategoryByPosition(position);

                //retrieving record from DB, or create one if not available
                if (userSub != null){
                    if (sub.equals("SUBSCRIBE")) {
                        userSub.addSubscription(Enum.valueOf(Category.class, category));
                        holder.subscribe.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                        holder.subscribe.setText("SUBSCRIBED");
                    } else if (sub.equals("SUBSCRIBED")) {
                        userSub.removeSubscription(Enum.valueOf(Category.class, category));
                        holder.subscribe.setBackgroundColor(Color.parseColor("#FFF57C00"));
                        holder.subscribe.setText("SUBSCRIBE");
                    }

                    if (userSub.getSubscription().size() == 0) {
                        DB.collection("Subscription").document(userId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "deleted " + userId + " from sub");
                            }
                        });
                    } else {
                        DB.collection("Subscription").document(userId).set(userSub).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "updated sub: " + cat);
                            }
                        });
                    }
                } else {
                    userSub = new Subscription();
                    userSub.setUserId(userId);
                    userSub.addSubscription(Enum.valueOf(Category.class, category));
                    holder.subscribe.setBackgroundColor(Color.parseColor("#FF03DAC5"));
                    holder.subscribe.setText("SUBSCRIBED");

                    DB.collection("Subscription").document(userId).set(userSub).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "subscribed/unsubscribed to: " + cat);

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        Button subscribe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String category = null;

                    category = getCategoryByPosition(position);

                    Intent topicDisplay = new Intent(itemView.getContext(), TopicDisplay.class);
                    topicDisplay.putExtra("category", category);
                    itemView.getContext().startActivity(topicDisplay);

                }
            });

            titleTextView = itemView.findViewById(R.id.titleTextView);
            subscribe = itemView.findViewById(R.id.subscribeBtn);
        }
    }

    public String getCategoryByPosition(int position){
        String result = null;

        if(position==0) {
            result = "DatingAndRelationships";
        } else if (position==1) {
            result = "Teenagers";
        } else if (position==2) {
            result = "Parenting";
        } else if (position==3) {
            result = "ChildCare";
        } else if (position==4) {
            result = "Finances";
        } else if (position==5) {
            result = "MentalHealthSupport";
        }

        return result;
    }

}
