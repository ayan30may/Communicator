package com.example.myapplicationchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplicationchat.BuildConfig;
import com.example.myapplicationchat.Common.Common;
import com.example.myapplicationchat.Display.DisplayImageActivity;
import com.example.myapplicationchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile_Activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView name,phone,about;
    private FirebaseUser user;

    private FirebaseFirestore firestore;
    private BottomSheetDialog bottomSheetDialog,editNameBottom,editAboutBottom;
    private FloatingActionButton fab;
    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;
    private CircleImageView profileImage;
    private ProgressDialog progressDialog;
    private LinearLayout editUsername,editAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_);

        profileImage = findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(this);
        editUsername = findViewById(R.id.username_layout);
        editAbout = findViewById(R.id.about_layout);

        mToolbar = findViewById(R.id.status_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        about = findViewById(R.id.about);
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fab = findViewById(R.id.fab);

        if (user != null) {
            firestore.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String username = documentSnapshot.getString("name");
                            String phonenumber = documentSnapshot.getString("phoneNumber");
                            String profileimage = documentSnapshot.getString("imageProfile");
                            String userAbout = documentSnapshot.getString("about");

                            name.setText(username);
                            phone.setText(phonenumber);
                            about.setText(userAbout);
                            Glide.with(UserProfile_Activity.this).load(profileimage).into(profileImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Get Data", "On Failure" + e.getMessage());
                }
            });
        }

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.bottom_edit, null);

                ((View) view.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editNameBottom.dismiss();
                    }
                });

                final EditText nameEdittext = view.findViewById(R.id.name_edittext);
                ((View) view.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(nameEdittext.getText().toString())) {
                            Toast.makeText(UserProfile_Activity.this, "Name can't be Empty", Toast.LENGTH_SHORT).show();
                        } else {
                            updateName(nameEdittext.getText().toString());
                            editNameBottom.dismiss();
                        }
                    }
                });

                editNameBottom = new BottomSheetDialog(view.getContext());
                editNameBottom.setContentView(view);
                editNameBottom.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        editNameBottom = null;
                    }
                });
                editNameBottom.show();

            }
        });

        editAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.bottom_about_edit, null);

                ((View) view.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editAboutBottom.dismiss();
                    }
                });

                final EditText aboutEdittext = view.findViewById(R.id.about_edittext);
                ((View) view.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(aboutEdittext.getText().toString())) {
                            Toast.makeText(UserProfile_Activity.this, "Please add some thoughts", Toast.LENGTH_SHORT).show();
                        } else {
                            updateAbout(aboutEdittext.getText().toString());
                            editAboutBottom.dismiss();
                        }
                    }
                });

                editAboutBottom = new BottomSheetDialog(view.getContext());
                editAboutBottom.setContentView(view);
                editAboutBottom.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        editAboutBottom = null;
                    }
                });
                editAboutBottom.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
                ((View) view.findViewById(R.id.gallery)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery();
                        bottomSheetDialog.dismiss();
                    }
                });
                ((View) view.findViewById(R.id.camera)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkCameraPermission();
                        bottomSheetDialog.dismiss();
                    }
                });
                ((View) view.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePhoto();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        bottomSheetDialog = null;
                    }
                });
                bottomSheetDialog.show();
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileImage.getDrawable() == null) {
                    View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
                    ((View) view.findViewById(R.id.gallery)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openGallery();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    ((View) view.findViewById(R.id.camera)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkCameraPermission();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bottomSheetDialog = new BottomSheetDialog(view.getContext());
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            bottomSheetDialog = null;
                        }
                    });
                    bottomSheetDialog.show();
                } else {
                    profileImage.invalidate();
                    Drawable drawable = profileImage.getDrawable();
                    Common.IMAGE_BITMAP = ((BitmapDrawable) drawable.getCurrent()).getBitmap();
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserProfile_Activity.this, profileImage, "image");
                    Intent intent = new Intent(UserProfile_Activity.this, DisplayImageActivity.class);
                    startActivity(intent, activityOptionsCompat.toBundle());
                }
            }
        });
    }

    private void deletePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure?");
        builder.setMessage("Do You Really Want to Delete Profile Image");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentReference documentReference = firestore.collection("users").document(user.getUid());
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("imageProfile", FieldValue.delete());
                documentReference.update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UserProfile_Activity.this, "Profile Picture Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }


    private void checkCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    221);
        }else if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    222);
        }else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpeg";
        try{
            File file = File.createTempFile("IMG_" + timeStamp,".jpeg",getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            intent.putExtra("listPhotoName",imageFileName);
            startActivityForResult(intent,440);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateAbout(String toString) {
        firestore.collection("users").document(user.getUid()).update("about",toString).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserProfile_Activity.this, "updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateName(String s) {
        firestore.collection("users").document(user.getUid()).update("name",s).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserProfile_Activity.this, "updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"),IMAGE_GALLERY_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null){
                    if (data.getData()!=null){
                        imageUri = data.getData();
                        uploadToFirebase();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            profileImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (requestCode == 440) {
            if (resultCode == RESULT_OK) {
                        uploadToFirebase();
                    }
                }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadToFirebase() {
        if(imageUri!=null){
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImagesProfile/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                     String sdownloadUrl = String.valueOf(downloadUrl);

                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("imageProfile",sdownloadUrl);

                    progressDialog.dismiss();

                    firestore.collection("users").document(user.getUid()).update(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UserProfile_Activity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserProfile_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UserProfile_Activity.this, settings_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}