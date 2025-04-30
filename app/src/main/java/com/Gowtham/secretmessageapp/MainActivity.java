package com.Gowtham.secretmessageapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSendMessage, btnViewMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnViewMessages = findViewById(R.id.btnViewMessages);

        btnSendMessage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
            startActivity(intent);
        });

        btnViewMessages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
            startActivity(intent);
        });
    }
}
