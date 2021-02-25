package com.tdevelopments.whazzup.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.tdevelopments.whazzup.R;
import com.tdevelopments.whazzup.fragments.CallFragment;
import com.tdevelopments.whazzup.fragments.StatusFragment;
import com.tdevelopments.whazzup.fragments.chatFragment;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    TextView titleText;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottom_nav);
        titleText = findViewById(R.id.page_title);
        ImageView searchbtn  = findViewById(R.id.serach_btn);
        ImageView camerabtn = findViewById(R.id.storyImagePick);
        ImageView userView = findViewById(R.id.menu_option);
        firebaseStorage  = FirebaseStorage.getInstance();




        // initialize firt fragment on activity open
        getSupportFragmentManager().beginTransaction().replace(R.id.frameForFrag, new chatFragment()).commit();

        // on nav item clicked
        onBottomMenuSelected();


        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, UserAuth.class);
                startActivity(intent);
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this, storyImagePicker.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void onBottomMenuSelected() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i) {

                    case R.id.chat_sec:
                        fragment = new chatFragment();
                        titleText.setText("Message");
                        break;

                    case R.id.phone_sec:
                        fragment = new CallFragment();
                        titleText.setText("Calls");
                        break;
                    case R.id.status_sec:
                        fragment = new StatusFragment();
                        titleText.setText("Status");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameForFrag, fragment).commit();
            }
        });
    }


}