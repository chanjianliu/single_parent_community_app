package com.team9.spda_team9.forum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9.spda_team9.Authentication.Login;
import com.team9.spda_team9.Authentication.ValidationUtil;
import com.team9.spda_team9.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Topic_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Topic_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private Button button;
    private Spinner spinner;
    private EditText mEditTextTitle, mEditTextBody;
    private long dt;
    private Topic topic;
    private Category category;

    private static final String TAG = Topic_Fragment.class.getSimpleName();

    public Topic_Fragment() {
        // Required empty public constructor
    }

    public static Topic_Fragment newInstance() {
        Topic_Fragment fragment = new Topic_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_topic_new, container, false);
        Spinner spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        db = FirebaseFirestore.getInstance();

        mEditTextTitle = root.findViewById(R.id.title);
        mEditTextBody = root.findViewById(R.id.body);
        dt = System.currentTimeMillis();
        button = root.findViewById(R.id.button);
        topic = new Topic();

        button.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {

        String title = mEditTextTitle.getText().toString().trim();
        String body = mEditTextBody.getText().toString().trim();

        if (ValidationUtil.notEmptyValidation(title)
                && ValidationUtil.notEmptyValidation(body)) {
            topic.setTitle(title);
            topic.setBody(body);
            topic.setDateTime(Long.toString(dt));
            topic.setCategory(category);
            topic.setUserId(FirebaseAuth.getInstance().getUid());

            //for better display purpose
            SharedPreferences userDetail = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = userDetail.getString("username", "anonymous");
            topic.setUsername(username);

            db.collection("Topic").add(topic).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    db.collection("Topic").document(documentReference.getId()).update("topicId", documentReference.getId());
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });

            Intent intent = new Intent(Topic_Fragment.this.getContext(), TopicDetails.class);
            intent.putExtra("topic", topic);
            startActivity(intent);

            mEditTextTitle.setText("");
            mEditTextBody.setText("");
        } else {
            if (!ValidationUtil.notEmptyValidation(title)) {
                mEditTextTitle.setError("Please enter a title.");
            }
            if (ValidationUtil.notEmptyValidation(body)) {
                mEditTextBody.setError("Please enter something.");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = Category.values()[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}