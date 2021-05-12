package com.channels.twitter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.R;

public class HomeActivity extends AppCompatActivity {
    TextView name;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_home);

        user = getIntent().getStringExtra("username");
        name = (TextView) findViewById(R.id.nametextView);
        name.setText(user);

    }
}
