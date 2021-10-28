package com.example.myapplicationchat.Display;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplicationchat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;

public class ReviewImageSending {
    private Context context;
    private Dialog dialog;
    private Bitmap bitmap;
    private ZoomageView image;
    private FloatingActionButton fab;

    public ReviewImageSending(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
        this.dialog = new Dialog(context);
        initialize();
    }
    public void initialize(){
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_review_sending_image);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        image = dialog.findViewById(R.id.imageView);
        fab = dialog.findViewById(R.id.send);
    }
    public void show(onCallBack onCallBack){
        dialog.show();
        image.setImageBitmap(bitmap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonSend();
                dialog.dismiss();
            }
        });
    }
    public interface onCallBack{
        void onButtonSend();
    }
}
