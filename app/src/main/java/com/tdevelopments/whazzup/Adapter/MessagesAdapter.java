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
import com.tdevelopments.whazzup.EncryptionAlgo.AES;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECIEVE = 2;
    public  static String newpwdtext ="qwerty";
    

    public MessagesAdapter(Context context , ArrayList<Message> messages){
        this.context = context;
        this.messages = messages;

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




          if (holder.getClass() == SentViewHolder.class) {
              SentViewHolder viewHolder = (SentViewHolder)holder;
              try {
                  viewHolder.sendTextView.setText(AES.decrypt(message.getMessage(), newpwdtext ));
              } catch (Exception e) {
                  e.printStackTrace();
              }


          } 
          else {
              RecieverViewHolder viewHolder = (RecieverViewHolder)holder;
              try {
                  viewHolder.recieveTextView.setText(AES.decrypt(message.getMessage(), newpwdtext));
              } catch (Exception e) {
                  e.printStackTrace();
              }


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

        }
    }

    public class RecieverViewHolder extends  RecyclerView.ViewHolder{
        TextView recieveTextView;
        ImageView feelingR;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieveTextView = itemView.findViewById(R.id.recievechat);
           
        }
    }
}
