package com.morse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.R;

public class LoginActivity extends AppCompatActivity {
    private final String Username = "Admin";
    private final String Password = "1234";
    boolean isValid = false;
    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eName = findViewById(R.id.userCredential);
        ePassword = findViewById(R.id.userPassword);
        eLogin = findViewById(R.id.loginButton);
        eAttemptsInfo = findViewById(R.id.remainingAttemptsText);

        eLogin.setOnClickListener(v -> {
            String inputName = eName.getText().toString();
            String inputPassword = ePassword.getText().toString();

            if (inputName.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all the details correctly", Toast.LENGTH_SHORT).show();
            } else {

                isValid = validate(inputName, inputPassword);
                if (!isValid) {
                    counter--;
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();

                    eAttemptsInfo.setText(getString(R.string.attempts, counter));

                    if (counter == 0) {
                        eLogin.setEnabled(false);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    //the code to go to new activity
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validate(String name, String password) {
        return name.equals(Username) && password.equals(Password);
    }
}