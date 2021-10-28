package com.example.myapplicationchat.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplicationchat.Common.Common;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.databinding.ActivityFriendsImageBinding;
import com.example.myapplicationchat.Activity.home_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendsImageActivity extends AppCompatActivity {
    private ActivityFriendsImageBinding binding;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_image);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_friends_image);
        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendsImageActivity.this, home_activity.class));
            }
        });
    }
}