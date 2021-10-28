package com.example.myapplicationchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class
login_activity extends AppCompatActivity {

    private EditText mobileNumber,otp;
    private CountryCodePicker countryCodePicker;
    private Button contd_btn,verify;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String s;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        mobileNumber=findViewById(R.id.mobileNumber);
        countryCodePicker=findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(mobileNumber);
        contd_btn=findViewById(R.id.contdBtn);
        mAuth=FirebaseAuth.getInstance();
        otp=findViewById(R.id.otpText);
        verify=findViewById(R.id.verifyBtn);
        progressDialog=new ProgressDialog(this);
        user=FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        contd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_mobileNumber = mobileNumber.getText().toString().trim();
                if (TextUtils.isEmpty(txt_mobileNumber)) {
                    mobileNumber.setError("Mobile Number not Entered");
                } else if (txt_mobileNumber.replace(" ", "").length() != 10) {
                    mobileNumber.setError("Enter Correct Mobile Number");
                } else {
                    progressDialog.setTitle("Sending OTP");
                    progressDialog.setMessage("Please Wait!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    String phoneNumber = countryCodePicker.getFullNumberWithPlus().replace(" ","");
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(login_activity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_otp = otp.getText().toString().trim();
                if (TextUtils.isEmpty(txt_otp)){
                    otp.setError("OTP not Entered");
                }else if (txt_otp.replace(" ","").length()!=6){
                    otp.setError("Enter Correct OTP");
                }else {
                    try {
                        progressDialog.setTitle("Verifying User");
                        progressDialog.setMessage("Please Wait!");
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, txt_otp);
                        signInWithPhoneAuthCredential(credential);
                    }catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(login_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progressDialog.dismiss();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(login_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                countryCodePicker.setVisibility(View.VISIBLE);
                mobileNumber.setVisibility(View.VISIBLE);
                contd_btn.setVisibility(View.VISIBLE);
                otp.setVisibility(View.INVISIBLE);
                verify.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressDialog.dismiss();
                countryCodePicker.setVisibility(View.INVISIBLE);
                mobileNumber.setVisibility(View.INVISIBLE);
                contd_btn.setVisibility(View.INVISIBLE);
                otp.setVisibility(View.VISIBLE);
                verify.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user!=null){
            startActivity(new Intent(login_activity.this,home_activity.class));
            finish();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            startActivity(new Intent(login_activity.this,UserProfileActivity.class));
//                            if (user!=null){
//                                String userId = user.getUid();
//                            Users users = new Users(
//                                    "",
//                                    "",
//                                    userId,
//                                    "",
//                                    user.getPhoneNumber()
//                            );
//                            firestore.collection("users").add(users)
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//
//                                        }
//                                    });
//                            }else {
//                                Toast.makeText(login_activity.this, "Error", Toast.LENGTH_SHORT).show();
//                            }
                            }else{
                            progressDialog.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(login_activity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                });
    }
}