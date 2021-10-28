package com.example.myapplicationchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.myapplicationchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startActivity(new Intent(SplashActivity.this,SelectLanguageActivity.class));
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (user!=null){
            startActivity(new Intent(SplashActivity.this,home_activity.class));
            finish();
        }
    }
}