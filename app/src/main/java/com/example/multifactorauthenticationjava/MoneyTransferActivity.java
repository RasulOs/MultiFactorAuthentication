package com.example.multifactorauthenticationjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MoneyTransferActivity extends AppCompatActivity {

    private Button transfer_submit, transfer_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);

        if (MainActivity.getSecurityLevel() <= 1) {
            Intent intent = new Intent(MoneyTransferActivity.this, PinCodeActivity.class);
            intent.putExtra(PinCodeActivity.ACTIVITY_FROM, "Transfer");
            startActivity(intent);
            finish();
        }

        transfer_submit = findViewById(R.id.transfer_submit);
        transfer_cancel = findViewById(R.id.transfer_cancel);

        transfer_submit.setOnClickListener(view -> {
            finish();
        });

        transfer_cancel.setOnClickListener(view -> {
            finish();
        });
    }
}