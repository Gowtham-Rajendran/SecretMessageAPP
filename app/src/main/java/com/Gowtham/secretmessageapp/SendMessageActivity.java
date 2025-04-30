package com.Gowtham.secretmessageapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendMessageActivity extends AppCompatActivity {

    EditText editPhoneNumber, editMessage;
    Button btnSend;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        database = FirebaseDatabase.getInstance().getReference("messages");

        btnSend.setOnClickListener(v -> {
            String phone = editPhoneNumber.getText().toString().trim();
            String messageText = editMessage.getText().toString().trim();

            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(messageText)) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Save a full Message object instead of plain text
            Message msg = new Message(messageText);

            database.child(phone).push().setValue(msg)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Message sent anonymously!", Toast.LENGTH_SHORT).show();
                        editMessage.setText("");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }
}
