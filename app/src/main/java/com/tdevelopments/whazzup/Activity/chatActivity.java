package com.tdevelopments.whazzup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopments.whazzup.Adapter.MessagesAdapter;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.UserModel.Message;
import com.tdevelopments.whazzup.UserModel.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.tdevelopments.whazzup.EncryptionAlgo.AES;


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
     ImageView btnforback , userprofr , speechToText;
     User user;
     
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;

    public  static String pwdtext ="qwerty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usernamer = findViewById(R.id.userNameR);
        btnToSendMsges = findViewById(R.id.btnTosendMsg);
        userprofr = findViewById(R.id.profile_image);

        chatBoxTosendmsg = findViewById(R.id.chatBoxTosendMsg);
        btnforback = findViewById(R.id.backbtnchat);
        speechToText = findViewById(R.id.speechToText);

        String userNameR = getIntent().getStringExtra("name");
        String recieverUid = getIntent().getStringExtra("uid");
        String SenderUid = FirebaseAuth.getInstance().getUid();



        usernamer.setText(userNameR);
        firebaseDatabaseChat = FirebaseDatabase.getInstance();
        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this, messages );
        chatboxRecyclerView = findViewById(R.id.chatboxrecy);
        chatboxRecyclerView.setAdapter(messagesAdapter);

        // checking  speech recognition permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        // init for speech recognition
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // speech recognition implementation


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                chatBoxTosendmsg.setText("");
                chatBoxTosendmsg.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                speechToText.setImageResource(R.drawable.mic);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                chatBoxTosendmsg.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speechToText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                    chatBoxTosendmsg.setHint("Message..");
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechToText.setImageResource(R.drawable.micmute);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        // speech recognition implement end
        

        // getting user profile of receiver
        firebaseDatabaseChat.getReference().child("users")
                .child(recieverUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        Glide.with(chatActivity.this).load(user.getProfileUrl())
                                .placeholder(R.drawable.user)
                                .into(userprofr);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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
                             try {
                                 AES.decrypt(message.getMessage(),pwdtext);
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
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
                String msgFromBoxToSend = null;
                try {
                    msgFromBoxToSend = AES.encrypt(chatBoxTosendmsg.getText().toString() , pwdtext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    
    // speech recogintion destroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
        chatBoxTosendmsg.setHint("message...");
    }

    // audio permission

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
    
}