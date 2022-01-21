package com.example.multifactorauthenticationjava;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;

import data.DBHelper;

public class MainActivity extends AppCompatActivity {

    private ImageView main_touch_id_image;
    private Button main_signin;
    private Button main_signup;

    private TextInputLayout main_edit_email, main_edit_password;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private static final int REQUEST_CODE = 10101;

    private static int securityLevel = 0;

    private DBHelper dbHelper;

    private static String accountEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);

        main_touch_id_image = findViewById(R.id.main_touch_id_image);
        main_signup = findViewById(R.id.main_signup);

        main_edit_email = findViewById(R.id.main_edit_email);
        main_edit_password = findViewById(R.id.main_edit_password);


        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MainActivity", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MainActivity", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MainActivity", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }


        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                setSecurityLevel(3);
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build();


        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        main_touch_id_image.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        main_signin = findViewById(R.id.main_signin);
        main_signin.setOnClickListener(view -> {
            String email = main_edit_email.getEditText().getText().toString();
            String password = main_edit_password.getEditText().getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            } else {

                boolean checkUser = dbHelper.checkUsername(email);
                if (checkUser == true) {
                    boolean correctInsert = dbHelper.checkUsernameAndPassword(email, password);
                    if (correctInsert == true) {
                        Toast.makeText(this, "Entered successfully", Toast.LENGTH_SHORT).show();
                        accountEmail = email;
                        setSecurityLevel(1);
                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                    } else {
                        Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "There is no such user", Toast.LENGTH_SHORT).show();
                }
            }



        });

        main_signup.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        });
    }

    public static int getSecurityLevel() {
        return securityLevel;
    }

    public static void setSecurityLevel(int securityLevel) {
        MainActivity.securityLevel = securityLevel;
    }

    public static String getAccountEmail() {
        return accountEmail;
    }

}