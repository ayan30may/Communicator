package com.example.myapplicationchat.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplicationchat.Adapter.ContactListAdapter;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactsFragment extends Fragment {
    private static final Object TAG = "ContactsFragment";

    public ContactsFragment() {
    }

    private ContactListAdapter contactListAdapter;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private List<Users> list;

    public static final int REQUEST_READ_CONTACTS = 79;
    private ListView contactlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();

        if(user!=null)
            getAllContacts();
        return view;
    }

    private void getAllContacts() {
        firestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                    String userId = snapshot.getString("userId");
                    String name = snapshot.getString("name");
                    String imageProfile = snapshot.getString("imageProfile");
                    String about = snapshot.getString("about");
                    String phone = snapshot.getString("phoneNumber");

                    Users users = new Users();
                    users.setUserId(userId);
                    users.setName(name);
                    users.setAbout(about);
                    users.setImageProfile(imageProfile);
                    users.setPhoneNumber(phone);

                    if (userId!=null && !userId.equals(user.getUid())){
                            list.add(users);

                    }
                }
                contactListAdapter = new ContactListAdapter(list,getActivity());
                recyclerView.setAdapter(contactListAdapter);
            }
        });
    }
}