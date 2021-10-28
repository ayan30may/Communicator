package com.example.myapplicationchat.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplicationchat.Activity.UserProfile_Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UploadToFirebase {
    private Context context;

    public UploadToFirebase(Context context) {
        this.context = context;
    }

    public void uploadImageToFirebaseStorage(Uri imageUri, OnCallBack onCallBack){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImagesChats/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                String sdownloadUrl = String.valueOf(downloadUrl);

                onCallBack.onUploadSuccess(sdownloadUrl);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCallBack.onUploadFailed(e);
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public interface OnCallBack{
        void onUploadSuccess(String url);
        void onUploadFailed(Exception e);

    }
}
