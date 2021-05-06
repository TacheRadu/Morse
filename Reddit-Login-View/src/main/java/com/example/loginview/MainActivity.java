package com.example.loginview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    boolean isValid = false;
    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;
    private String Username = "Admin";
    private String Password = "1234";
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        eAttemptsInfo = findViewById(R.id.tvAttemptsInfo);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the details correctly", Toast.LENGTH_SHORT).show();
                } else {

                    isValid = validate(inputName, inputPassword);
                    if (!isValid) {
                        counter--;
                        Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();

                        eAttemptsInfo.setText("No. of attempts remaining: " + counter);

                        if (counter == 0) {
                            eLogin.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        //the code to go to new activity
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validate(String name, String password) {
        if (name.equals(Username) && password.equals(Password)) {
            return true;
        }

        return false;
    }
}