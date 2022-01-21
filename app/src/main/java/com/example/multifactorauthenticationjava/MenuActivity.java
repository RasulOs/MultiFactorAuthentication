package com.example.multifactorauthenticationjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import data.DBHelper;

public class MenuActivity extends AppCompatActivity {

    private TextView menu_title;
    private TextView menu_seurity;

    private Button menu_balance, menu_money_transfer, menu_password;

    private DBHelper dbHelper;

    private static String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu_title = findViewById(R.id.menu_title);
        menu_seurity = findViewById(R.id.menu_seurity);

        menu_balance = findViewById(R.id.menu_balance);
        menu_money_transfer = findViewById(R.id.menu_money_transfer);
        menu_password = findViewById(R.id.menu_password);

        dbHelper = new DBHelper(MenuActivity.this);

        dbHelper.initializeName(MainActivity.getAccountEmail());

        if (name.equals("")) {
            name = "admin";
        }

        menu_title.setText("Welcome, " + name);


        menu_balance.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, BalanceActivity.class));
        });
        menu_money_transfer.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, MoneyTransferActivity.class));
        });
        menu_password.setOnClickListener(view -> {
            startActivity(new Intent(MenuActivity.this, PasswordChangeActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        menu_seurity.setText("Your security level is: " + MainActivity.getSecurityLevel());
    }

    public static void setName(String name) {
        MenuActivity.name = name;
    }
}