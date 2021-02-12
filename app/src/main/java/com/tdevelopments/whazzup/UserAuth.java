package com.tdevelopments.whazzup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;


public class UserAuth extends AppCompatActivity {
    TextInputLayout textInputLayout;
    Button buttonConfrm;
    CountryCodePicker countryCodePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        // firebase initialize
        FirebaseApp.initializeApp(this);

        // layout view
        textInputLayout = findViewById(R.id.userCred);
        buttonConfrm = findViewById(R.id.confrm_Btn);
        countryCodePicker = findViewById(R.id.cpp);

        // check if user is logged in already
        userIsLoggedIn();
        


        buttonConfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCreds =  textInputLayout.getEditText().getText().toString().trim();

                if (userCreds.isEmpty() )
                {
                    textInputLayout.setError("field can't be empty");
                }
                else if (userCreds.length() > 10 || userCreds.length() < 10)
                {
                    textInputLayout.setError("input should'nt exceed 10 digit");
                }

                else {
                    Intent intent = new Intent(UserAuth.this, SignIn.class);
                    intent.putExtra("phoneNum", "+" + countryCodePicker.getSelectedCountryCode() +  userCreds);
                    startActivity(intent);
                    finish();
                }


            }
        });

    }

    // if user already logged in farword to home
    private void userIsLoggedIn() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null )
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }
    }
}