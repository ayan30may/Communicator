package com.example.myapplicationchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplicationchat.R;
import com.example.myapplicationchat.databinding.ActivityGroupChatBinding;

public class GroupChatActivity extends AppCompatActivity {

    private ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}