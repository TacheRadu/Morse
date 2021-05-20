package com.channels.twitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.R;
import com.morse.Constants;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.List;


/**
 * Class that handles the UI/UX for logging in to a Twitter account.
 * After a successful login, it will redirect the user to another activity.
 *
 * @author Ionuț Roșca
 * @author Ionuț Hristea
 * @version 0.2.0
 */
public class TwitterChannelLoginActivity extends AppCompatActivity {
    static TwitterSession session;

    static SharedPreferences sharedPreferences;

    TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        setContentView(R.layout.twitter_login_activity);
        loginButton = findViewById(R.id.login_button);
        if (!isLoggedIn()) {
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();

                    loginMethod(session, authToken);
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void loginMethod(TwitterSession twitterSession, TwitterAuthToken token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_KEY_OAUTH_SECRET, token.secret);
        editor.putString(Constants.PREF_KEY_OAUTH_TOKEN, token.token);
        editor.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, true);
        editor.putString(Constants.PREF_USER, twitterSession.getUserName());
        editor.putLong(Constants.PREF_ID, twitterSession.getUserId());
        editor.apply();
        TwitterChannelLoginActivity.this.setResult(0);
        TwitterChannelLoginActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private Boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false);
    }

}
