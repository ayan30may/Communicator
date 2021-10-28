package com.example.myapplicationchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.Adapter.ChatAdapter;
import com.example.myapplicationchat.Adapter.UploadToFirebase;
import com.example.myapplicationchat.Display.ReviewImageSending;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.databinding.ActivityChatBinding;
import com.example.myapplicationchat.model.Chats;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private ActivityChatBinding binding;
    private FirebaseUser user;
    private String receiverId;
    private DatabaseReference reference;
    private ChatAdapter chatAdapter;
    private List<Chats> list;

    private FirebaseStorage storage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        receiverId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("name");
        String imageProfile = intent.getStringExtra("imageProfile");
        String info = intent.getStringExtra("about");
        String phone = intent.getStringExtra("phoneNumber");

        if (receiverId!=null){
            binding.username.setText(userName);
            Glide.with(this).load(imageProfile).placeholder(R.drawable.ic_baseline_person_24).into(binding.userImage);
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.message.getText().toString())){
                    binding.mic.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
                }else {
                    binding.mic.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        readChats();
        binding.mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.message.getText().toString())){
                    sendTextMessage(binding.message.getText().toString());
                    binding.message.setText("");
                }
            }
        });
        binding.videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChatActivity.this,VideoCallActivity.class);
                intent1.putExtra("userId",receiverId);
                intent1.putExtra("name",userName);
                intent1.putExtra("imageProfile",imageProfile);
                startActivity(intent1);
            }
        });
        binding.audiocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        binding.nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,UserInfoActivity.class);
                intent.putExtra("userId",receiverId);
                intent.putExtra("name",userName);
                intent.putExtra("imageProfile",imageProfile);
                intent.putExtra("about",info);
                intent.putExtra("phoneNumber",phone);
                startActivity(intent);
            }
        });

        binding.attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                startActivityForResult(galleryIntent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                if (data!=null){
                    if (data.getData()!=null){
                        imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            reviewImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    private void reviewImage(Bitmap bitmap){
        new ReviewImageSending(ChatActivity.this,bitmap).show(new ReviewImageSending.onCallBack() {
            @Override
            public void onButtonSend() {
                if (imageUri!=null){
                    final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
                    progressDialog.setMessage("Sending Image");
                    progressDialog.show();
                    new UploadToFirebase(ChatActivity.this).uploadImageToFirebaseStorage(imageUri, new UploadToFirebase.OnCallBack() {
                        @Override
                        public void onUploadSuccess(String url) {
                            Date date = Calendar.getInstance().getTime();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String today = formatter.format(date);

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            String currentTime = dateFormat.format(calendar.getTime());

                            Chats chats = new Chats(
                                    today+"."+currentTime,
                                    "",
                                    url,
                                    "IMAGE",
                                    user.getUid(),
                                    receiverId,""
                            );

                            reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Send","onSuccess");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Send","onFailure"+e.getMessage());
                                }
                            });
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ChatList").child(user.getUid()).child(receiverId);
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()){
                                        reference1.child("id").setValue(receiverId);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onUploadFailed(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.view_contact){

        }
        if (item.getItemId() == R.id.search_chat){

        }
        if (item.getItemId() == R.id.clear_chat){

        }
        return true;
    }

    private void readChats() {
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Chats chats = dataSnapshot.getValue(Chats.class);
                        chats.setMessageId(dataSnapshot.getKey());
                        if (chats!=null && chats.getSender().equals(user.getUid()) && chats.getReceiver().equals(receiverId)
                        || chats.getReceiver().equals(user.getUid()) && chats.getSender().equals(receiverId)) {
                            list.add(chats);
                            Log.d(TAG,"onDataChange: UserName "+chats.getTextMessage());
                        }
                    }
                    if (chatAdapter!=null){
                        chatAdapter.notifyDataSetChanged();
                    }else {
                        chatAdapter = new ChatAdapter(list,ChatActivity.this);
                        binding.recyclerView.setAdapter(chatAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendTextMessage(String text){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = dateFormat.format(calendar.getTime());

        Chats chats = new Chats(
                today+"."+currentTime,
                text,
                "",
                "TEXT",
                user.getUid(),
                receiverId,""
        );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Send","onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Send","onFailure"+e.getMessage());
            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ChatList").child(user.getUid()).child(receiverId);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    reference1.child("id").setValue(receiverId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}