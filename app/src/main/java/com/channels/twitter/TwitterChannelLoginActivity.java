package com.channels.twitter;

import com.R;
import android.os.Bundle;
import com.morse.Constants;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.Callback;
import androidx.appcompat.app.AppCompatActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


/**
 * Class that handles the UI/UX for logging in to a Twitter account.
 * After a successful login, it will redirect the user to another activity.
 *
 * @author Ionuț Roșca
 * @author Ionuț Hristea
 * @version 0.2.1
 */
public class TwitterChannelLoginActivity extends AppCompatActivity {
    static TwitterSession sTwitterSession;
    static SharedPreferences sSharedPreferences;
    TwitterLoginButton mTwitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        sSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        setContentView(R.layout.twitter_login_activity);
        mTwitterLoginButton = findViewById(R.id.login_button);
        if (!isLoggedIn()) {
            mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    sTwitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = sTwitterSession.getAuthToken();

                    loginMethod(sTwitterSession, authToken);
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(getApplicationContext(), "Login fail",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void loginMethod(TwitterSession twitterSession, TwitterAuthToken token) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
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

        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private Boolean isLoggedIn() {
        return sSharedPreferences.getBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false);
    }

}
