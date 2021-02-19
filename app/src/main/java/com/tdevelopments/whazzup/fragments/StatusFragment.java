package com.tdevelopments.whazzup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tdevelopments.whazzup.Adapter.StatusApter;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.UserStatus;

import java.util.ArrayList;


public class StatusFragment extends Fragment {
    StatusApter statusApter;
    ArrayList<UserStatus> userStatuses;
    RecyclerView statusRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_chat, container, false);

        userStatuses = new ArrayList<>();
        statusRecyclerView = rootView.findViewById(R.id.statusRecy);
        statusApter = new StatusApter(getActivity(), userStatuses);
        statusRecyclerView.setAdapter(statusApter);



        return rootView;
    }
}