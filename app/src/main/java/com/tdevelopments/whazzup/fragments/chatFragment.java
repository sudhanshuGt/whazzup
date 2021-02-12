package com.tdevelopments.whazzup.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.tdevelopments.whazzup.MainActivity;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserAuth;


public class chatFragment extends Fragment {

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_chat, container, false);
        return rootView;

    }
}