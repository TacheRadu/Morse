package com.channels.twitter;

import com.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import twitter4j.Twitter;
import com.morse.Constants;
import android.widget.Toast;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.TwitterException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


/**
 * The activity responsible of the posting of a tweet.
 *
 * @version 0.1.1
 */
public class TweetActivity extends AppCompatActivity {
    Twitter mTwitter;
    EditText mStatus;
    Button mPostTweet;
    SwitchCompat mSwitchCompat;
    EditText mDelay;
    StrictMode.ThreadPolicy mPolicy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createSession();
        mPostTweet.setOnClickListener(f -> postNowOrLater());

        mSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mPostTweet.setText(getString(R.string.delayed_tweet));
                mDelay.setVisibility(View.VISIBLE);
            } else {
                mPostTweet.setText(getString(R.string.tweet));
                mDelay.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void createSession() {
        AccessToken token = new AccessToken(
                HomeActivity.sSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                HomeActivity.sSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET, ""));

        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        mTwitter.setOAuthAccessToken(token);

    }

    private void init() {
        setContentView(R.layout.twitter_tweet_activity);
        mStatus = findViewById(R.id.tweetText);
        mPostTweet = findViewById(R.id.button);
        mSwitchCompat = findViewById(R.id.switchCompat);
        mDelay = findViewById(R.id.delay);
        mDelay.setVisibility(View.INVISIBLE);
        mPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(mPolicy);
    }

    private void updateStatus() {
        try {
            mTwitter.updateStatus(mStatus.getText().toString());
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(),
                    "Tweet posted!", Toast.LENGTH_LONG).show());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void postNowOrLater() {
        if (!mSwitchCompat.isChecked()) {
            updateStatus();
            mStatus.setText("");
        } else {
            long delayTime = Integer.parseInt(mDelay.getText().toString()) * 60 * 1000;
            Toast.makeText(getApplicationContext(),
                    "Delayed Tweet will be posted!", Toast.LENGTH_SHORT)
                    .show();
            new Thread(() -> {
                try {
                    Thread.sleep(delayTime);
                    updateStatus();
                    mStatus.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
