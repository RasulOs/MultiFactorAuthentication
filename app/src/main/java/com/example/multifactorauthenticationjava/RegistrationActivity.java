package com.example.multifactorauthenticationjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.function.Supplier;

import data.DBHelper;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout registration_full_name, registration_email, registration_mobile_number,
            registration_pin, registration_signup_password, registration_signup_password_again;

    private Button registration_signup_register, registration_cancel;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registration_full_name = findViewById(R.id.registration_full_name);
        registration_email = findViewById(R.id.registration_email);
        registration_mobile_number = findViewById(R.id.registration_mobile_number);
        registration_pin = findViewById(R.id.registration_pin);
        registration_signup_password = findViewById(R.id.registration_signup_password);
        registration_signup_password_again = findViewById(R.id.registration_signup_password_again);

        registration_signup_register = findViewById(R.id.registration_signup_register);
        registration_cancel = findViewById(R.id.registration_cancel);



        dbHelper = new DBHelper(RegistrationActivity.this);

        registration_signup_register.setOnClickListener(view -> {
            String fullName = registration_full_name.getEditText().getText().toString();
            String email = registration_email.getEditText().getText().toString();
            String mobile = registration_mobile_number.getEditText().getText().toString();
            String pin = registration_pin.getEditText().getText().toString();
            String password = registration_signup_password.getEditText().getText().toString();
            String rePassword = registration_signup_password_again.getEditText().getText().toString();


            if (fullName.equals("") || email.equals("") || mobile.equals("") || pin.equals("")
                    || password.equals("") || rePassword.equals("")) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            }
            else {
                if (!password.equals(rePassword)) {
                    Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean checkUser = dbHelper.checkUsername(email);
                    if (checkUser == false) {
                        boolean correctInsert = dbHelper.insertData(fullName, email, mobile, pin, password);
                        if (correctInsert == true) {
                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            endActivity();
                        }
                        else {
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "There is already such user", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        registration_cancel.setOnClickListener(view -> {
            endActivity();
        });
    }

    private void endActivity() {
        finish();
    }
}