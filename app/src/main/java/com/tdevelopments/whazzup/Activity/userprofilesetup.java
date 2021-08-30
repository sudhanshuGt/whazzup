 package com.tdevelopments.whazzup.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class userprofilesetup extends AppCompatActivity {

    CircleImageView circleImageView;
    TextInputLayout editUserName ,  editTextAbout ;
    Button buttonSetProf;
    TextView skipProfSet;

    Uri selectedProf;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofilesetup);

        // view binding or hooks
        circleImageView = findViewById(R.id.profile_image);
        editUserName = findViewById(R.id.userNameSetup);
        editTextAbout = findViewById(R.id.userAboutSetup);
        buttonSetProf = findViewById(R.id.buttonSetupProfile);
        skipProfSet = findViewById(R.id.skipProfileSetup);

        progressDialog = new ProgressDialog(this);


        // firebase initialize

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        buttonSetProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = editUserName.getEditText().getText().toString().trim();

                if (enteredUsername.isEmpty() || selectedProf == null)
                {
                    editUserName.setError(" set profile / username please !");
                 }

               

                if (selectedProf != null || !enteredUsername.isEmpty())
                {
                    progressDialog.setMessage("Creating Your profile , wait..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StorageReference storageReference = firebaseStorage.getReference().child("UserProfile").child(firebaseAuth.getUid());
                    storageReference.putFile(selectedProf).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {
                              storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {


                                      String profileUrl = uri.toString();
                                      String UserId = firebaseAuth.getUid();
                                      String UserName = editUserName.getEditText().getText().toString().trim();
                                      String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
                                      String userAbout = editTextAbout.getEditText().getText().toString().trim();

                                      User user = new User(UserId, UserName, phoneNumber, profileUrl, userAbout);

                                      firebaseDatabase.getReference()
                                              .child("users")
                                              .child(UserId)
                                              .setValue(user)
                                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void aVoid) {
                                                      progressDialog.dismiss();
                                                      Intent intent = new Intent(userprofilesetup.this, MainActivity.class);
                                                      startActivity(intent);
                                                      finish();

                                                  }
                                              });
                                  }
                              });
                            }
                        }
                    });
                }
                else
                    {
                        // if user does'nt selected a profile image

                        String UserId = firebaseAuth.getUid();
                        String UserName = editUserName.getEditText().getText().toString().trim();
                        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
                        String userAbout = editTextAbout.getEditText().getText().toString().trim();

                        User user = new User(UserId, UserName, phoneNumber, "No Image", userAbout);

                        firebaseDatabase.getReference()
                                .child("users")
                                .child(UserId)
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(userprofilesetup.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                    }

            }
        });

        skipProfSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userprofilesetup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
        {
            if (data.getData() != null)  {
                circleImageView.setImageURI(data.getData());
                selectedProf = data.getData();
            }

        }
    }
}