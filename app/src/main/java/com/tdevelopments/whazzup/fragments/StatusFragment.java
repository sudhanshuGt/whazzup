package com.tdevelopments.whazzup.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tdevelopments.whazzup.Adapter.StatusAdapter;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.Status;
import com.tdevelopments.whazzup.UserModel.User;
import com.tdevelopments.whazzup.UserModel.UserStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class StatusFragment extends Fragment {

    ImageView buttonToUploadStatus;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    StatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_status, container, false);

        buttonToUploadStatus = rootView.findViewById(R.id.uploadImgS);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("uploading...");
        progressDialog.setCancelable(false);
        firebaseDatabase = FirebaseDatabase.getInstance();


        userStatuses = new ArrayList<>();
        statusAdapter = new StatusAdapter(getActivity(), userStatuses );
        recyclerView = rootView.findViewById(R.id.statusRecy);
        recyclerView.setAdapter(statusAdapter);



        firebaseDatabase.getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        buttonToUploadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(intent, 12);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (data != null)
         {
             if (data.getData() != null)

             {
                 progressDialog.show();
                 FirebaseStorage storage = FirebaseStorage.getInstance();
                 Date date = new Date();
                 StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");

                 reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getUserName());
                                    userStatus.setProfileImage(user.getProfileUrl());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String , Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileUrl", userStatus.getProfileImage());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUpdated());

                                    firebaseDatabase.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    firebaseDatabase.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    progressDialog.dismiss();
                                }
                            });
                        }
                     }
                 });
             }
         }
    }
}