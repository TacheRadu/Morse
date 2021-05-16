package com.example.posttweet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText writeTweet;
    private Button sendTweet;
    private Button options;
    private BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.myToolBar);

        setSupportActionBar(toolbar);

        writeTweet = findViewById(R.id.etTextBox);
        sendTweet = findViewById(R.id.btnTweet);

        options = findViewById(R.id.etOptions);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);
                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout,
                        findViewById(R.id.bottom_sheet));
                sheetView.findViewById(R.id.set_everyone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        options.setText("Everyone");
                        bottomSheetDialog.dismiss();
                    }
                });
                sheetView.findViewById(R.id.set_followed_people).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        options.setText("Only followed");
                        bottomSheetDialog.dismiss();
                    }
                });
                sheetView.findViewById(R.id.set_mentioned_people).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        options.setText("Only mentioned");
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });

        sendTweet.setOnClickListener(v -> {
            String text = writeTweet.getText().toString();
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