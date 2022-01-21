package com.example.multifactorauthenticationjava;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;

import data.DBHelper;

public class PinCodeActivity extends AppCompatActivity {

    public static final String ACTIVITY_FROM = "ActivityFrom";

    private Button pincode_submit, pincode_cancel;
    private ImageView pincode_touch_id_image;

    private TextInputLayout pincode_edit_pin;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private static final int REQUEST_CODE = 10101;

    String destination;

    private static String pinFromDB = "";

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);

        pincode_touch_id_image = findViewById(R.id.pincode_touch_id_image);

        pincode_submit = findViewById(R.id.pincode_submit);
        pincode_cancel = findViewById(R.id.pincode_cancel);

        pincode_edit_pin = findViewById(R.id.pincode_edit_pin);

        dbHelper = new DBHelper(PinCodeActivity.this);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("PinCodeActivity", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("PinCodeActivity", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("PinCodeActivity", "Biometric features are currently unavailable.");
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
        biometricPrompt = new BiometricPrompt(PinCodeActivity.this,
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
                MainActivity.setSecurityLevel(3);
                authenticationSucceed(destination);
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

        pincode_touch_id_image.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        pincode_submit.setOnClickListener(view -> {
            String pin = pincode_edit_pin.getEditText().getText().toString();


            if (pin.equals("")) {
                Toast.makeText(this, "Enter a PIN", Toast.LENGTH_SHORT).show();
            }
            else {
                //String email = MainActivity.getAccountEmail();

                dbHelper.initializePIN(MainActivity.getAccountEmail());

                if (pin.equals(pinFromDB)) {
                    Toast.makeText(this, "You entered PIN successfully", Toast.LENGTH_SHORT).show();
                    MainActivity.setSecurityLevel(2);
                    authenticationSucceed(destination);
                }
                else {
                    Toast.makeText(this, "PIN does not match", Toast.LENGTH_SHORT).show();
                }
            }


        });

        pincode_cancel.setOnClickListener(view -> {
            finish();
        });

        Intent intent = getIntent();
        destination = intent.getStringExtra(ACTIVITY_FROM).toString();
    }

    private void authenticationSucceed(String destination) {
        if (destination.equals("Transfer")) {
            startActivity(new Intent(PinCodeActivity.this, MoneyTransferActivity.class));
            finish();
        }
        else if(destination.equals("Password")) {
            startActivity(new Intent(PinCodeActivity.this, PasswordChangeActivity.class));
            finish();
        }
    }

    public static void setPinFromDB (String pin) {
        pinFromDB = pin;
    }
}