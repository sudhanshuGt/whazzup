package com.tdevelopments.whazzup.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.UserStatus;

import java.util.ArrayList;

public class StatusApter extends RecyclerView.Adapter<StatusApter.StatusViewHolder> {

    Context context;
    ArrayList<UserStatus> userStatuses;
    
    public StatusApter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }




    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_layout, parent, false  );
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView userStatus;
        TextView userNameStatus;
        TextView timeOfStaus;
        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameStatus = itemView.findViewById(R.id.statusImage);
            userNameStatus = itemView.findViewById(R.id.userNameStatus);
            timeOfStaus = itemView.findViewById(R.id.statusTime);
        }
    }
}
