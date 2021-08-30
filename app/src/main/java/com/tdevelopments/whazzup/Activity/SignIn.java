package com.tdevelopments.whazzup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;

import java.util.concurrent.TimeUnit;

public class SignIn extends AppCompatActivity {
    TextView textView;
    PinView pinView;
    Button buttonVerfy;
    String phoneNumber;
    String OtpId;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        textView = findViewById(R.id.textView2);
        pinView = findViewById(R.id.firstPinView);
        buttonVerfy = findViewById(R.id.buttonVerify);

        // progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending OTP");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // firebase auth initialize..
        mAuth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference();


        // receiving string pass through previous activity to this through intent
        Intent intent = getIntent();
        phoneNumber = intent.getExtras().getString("phoneNum");

        textView.setText( phoneNumber);

        // start verification
        processOtpNow();


        // on manual otp verification checking if field is empty and also both system generated and user enter code is same or not
        buttonVerfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinView.getText().toString().isEmpty())
                {
                    pinView.setError("field can't be empty");
                }
                else if (pinView.getText().toString().length() != 6)
                {
                    pinView.setError("Invalid otp");
                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpId, pinView.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void processOtpNow() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber ,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            OtpId = s;
            progressDialog.dismiss();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(SignIn.this, "verfication faild", Toast.LENGTH_SHORT).show();
        }
    }  ;


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference userNameRef = firebaseRef.child("users").child(mAuth.getUid());
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists()) {
                                        Intent intent = new Intent(SignIn.this, userprofilesetup.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(SignIn.this, "database error", Toast.LENGTH_SHORT).show(); //Don't ignore errors!
                                }
                            };
                            userNameRef.addListenerForSingleValueEvent(eventListener);

                        } else {

                            Toast.makeText(SignIn.this, "SignIn failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}