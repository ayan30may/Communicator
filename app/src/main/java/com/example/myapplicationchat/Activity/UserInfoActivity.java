package com.example.myapplicationchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.R;

public class UserInfoActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView userName, activeTime, about, phoneNumber;
    private String receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.username);
        activeTime = findViewById(R.id.last_active);
        about = findViewById(R.id.user_about);
        phoneNumber = findViewById(R.id.phone_number);

        Intent intent = getIntent();
        receiverId = intent.getStringExtra("userId");
        String name = intent.getStringExtra("name");
        String imageProfile = intent.getStringExtra("imageProfile");
        String info = intent.getStringExtra("about");
        String phone = intent.getStringExtra("phoneNumber");

        if (receiverId!=null){
            userName.setText(name);
            Glide.with(this).load(imageProfile).placeholder(R.drawable.ic_baseline_person_24).into(profileImage);
            about.setText(info);
            phoneNumber.setText(phone);
        }
    }
}