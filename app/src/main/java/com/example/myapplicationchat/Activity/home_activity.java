package com.example.myapplicationchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplicationchat.Adapter.TopStatusAdapter;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.Adapter.TabsAccessoriesAdapter;
import com.example.myapplicationchat.model.Status;
import com.example.myapplicationchat.model.UserStatus;
import com.example.myapplicationchat.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;

public class home_activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabsAccessoriesAdapter tabsAccessoriesAdapter;
    private FirebaseAuth mAuth;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    RecyclerView statusList;
    FloatingActionButton fab1;
    ProgressDialog progressDialog;
    Users users;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        statusList = findViewById(R.id.story_bar);
        fab1 = findViewById(R.id.add_status);
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        userStatuses = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.setCancelable(false);

        statusAdapter = new TopStatusAdapter(this,userStatuses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        statusList.setLayoutManager(layoutManager);
        statusList.setAdapter(statusAdapter);

        mToolbar = findViewById(R.id.status_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My chats");

        mViewPager = findViewById(R.id.tabs_pager);
        tabsAccessoriesAdapter = new TabsAccessoriesAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabsAccessoriesAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mAuth=FirebaseAuth.getInstance();

        firestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                            }
                        }
                    }
                });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,75);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            if(data.getData()!=null){
                progressDialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference storageReference = storage.getReference().child("status").child(date.getTime()+" ");

                storageReference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(users.getName());
                                    userStatus.setProfileImage(users.getImageProfile());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("name",userStatus.getName());
                                    map.put("profileImage",userStatus.getProfileImage());
                                    map.put("lastUpdated",userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl,userStatus.getLastUpdated());

                                    database.getReference().child("stories").child(FirebaseAuth.getInstance().getUid()).updateChildren(map);

                                    database.getReference().child("stories").child(FirebaseAuth.getInstance().getUid()).child("statuses").push()
                                            .setValue(status);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.new_group){
            startActivity(new Intent(home_activity.this,GroupChatActivity.class));
            finish();
        }
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(home_activity.this,settings_activity.class));
            finish();
        }
        if (item.getItemId() == R.id.log_out){
            mAuth.signOut();
            startActivity(new Intent(home_activity.this,login_activity.class));
            finish();
        }
        return true;
    }
}