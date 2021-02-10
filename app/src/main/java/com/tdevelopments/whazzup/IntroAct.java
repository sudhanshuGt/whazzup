package com.tdevelopments.whazzup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class IntroAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ScreenSplash screenSplash = new ScreenSplash();
        screenSplash.start();
    }

    private class ScreenSplash extends Thread
    {
        @Override
        public void run() {
            try {
                sleep(5000);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }


            Intent intent = new Intent(IntroAct.this, UserAuth.class);
            startActivity(intent);
            finish();
            


        }
    }
}