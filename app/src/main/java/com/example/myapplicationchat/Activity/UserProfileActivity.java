package com.example.myapplicationchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

     private static final String TAG ="UserProfileActivity" ;
     EditText name;
     Button next;
     FirebaseFirestore firestore;
     FirebaseUser firebaseUser;
     ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.name_edittext);
        next = findViewById(R.id.next_button);
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);

        firestore.collection("users").document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    name.setText(task.getResult().getString("name"));
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = name.getText().toString();
                if (TextUtils.isEmpty(txt_name)){
                    name.setError("Please Enter Your Name");
                }else {
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    if (firebaseUser!=null){
                        String userId = firebaseUser.getUid();
                        Users users = new Users(
                                txt_name,
                                "",
                                userId,
                                "",
                                firebaseUser.getPhoneNumber()
                        );
                        firestore.collection("users").document(firebaseUser.getUid()).set(users)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(UserProfileActivity.this, "Go to Three dots-> Settings-> Update your Profile", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UserProfileActivity.this,home_activity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Please Login!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}