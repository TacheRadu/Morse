package com.channels.twitter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    StrictMode.ThreadPolicy policy;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createSession();
        postTweet.setOnClickListener(f -> updateStatus());

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

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void updateStatus() {
        try {
            twitter.updateStatus(status.getText().toString());
            Toast.makeText(getApplicationContext(),
                    "Tweet Posted!", Toast.LENGTH_SHORT)
                    .show();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
