package com.example.myapplicationchat.Display;

import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.Common.Common;
import com.example.myapplicationchat.Display.FriendsImageActivity;
import com.example.myapplicationchat.R;
import com.example.myapplicationchat.model.Users;

public class DisplayContactsImage {
    private Context context;

    public DisplayContactsImage(Context context, Users users) {
        this.context = context;
        initialize(users);
    }

    private void initialize(Users users) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_display_contacts_image);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);

        ImageView profileImage;
        TextView username;

        profileImage = dialog.findViewById(R.id.user_image);
        username = dialog.findViewById(R.id.username);

        username.setText(users.getName());
        Glide.with(context).load(users.getImageProfile()).into(profileImage);

        dialog.show();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImage.invalidate();
                Drawable drawable = profileImage.getDrawable();
                Common.IMAGE_BITMAP = ((BitmapDrawable) drawable.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, profileImage, "image");
                Intent intent = new Intent(context, FriendsImageActivity.class);
                context.startActivity(intent, activityOptionsCompat.toBundle());
            }
        });
    }
}