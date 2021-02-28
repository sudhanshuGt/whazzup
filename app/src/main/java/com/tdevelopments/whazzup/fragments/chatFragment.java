package com.tdevelopments.whazzup.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;
import com.tdevelopments.whazzup.Adapter.UsersAdaper;

import java.util.ArrayList;


public class chatFragment extends Fragment {

    
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerViewUsers;
    ArrayList<User> users;
    UsersAdaper usersAdaper;
    String loggedInUserUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_chat, container, false);

        //  firebase initialize
        firebaseDatabase = FirebaseDatabase.getInstance();
        loggedInUserUid = FirebaseAuth.getInstance().getUid();

        // array list for recyclerView
        users = new ArrayList<>();

        // setting adapter and recycler view
        usersAdaper = new UsersAdaper(getActivity(), users);
        recyclerViewUsers = rootView.findViewById(R.id.chatListRecycler);
        recyclerViewUsers.setAdapter(usersAdaper);

        // function to get the data from the firebase
        getTheUserDataFromForebase();


        return rootView;

    }



    private void getTheUserDataFromForebase() {

        firebaseDatabase.getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            User user =snapshot1.getValue(User.class);
                            if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                                users.add(user);
                            }
                        }

                        usersAdaper.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}