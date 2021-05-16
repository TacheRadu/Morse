package com.example.posttweet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText writePost;
    private Button sendPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.myToolBar);

        setSupportActionBar(toolbar);

        writePost = findViewById(R.id.etComment);
        sendPost = findViewById(R.id.btnPost);

        sendPost.setOnClickListener(v -> {
            String text = writePost.getText().toString();
            if(text.isEmpty()){
                Toast.makeText(MainActivity.this, "Empty tweet", Toast.LENGTH_SHORT).show();
            }
            else {
                //the code to go to new activity
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}