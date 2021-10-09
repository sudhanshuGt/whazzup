package com.tdevelopments.whazzup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;

public class account_setting extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;

    TextView usernamecc ,  ediprofbtn , encryptionSetting  ;
    ImageView userprofile , accbackbtn;
    Button buttonLogOut ;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        // init view
         usernamecc = findViewById(R.id.usernameacc);
         userprofile = findViewById(R.id.userprofinsetting);
         buttonLogOut = findViewById(R.id.logoutBtn);
         accbackbtn = findViewById(R.id.accnackbtn);
         ediprofbtn = findViewById(R.id.editprofbtn);
         encryptionSetting = findViewById(R.id.encryptionSettings);

         // init firebase db , auth , storage
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // encryption setting button

        encryptionSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account_setting.this, EncryptionSetup.class);
                startActivity(intent);
            }
        });

        ediprofbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account_setting.this, editprof.class);
                startActivity(intent);
            }
        });

        // logout btn
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(account_setting.this, UserAuth.class);
                startActivity(intent);
                finish();
            }
        });

        // back btn
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

    // get username and profile from firebase db
    private void getTheUserProfileData() {

        firebaseDatabase.getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        if (user != null) {
                            usernamecc.setText(user.getUserName());

                            Glide.with(getApplicationContext()).load(user.getProfileUrl())
                                    .placeholder(R.drawable.user)
                                    .into(userprofile);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    // on back press handling
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(account_setting.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}