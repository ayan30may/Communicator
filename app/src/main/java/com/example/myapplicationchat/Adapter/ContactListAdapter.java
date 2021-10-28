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
import com.example.myapplicationchat.Display.DisplayContactsImage;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.viewHolder> {
    private List<Users> list;
    private Context context;

    public ContactListAdapter(List<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contacts,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final Users users = list.get(position);
        holder.userName.setText(users.getName());
        holder.des.setText(users.getAbout());
        Glide.with(context).load(users.getImageProfile()).placeholder(R.drawable.ic_baseline_person_24).into(holder.profileImage);
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context,ChatActivity.class)
        .putExtra("userId",users.getUserId())
        .putExtra("name",users.getName())
        .putExtra("imageProfile",users.getImageProfile())));

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DisplayContactsImage(context,users);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView userName,des;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.name);
            des = itemView.findViewById(R.id.description);
        }
    }
}
