package com.tdevelopments.whazzup.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECIEVE = 2;
    String senderRoom;
    String receiverRoom;
    

    public MessagesAdapter(Context context , ArrayList<Message> messages , String senderRoom, String receiverRoom){
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }                                                          

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chatsend, parent, false);
            return new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chatrecieve, parent, false);
            return new RecieverViewHolder(view);
        }
        
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT ;
        }
        else {
            return ITEM_RECIEVE;
        }
        
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);


        int reaction[] = new int[]{
                R.drawable.likereact,
                R.drawable.heartreact,
                R.drawable.laughing,
                R.drawable.hearteye,
                R.drawable.wowreat,
                R.drawable.sad
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reaction)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (newpose) -> {
            if (holder.getClass() == SentViewHolder.class) {
                SentViewHolder viewHolder = (SentViewHolder)holder;
                viewHolder.feelingS.setImageResource(reaction[newpose]);
            }
            else {
                RecieverViewHolder recieverViewHolder = (RecieverViewHolder)holder;
                recieverViewHolder.feelingR.setImageResource(reaction[newpose]);

            }
            message.setFeeling(newpose);
            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child("senderRoom")
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child("receiverRoom")
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);
            return true; // true is closing popup, false is requesting a new selection
        });


          if (holder.getClass() == SentViewHolder.class) {
              SentViewHolder viewHolder = (SentViewHolder)holder;
              viewHolder.sendTextView.setText(message.getMessage());
              if (message.getFeeling() >= 0) {
                  message.setFeeling(reaction[(int) message.getFeeling()]);
                  viewHolder.feelingS.setVisibility(View.VISIBLE);
              } else {
                  viewHolder.feelingS.setVisibility(View.GONE);
              }

              viewHolder.sendTextView.setOnTouchListener(new View.OnTouchListener() {
                  @Override
                  public boolean onTouch(View v, MotionEvent event) {
                      popup.onTouch(v, event);
                      return false;
                  }
              });
          } 
          else {
              RecieverViewHolder viewHolder = (RecieverViewHolder)holder;
              viewHolder.recieveTextView.setText(message.getMessage());
              if (message.getFeeling() >= 0) {
                  message.setFeeling(reaction[(int) message.getFeeling()]);
                  viewHolder.feelingR.setVisibility(View.VISIBLE);
              }  else {
                  viewHolder.feelingR.setVisibility(View.GONE);
              }

              viewHolder.recieveTextView.setOnTouchListener(new View.OnTouchListener() {
                  @Override
                  public boolean onTouch(View v, MotionEvent event) {

                      popup.onTouch(v, event);
                      return false;
                  }
              });
          }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        TextView sendTextView;
        ImageView feelingS;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            sendTextView = itemView.findViewById(R.id.sendchat);
            feelingS = itemView.findViewById(R.id.msgfeelingS);
        }
    }

    public class RecieverViewHolder extends  RecyclerView.ViewHolder{
        TextView recieveTextView;
        ImageView feelingR;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieveTextView = itemView.findViewById(R.id.recievechat);
            feelingR = itemView.findViewById(R.id.msgfeelingR);
        }
    }
}
