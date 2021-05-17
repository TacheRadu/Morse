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
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (!mSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false))
            startActivityForResult(new Intent(HomeActivity.this, TwitterChannelLoginActivity.class), 0);
        setContentView(R.layout.activity_twitter_home);

        send = findViewById(R.id.send);
        tweet = findViewById(R.id.tweet);
        name = (TextView) findViewById(R.id.nametextView);
        name.setText(String.format(getResources().getString(R.string.hello_twitter), mSharedPreferences.getString(Constants.PREF_USER, "")));

        send.setOnClickListener(f -> openSendActivity());
    }

    private void openSendActivity(){
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            name = (TextView) findViewById(R.id.nametextView);
            name.setText(mSharedPreferences.getString(Constants.PREF_USER, ""));
        }
    }

}
