package com.tdevelopments.whazzup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class editprof extends AppCompatActivity {

    TextView cancelBtn , saveBtn;
    User user;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;

    // views
    CircleImageView imageView;
    TextInputLayout editusername , edituserbaout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprof);

        // init view
        cancelBtn = findViewById(R.id.cancelBtn);
        imageView = findViewById(R.id.editprofimgview);
        editusername =  findViewById(R.id.editprofusername);
        edituserbaout = findViewById(R.id.editprofuserabout);

        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        getTheUserProfileDataToEdit();


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void getTheUserProfileDataToEdit() {

        firebaseDatabase.getReference().child("users")
                .child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        if (user != null) {

                            Glide.with(getApplicationContext()).load(user.getProfileUrl())
                                    .placeholder(R.drawable.user)
                                    .into(imageView);
                            editusername.getEditText().setText(user.getUserName());
                            edituserbaout.getEditText().setText(user.getUserAbout());

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}