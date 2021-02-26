package com.tdevelopments.whazzup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.tdevelopments.whazzup.Activity.MainActivity;
import com.tdevelopments.whazzup.Activity.UserAuth;
import com.tdevelopments.whazzup.UserModel.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class account_setting extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;

    TextView usernamecc , userabout , userPhoneacc;
    ImageView userprofile , accbackbtn;
    Button buttonLogOut;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

         usernamecc = findViewById(R.id.usernameacc);
         userabout  = findViewById(R.id.useraboutacc);
         userprofile = findViewById(R.id.userprofinsetting);
         userPhoneacc = findViewById(R.id.userPhoneacc);
         buttonLogOut = findViewById(R.id.logoutBtn);
         accbackbtn = findViewById(R.id.accnackbtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(account_setting.this, UserAuth.class);
                startActivity(intent);
                finish();
            }
        });
        accbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account_setting.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getTheUserProfileData();

    }

    private void getTheUserProfileData() {

        firebaseDatabase.getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        usernamecc.setText(user.getUserName());
                        userabout.setText(user.getUserAbout());
                        userPhoneacc.setText(user.getPhoneNumber());
                        Glide.with(account_setting.this).load(user.getProfileUrl())
                                .placeholder(R.drawable.user)
                                .into(userprofile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(account_setting.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}