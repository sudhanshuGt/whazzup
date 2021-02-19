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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.User;
import com.tdevelopments.whazzup.Activity.chatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

          String senderId = FirebaseAuth.getInstance().getUid();
          String senderRoom = senderId + user.getUserId();

          FirebaseDatabase.getInstance().getReference()
                  .child("chats")
                  .child(senderRoom)
                  .addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {


                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(long.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                            holder.lastMsgInlist.setText(lastMsg);
                            holder.lstMsgTime.setText(dateFormat.format(new Date(time)));

                        }
                        else {
                             holder.lastMsgInlist.setText("Tap to chat");
                             holder.lstMsgTime.setVisibility(View.INVISIBLE);
                        }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });

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
        TextView lastMsgInlist;
        TextView lstMsgTime ;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName  = itemView.findViewById(R.id.userNameForChatList);
            userProfile = itemView.findViewById(R.id.userprof);
            lastMsgInlist = itemView.findViewById(R.id.lastMsgChat);
            lstMsgTime = itemView.findViewById(R.id.lastMsgTime);
        }
    }
}
