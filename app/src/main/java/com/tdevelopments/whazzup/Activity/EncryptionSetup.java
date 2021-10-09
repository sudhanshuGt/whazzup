package com.tdevelopments.whazzup.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.tdevelopments.whazzup.R;

public class EncryptionSetup extends AppCompatActivity {

    RadioButton AESalgo , DESalgo , RSAalgo;
    TextInputLayout setEncryptionKey;
    TextView skipEncryption;
    Button setEncryptionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption_setup);

        AESalgo = findViewById(R.id.AESalgo);
        DESalgo = findViewById(R.id.DESalgo);
        RSAalgo = findViewById(R.id.RSAalgo);
        setEncryptionKey = findViewById(R.id.encryptionKeyData);
        setEncryptionBtn = findViewById(R.id.setEncryption);
        skipEncryption = findViewById(R.id.skipEncryption);

        // skip btn

        skipEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}