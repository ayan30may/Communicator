package com.example.myapplicationchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.Adapter.AppCompat;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class settings_activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayout userProfile;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private TextView name,about;
    private CircleImageView profileImage;
    private LinearLayout delete;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    ActivitySettingsBinding settingsBinding;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme_MyApplicationChat);
        }else{
            setTheme(R.style.Theme_MyApplicationChat);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImage = findViewById(R.id.profile_image);
        aSwitch = findViewById(R.id.switch_btn);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");

        name = findViewById(R.id.name);
        about = findViewById(R.id.status);
        delete = findViewById(R.id.delete_layout);
        progressBar = findViewById(R.id.progress_bar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        userProfile = findViewById(R.id.user_profile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(settings_activity.this,UserProfile_Activity.class));
            }
        });

        if(user!=null){
            firestore.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String userName = documentSnapshot.get("name").toString();
                            name.setText(userName);
                            String profileimage = documentSnapshot.getString("imageProfile");
                            Glide.with(settings_activity.this).load(profileimage).placeholder(R.drawable.ic_baseline_person_24).into(profileImage);
                            String userAbout = documentSnapshot.get("about").toString();
                            about.setText(userAbout);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Get Data","On Faliure" +e.getMessage());
                }
            });
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }
}