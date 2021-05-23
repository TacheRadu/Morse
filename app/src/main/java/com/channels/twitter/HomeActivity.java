package com.channels.twitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.morse.Constants;

public class HomeActivity extends AppCompatActivity {
    TextView name;
    String user;
    static SharedPreferences mSharedPreferences;
    Button send, tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSharedPreferences();
        init();

        send.setOnClickListener(f -> openSendActivity());
        tweet.setOnClickListener(f -> openTweetActivity());
    }

    private void init(){
        setContentView(R.layout.activity_twitter_home);
        send = findViewById(R.id.send);
        tweet = findViewById(R.id.tweet);
        name = findViewById(R.id.nametextView);
        name.setText(String.format(getResources().getString(R.string.hello_twitter), mSharedPreferences.getString(Constants.PREF_USER, "")));
    }

    private void createSharedPreferences(){
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (!mSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false))
            startActivityForResult(new Intent(HomeActivity.this, TwitterChannelLoginActivity.class), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false))
            finish();
    }

    private void openSendActivity(){
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }

    private void openTweetActivity(){
        Intent intent = new Intent(this, TweetActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            name = findViewById(R.id.nametextView);
            name.setText(mSharedPreferences.getString(Constants.PREF_USER, ""));
        }
    }

}
