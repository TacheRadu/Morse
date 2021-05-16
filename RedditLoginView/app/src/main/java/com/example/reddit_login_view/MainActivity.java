package com.example.reddit_login_view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button etConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etConnect = findViewById(R.id.etConnect);

        etConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                //the code to go to new activity
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

    }
}