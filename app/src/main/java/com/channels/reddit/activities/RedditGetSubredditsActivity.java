package com.channels.reddit.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.R;

import net.dean.jraw.RedditClient;
import net.dean.jraw.Version;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Account;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.DeferredPersistentTokenStore;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.oauth.StatefulAuthHelper;
import net.dean.jraw.oauth.TokenStore;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.pagination.Paginator;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity responsible with displaying the subreddits of a logged in user.
 *
 * @author  Ionuț Roșca
 * @version 0.1.0
 */
public class RedditGetSubredditsActivity extends AppCompatActivity {
    private static String sRedditRegisteredAppClientId;
    private static String sRedditRegisteredAppUsername;
    private static String sRedditRegisteredAppPassword;
    private static String sRedditRegisteredAppPublicKey;
    private static String sRedditRegisteredAppPrivateKey;
    private static final String REDIRECT_URI = "http://www.example.com/my_redirect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_get_subreddits);

        sRedditRegisteredAppClientId = getString(R.string.com_reddit_sdk_android_REDDIT_APP_CLIENT_ID);
        sRedditRegisteredAppUsername = getString(R.string.com_reddit_sdk_android_REDDIT_APP_USERNAME);
        sRedditRegisteredAppPassword = getString(R.string.com_reddit_sdk_android_REDDIT_APP_PASSWORD);
        sRedditRegisteredAppPrivateKey = getString(R.string.com_reddit_sdk_android_REDDIT_APP_PUBLIC_KEY);
        sRedditRegisteredAppPublicKey = getString(R.string.com_reddit_sdk_android_REDDIT_APP_PRIVATE_KEY);

        this.getSubreddits();
    }

    private void getSubreddits() {
        UserAgent userAgent =
                new UserAgent("Morse", "com.channels.reddit", Version.get(), "morse_boss_user");
        Credentials credentials = Credentials.installedApp(sRedditRegisteredAppClientId, REDIRECT_URI);

        NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(userAgent);
        final StatefulAuthHelper helper = OAuthHelper.interactive(networkAdapter, credentials);
    }
}
