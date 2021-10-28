package com.example.myapplicationchat.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplicationchat.Common.Common;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.Activity.UserProfile_Activity;
import com.example.myapplicationchat.databinding.ActivityDisplayImageBinding;

public class DisplayImageActivity extends AppCompatActivity {

    private ActivityDisplayImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_display_image);
        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);

        binding.back.setOnClickListener(v -> startActivity(new Intent(DisplayImageActivity.this, UserProfile_Activity.class)));
    }
}