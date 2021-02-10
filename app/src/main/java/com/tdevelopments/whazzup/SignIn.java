package com.tdevelopments.whazzup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class SignIn extends AppCompatActivity {
    TextView textView;
    PinView pinView;
    Button buttonVerfy;
    String phoneNumber;
    String OtpId;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        textView = findViewById(R.id.textView2);
        pinView = findViewById(R.id.firstPinView);
        buttonVerfy = findViewById(R.id.buttonVerify);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        phoneNumber = intent.getExtras().getString("phoneNum");

        textView.setText( phoneNumber);

        processOtpNow();

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
                "+91" + phoneNumber ,
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
                                  Intent intent = new Intent(SignIn.this, MainActivity.class);
                                  startActivity(intent);
                                  finish();
                                  return;
                        } else {

                            Toast.makeText(SignIn.this, "SignIn failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}