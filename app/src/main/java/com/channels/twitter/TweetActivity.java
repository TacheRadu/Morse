package com.channels.twitter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.R;
import com.morse.Constants;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TweetActivity extends AppCompatActivity {

    Twitter twitter;
    EditText status;
    Button postTweet;
    SwitchCompat switchCompat;
    EditText delay;
    StrictMode.ThreadPolicy policy;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createSession();
        postTweet.setOnClickListener(f -> postNowOrLater());

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    postTweet.setText(getString(R.string.delayed_tweet));
                    delay.setVisibility(View.VISIBLE);
                } else {
                    postTweet.setText(getString(R.string.tweet));
                    delay.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void createSession() {
        AccessToken token = new AccessToken(HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET, ""));
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        twitter.setOAuthAccessToken(token);

    }

    private void init() {
        setContentView(R.layout.twitter_tweet_activity);
        status = findViewById(R.id.tweetText);
        postTweet = findViewById(R.id.button);
        switchCompat = findViewById(R.id.switchCompat);
        delay = findViewById(R.id.delay);
        delay.setVisibility(View.INVISIBLE);
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void updateStatus() {
        try {
            twitter.updateStatus(status.getText().toString());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Tweet posted!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void postNowOrLater() {
        if (!switchCompat.isChecked()) {
            updateStatus();
            status.setText("");
        } else {
            long delayTime = Integer.parseInt(delay.getText().toString()) * 60 * 1000;
            Toast.makeText(getApplicationContext(),
                    "Delayed Tweet will be posted!", Toast.LENGTH_SHORT)
                    .show();
            new Thread(() -> {
                try {
                    Thread.sleep(delayTime);
                    updateStatus();
                    status.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
