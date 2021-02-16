package com.tdevelopments.whazzup.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;
import com.tdevelopments.whazzup.Activity.chatActivity;

import java.util.ArrayList;

public class UsersAdaper extends RecyclerView.Adapter<UsersAdaper.UserViewHolder> {
     Context context;
    ArrayList<User> users;

    public UsersAdaper(Context context , ArrayList<User> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatuserslist, parent, false);
        
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
          User user = users.get(position);
          holder.userName.setText(user.getUserName());
          Glide.with(context).load(user.getUserProfilePic())
                  .placeholder(R.drawable.user)
                  .into(holder.userProfile);

          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(context, chatActivity.class);
                  intent.putExtra("name", user.getUserName());
                  intent.putExtra("uid", user.getUserId());
                  context.startActivity(intent);
              }
          });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
    {


        TextView userName;
        ImageView userProfile;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName  = itemView.findViewById(R.id.userNameForChatList);
            userProfile = itemView.findViewById(R.id.userprof);
        }
    }
}
