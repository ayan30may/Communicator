 package com.example.myapplicationchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewHolder> {
    private List<Chats> list;
    private Context context;

    public ChatAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser user;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_left,parent,false);
            return new viewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_right,parent,false);
            return new viewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;
        private ImageView messageImage;
        private LinearLayout textLayout, imageLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.text_message);
            messageImage = itemView.findViewById(R.id.chat_image);
            textLayout = itemView.findViewById(R.id.text_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
        }

        public void bind(Chats chats) {
            switch (chats.getType()){
                case "TEXT":
                    textLayout.setVisibility(View.VISIBLE);
                    imageLayout.setVisibility(View.GONE);

                    textMessage.setText(chats.getTextMessage());
                    break;

                case "IMAGE":
                    textLayout.setVisibility(View.GONE);
                    imageLayout.setVisibility(View.VISIBLE);

                    Glide.with(context).load(chats.getImageUrl()).into(messageImage);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
