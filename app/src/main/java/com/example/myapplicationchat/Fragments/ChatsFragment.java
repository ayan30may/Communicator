package com.example.myapplicationchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.ChatList;
import com.example.myapplicationchat.Adapter.ChatListAdapter;
import com.example.myapplicationchat.databinding.FragmentChatsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";

    public ChatsFragment() {
    }

    private FragmentChatsBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private ChatListAdapter adapter;
    private Handler handler = new Handler();

    private List<ChatList>list;
    private ArrayList<String> mUsers;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);

        list = new ArrayList<>();
        mUsers = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(list,getContext(),true);
        binding.recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (user!=null){
            getChats();
        }
        return binding.getRoot();
    }

    private void getChats() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        databaseReference.child("ChatList").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                mUsers.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    userId = ds.child("id").getValue().toString();
                    Log.d(TAG, "onDataChange: userid" + userId);

                    binding.progressCircular.setVisibility(View.GONE);
                    mUsers.add(userId);
                }
                if (userId!=null){
                    getUserInfo();
                }else {
                    Log.e(TAG,"Error");
                    binding.progressCircular.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,"Error"+error.getMessage());
            }
        });
    }
    private void getUserInfo(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                firestore.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            ChatList chatList = new ChatList(
                                    documentSnapshot.getString("userId"),
                                    documentSnapshot.getString("name"),
                                    "",
                                    "",
                                    documentSnapshot.getString("imageProfile")
                            );
                            list.add(chatList);
                        } catch (Exception e) {
                            Log.e(TAG, "onSuccess" + e.getMessage());
                        }

                        if (adapter!=null){
                            adapter.notifyItemInserted(0);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG,"onSuccess: adapter "+adapter.getItemCount());
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure: "+e.getMessage());
                    }
                });
            }
        });
    }
}