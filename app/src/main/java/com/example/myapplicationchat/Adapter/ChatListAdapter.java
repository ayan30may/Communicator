package com.example.myapplicationchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.Activity.ChatActivity;
import com.example.myapplicationchat.Display.DisplayImagePopup;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.ChatList;
import com.example.myapplicationchat.model.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private List<ChatList> list;
    private Context context;
    private boolean ischat;

    private String theLastMessage;

    public ChatListAdapter(List<ChatList> list, Context context, boolean ischat) {
        this.list = list;
        this.context = context;
        this.ischat = ischat;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ChatList chatList = list.get(position);
        holder.name.setText(chatList.getName());
        holder.message.setText(chatList.getMessage());
        holder.description.setText(chatList.getDescription());
        Glide.with(context).load(chatList.getImageProfile()).placeholder(R.drawable.ic_baseline_person_24).into(holder.circleImageView);

        if (ischat){
            lastMessage(chatList.getUserId(),holder.message);
        }else {
            holder.message.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {context.startActivity(new Intent(context, ChatActivity.class)
                    .putExtra("userId",chatList.getUserId())
                    .putExtra("name",chatList.getName())
                    .putExtra("imageProfile",chatList.getImageProfile()));
            }
        });
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DisplayImagePopup(context,chatList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView name,message,description;
        private CircleImageView circleImageView;
        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            description = itemView.findViewById(R.id.description);
            circleImageView = itemView.findViewById(R.id.profile_image);
        }
    }

    private void lastMessage(String userId,TextView message){
        theLastMessage = "default";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Chats chats = ds.getValue(Chats.class);
                    if(chats.getReceiver().equals(user.getUid()) && chats.getSender().equals(userId) ||
                            chats.getReceiver().equals(userId) && chats.getSender().equals(user.getUid())){
                        theLastMessage = chats.getTextMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        message.setText(" ");
                        break;

                    default:
                        message.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
