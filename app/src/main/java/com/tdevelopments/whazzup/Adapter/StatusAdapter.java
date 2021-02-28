 package com.tdevelopments.whazzup.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.tdevelopments.whazzup.Activity.MainActivity;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.Status;
import com.tdevelopments.whazzup.UserModel.UserStatus;
import com.tdevelopments.whazzup.fragments.StatusFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    Context context;
    ArrayList<UserStatus> userStatuses;

    public StatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_layout, parent, false);

        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
           UserStatus userStatus = userStatuses.get(position);

           Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size() -1 );

           Glide.with(context).load(lastStatus.getImageUrl()).into(holder.storyImage);
           holder.circularStatusView.setPortionsCount(userStatus.getStatuses().size());

           holder.userNameS.setText(userStatus.getName());

              long time = userStatus.getLastUpdated();
           SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
           holder.timeOfS.setText(dateFormat.format(new Date(time)));

           holder.circularStatusView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                     ArrayList<MyStory> myStories = new ArrayList<>();
                     for (Status status : userStatus.getStatuses()) {
                          myStories.add(new MyStory(status.getImageUrl()));
                     }

                   new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                           .setStoriesList(myStories) // Required
                           .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                           .setTitleText(userStatus.getName()) // Default is Hidden
                           .setSubtitleText("") // Default is Hidden
                           .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                           .setStoryClickListeners(new StoryClickListeners() {
                               @Override
                               public void onDescriptionClickListener(int position) {
                                   //your action
                               }

                               @Override
                               public void onTitleIconClickListener(int position) {
                                   //your action
                               }
                           }) // Optional Listeners
                           .build() // Must be called before calling show method
                           .show();
                     
               }
           });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
           ImageView storyImage;
           TextView userNameS;
           TextView timeOfS;
           CircularStatusView circularStatusView;
           public StatusViewHolder(@NonNull View itemView) {
               super(itemView);

               circularStatusView = itemView.findViewById(R.id.circular_status_view);
               storyImage = itemView.findViewById(R.id.statusImage);
               userNameS  = itemView.findViewById(R.id.userNameStatus);
               timeOfS = itemView.findViewById(R.id.statusTime);
           }
       }
   }