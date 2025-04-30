package com.Gowtham.secretmessageapp;

import android.os.Bundle;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    EditText editPhone, editOtp;
    Button btnSendOtp, btnVerify;
    String verificationId;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Link views
        editPhone = findViewById(R.id.editPhone);
        editOtp = findViewById(R.id.editOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerify = findViewById(R.id.btnVerify);

        // Send OTP on button click
        btnSendOtp.setOnClickListener(v -> sendOtp());

        // Verify OTP on button click
        btnVerify.setOnClickListener(v -> verifyOtp());
    }

    private void sendOtp() {
        String phone = editPhone.getText().toString().trim();

        if (phone.isEmpty() || phone.length() < 10) {
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // Auto-retrieval or instant verification
                    signInWithCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(PhoneAuthActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken token) {
                    verificationId = id;
                    Toast.makeText(PhoneAuthActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyOtp() {
        String code = editOtp.getText().toString().trim();

        if (verificationId == null || code.isEmpty()) {
            Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Phone number verified!", Toast.LENGTH_SHORT).show();
                // TODO: Redirect to ViewMessageActivity
                // startActivity(new Intent(this, ViewMessagesActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect OTP. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}