package com.team9.spda_team9.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team9.spda_team9.R;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button updateBtn;
    private TextView newPassword;
    private TextView confirmPassword;
    private TextView oldPassword;

//    private AwesomeValidation awesomeValidation;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);

        updateBtn = findViewById(R.id.button_update);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.password_confirm);
        oldPassword = findViewById(R.id.old_password);

        mAuth = FirebaseAuth.getInstance();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String newPass=newPassword.getText().toString();
//                String confir=confirmPassword.getText().toString();
                changePassword();
            }
        });

    }

    public void changePassword() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String newPass = newPassword.getText().toString();
        String oldPass = oldPassword.getText().toString();
        String confirm = confirmPassword.getText().toString();

        if (ValidationUtil.passwordValidation(oldPass) &&
                ValidationUtil.passwordValidation(newPass) &&
                ValidationUtil.passwordValidation(confirm)) {

            if (oldPassword.getText().toString().isEmpty() &&
                    newPassword.getText().toString().isEmpty() &&
                    confirmPassword.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_LONG).show();
            }
            if (oldPassword.getText().toString().equals(newPassword.getText().toString())) {
                Toast.makeText(this, "NewPassword and OldPassword are the same.Please enter different password. ", Toast.LENGTH_LONG).show();
            } else if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    // User is signed in

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail().toString(), oldPassword.getText().toString());

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
//                                        DocumentReference documentReference1=db.collection("User").document(firebaseUser.getUid());
//                                        Map<String,Object> edited=new HashMap<>();
//                                        edited.put("password",newPassword.getText().toString());
//                                        documentReference1.update(edited);
                                                               FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                                                       user.updatePassword(newPassword.getText().toString())
                                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                                       if (task.isSuccessful()) {


//                                                                    Toast.makeText(ChangePassword.this, "Profile changed successfully", Toast.LENGTH_SHORT).show();
                                                                                           User myUser = new User();

                                                                                           mAuth.signOut();
//                                                                    startActivity(new Intent(ChangePassword.this, Login.class));
//                                                                    finish();
                                                                                       }
                                                                                   }
                                                                               });
                                                                   }
                                                               });
                                                               Toast.makeText(ChangePassword.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                                                               startActivity(new Intent(ChangePassword.this, Login.class)
                                                                       .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                           } else {
                                                               Toast.makeText(ChangePassword.this, "Old Password Incorrect", Toast.LENGTH_LONG).show();
                                                           }
                                                       }
                                                   }
                            );
                }

            } else {
                Toast.makeText(ChangePassword.this, "Password mismatch ", Toast.LENGTH_LONG).show();
            }

        } else {
            if (!ValidationUtil.passwordValidation(oldPass)) {
                oldPassword.setError("Please enter the old Password. ");
            }
            if (!ValidationUtil.passwordValidation(newPass)) {
                newPassword.setError("Password must include upperCase, lowerCase, symbols and numbers.");
            }
            if (!ValidationUtil.passwordValidation(confirm)) {
                confirmPassword.setError("Password must include upperCase, lowerCase, symbols and numbers.");
            }
        }
    }
}




