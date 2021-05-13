package com.channels.twitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.List;

import twitter4j.TwitterFactory;


/**
 * Class that handles the UI/UX for logging in to a Twitter account.
 * After a successful login, it will redirect the user to another activity.
 *
 * @author Ionuț Roșca
 * @author Ionuț Hristea
 * @version 0.2.0
 */
public class TwitterChannelLoginActivity extends AppCompatActivity implements Channel {
    private AppCompatActivity parentActivity = this;
    private EditText userCredential;
    private EditText userPassword;
    private TextView remainingAttempts;
    private boolean credentialsCheckPassed = false;
    private int currentNumberOfAvailableRetries = 3;
    TwitterSession session;

    private static SharedPreferences mSharedPreferences;
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    static final String PREF_USER = "username";


    TwitterLoginButton loginButton;

    public TwitterChannelLoginActivity() {
    }

    public TwitterChannelLoginActivity(AppCompatActivity activity) {
        this.parentActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
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
        } else {
            Intent intent = new Intent(TwitterChannelLoginActivity.this, HomeActivity.class);
            intent.putExtra("username", mSharedPreferences.getString(PREF_USER, ""));
            startActivity(intent);
        }
    }

    public void loginMethod(TwitterSession twitterSession, TwitterAuthToken token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PREF_KEY_OAUTH_SECRET, token.secret);
        editor.putString(PREF_KEY_OAUTH_TOKEN, token.token);
        editor.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
        editor.putString(PREF_USER, twitterSession.getUserName());
        editor.apply();
        Intent intent = new Intent(TwitterChannelLoginActivity.this, HomeActivity.class);
        intent.putExtra("username", mSharedPreferences.getString(PREF_USER, ""));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.err.println("session");
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private Boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    @Override
    public Intent getIntent() {
        System.out.println("Got intent");
        return new Intent(parentActivity, TwitterChannelLoginActivity.class);
    }

    @Override
    public void login() {
        /* TODO: Implement this! */
    }

    @Override
    public void refreshChannel() {
        /* TODO: Implement this! */
    }

    @Override
    public void getContacts(int contactNumber) {
        /* TODO: Implement this! */
    }

    @Override
    public void checkCredentials() {
        /* TODO: Implement this! */
    }

    @Override
    public String getName() {
        return "Twitter";
    }

    @Override
    public String getDescription() {
        return "It's what's happening / Twitter";
    }

    @Override
    public int getImage() {
        return R.drawable.twitter;
    }

    @Override
    public void sendDelayedMessage(Message message, List<Contact> contact) {
        /* TODO: Implement this! */
    }

    @Override
    public String toString() {
        return "¯\\_(ツ)_/¯";
    }
}
