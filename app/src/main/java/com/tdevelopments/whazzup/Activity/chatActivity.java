package com.tdevelopments.whazzup.Activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopments.whazzup.Adapter.MessagesAdapter;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class chatActivity extends AppCompatActivity {
     TextView usernamer;
     RecyclerView chatboxRecyclerView;
     MessagesAdapter messagesAdapter;
     ArrayList<Message> messages;
     FirebaseDatabase firebaseDatabaseChat;
     String senderRoom  , receiverRoom;
     TextView btnToSendMsges;
     EditText chatBoxTosendmsg;
     TextView timeForMsg;
     ImageView btnforback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usernamer = findViewById(R.id.userNameR);
        btnToSendMsges = findViewById(R.id.btnTosendMsg);
        chatBoxTosendmsg = findViewById(R.id.chatBoxTosendMsg);
        btnforback = findViewById(R.id.backbtnchat);

        String userNameR = getIntent().getStringExtra("name");
        String recieverUid = getIntent().getStringExtra("uid");
        String SenderUid = FirebaseAuth.getInstance().getUid();


        usernamer.setText(userNameR);
        firebaseDatabaseChat = FirebaseDatabase.getInstance();
        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this, messages );
        chatboxRecyclerView = findViewById(R.id.chatboxrecy);
        chatboxRecyclerView.setAdapter(messagesAdapter);


        senderRoom = SenderUid + recieverUid;
        receiverRoom = recieverUid + SenderUid ;

        chatboxRecyclerView.scrollToPosition(messages.size() - 1);

        // firebase data gettting into recycler view
        firebaseDatabaseChat.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         messages.clear();
                         for (DataSnapshot snapshot1: snapshot.getChildren()) {
                             Message message = snapshot1.getValue(Message.class);
                             message.setMessageId(snapshot1.getKey());
                             messages.add(message);
                         }
                         messagesAdapter.notifyDataSetChanged();
                        chatboxRecyclerView.scrollToPosition(messages.size() - 1);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        // pushing data to firebase
        btnToSendMsges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String msgFromBoxToSend = chatBoxTosendmsg.getText().toString();
               Date date = new Date();
               Message message = new Message(msgFromBoxToSend, SenderUid , date.getTime() ) ;
               chatBoxTosendmsg.setText("");

               String randomKeyForMsg = firebaseDatabaseChat.getReference().push().getKey();
                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());

                firebaseDatabaseChat.getReference().child("chats")
                        .child(senderRoom).updateChildren(lastMsgObj);
                firebaseDatabaseChat.getReference().child("chats")
                        .child(receiverRoom).updateChildren(lastMsgObj);
                
               firebaseDatabaseChat.getReference().child("chats")
                       .child(senderRoom)
                       .child("messages")
                       .child(randomKeyForMsg)
                       .setValue(message)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               firebaseDatabaseChat.getReference().child("chats")
                                       .child(receiverRoom)
                                       .child("messages")
                                       .child(randomKeyForMsg)
                                       .setValue(message)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {

                                           }
                                       });
                           }
                       });

                       
            }
        });

        btnforback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
    }
    // handling back press key
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}