package com.example.multifactorauthenticationjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PasswordChangeActivity extends AppCompatActivity {
    private Button password_submit, password_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        if (MainActivity.getSecurityLevel() <= 2) {
            Intent intent = new Intent(PasswordChangeActivity.this, PinCodeActivity.class);
            intent.putExtra(PinCodeActivity.ACTIVITY_FROM, "Password");
            startActivity(intent);
            finish();
        }

        password_submit = findViewById(R.id.password_submit);
        password_cancel = findViewById(R.id.password_cancel);

        password_submit.setOnClickListener(view -> {
            finish();
        });

        password_cancel.setOnClickListener(view -> {
            finish();
        });
    }
}