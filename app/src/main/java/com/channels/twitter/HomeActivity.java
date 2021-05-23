package com.channels.twitter;

import com.R;
import android.os.Bundle;
import com.morse.Constants;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;


/**
 * The Twitter home activity that will be presented to the user after logging in.
 *
 * @version 0.1.1
 */
public class HomeActivity extends AppCompatActivity {
    TextView mName;
    static SharedPreferences sSharedPreferences;
    Button mButton;
    Button mTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSharedPreferences();
        init();

        mButton.setOnClickListener(f -> openSendActivity());
        mTweet.setOnClickListener(f -> openTweetActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false)) {
            finish();
        }
    }

    private void init(){
        setContentView(R.layout.activity_twitter_home);
        mButton = findViewById(R.id.send);
        mTweet = findViewById(R.id.tweet);
        mName = findViewById(R.id.nametextView);
        mName.setText(String.format(getResources().getString(R.string.hello_twitter),
                sSharedPreferences.getString(Constants.PREF_USER, "")));
    }

    private void createSharedPreferences(){
        sSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (!sSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false))
            startActivityForResult(new Intent(HomeActivity.this,
                    TwitterChannelLoginActivity.class), 0);
    }

    private void openSendActivity(){
        Intent intent = new Intent(this, FollowerListActivity.class);
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
            mName = findViewById(R.id.nametextView);
            mName.setText(sSharedPreferences.getString(Constants.PREF_USER, ""));
        }
    }
}
