package com.team9.spda_team9.Authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.team9.spda_team9.R;

import java.util.HashMap;
import java.util.Map;

public class ChangeFullName extends AppCompatActivity {

    private static final String TAG = " ";
    EditText fullname;
    Button update;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String userId;
    String NAME = null;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.fullname_change);

        fullname = findViewById(R.id.fullnameChange);
        update = findViewById(R.id.updatefullname);

//        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();

//
//       FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


        DocumentReference documentReference = db.collection("User").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fullname.setText(value.getString("fullName"));
                fullname.setSelection(value.getString("fullName").length());
            }


        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fullname.getText().toString().isEmpty()) {
                    Toast.makeText(ChangeFullName.this, "Full Name Field should not be Empty", Toast.LENGTH_LONG).show();
                }else{


                v.setEnabled(false);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullname.getText().toString())
                            .build();


                    firebaseUser.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DocumentReference documentReference1 = db.collection("User").document(firebaseUser.getUid());
                            Map<String, Object> edited = new HashMap<>();
                            edited.put("fullName", fullname.getText().toString());
                            documentReference1.update(edited);
                            v.setEnabled(true);
                            Toast.makeText(ChangeFullName.this, "Full Name changed successfully", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            v.setEnabled(true);
                            Toast.makeText(ChangeFullName.this, "Full Name change Unsuccessfull", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

        });

    }
}

