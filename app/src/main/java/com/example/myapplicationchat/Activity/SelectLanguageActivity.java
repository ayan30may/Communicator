package com.example.myapplicationchat.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.myapplicationchat.Adapter.AppCompat;
import com.example.myapplicationchat.Adapter.LanguageManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplicationchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SelectLanguageActivity extends AppCompat {

    private ImageButton english;
    private ImageButton hindi;
    private ImageButton urdu;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        user = FirebaseAuth.getInstance().getCurrentUser();

        english = findViewById(R.id.eng);
        hindi = findViewById(R.id.hin);
        urdu = findViewById(R.id.ur);
        LanguageManager languageManager = new LanguageManager(this);

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.updateResources("hi");
                recreate();
                startActivity(new Intent(SelectLanguageActivity.this,login_activity.class));
                finish();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.updateResources("en");
                recreate();
                startActivity(new Intent(SelectLanguageActivity.this,login_activity.class));
                finish();
            }
        });

        urdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.updateResources("ur");
                recreate();
                startActivity(new Intent(SelectLanguageActivity.this,login_activity.class));
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (user!=null){
            startActivity(new Intent(SelectLanguageActivity.this,home_activity.class));
            finish();
        }
    }
}