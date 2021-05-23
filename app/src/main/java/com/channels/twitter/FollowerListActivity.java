package com.channels.twitter;

import com.R;
import twitter4j.IDs;
import java.util.List;
import android.os.Bundle;
import twitter4j.Twitter;
import com.morse.Constants;
import java.util.ArrayList;
import android.os.StrictMode;
import android.content.Intent;
import android.widget.ListView;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Class responsible for with handling of the followers of a user.
 *
 * @version 0.1.1
 */
public class FollowerListActivity extends AppCompatActivity {
    private Twitter mTwitter;
    private List<String> mFollowersList;
    ArrayAdapter<String> mAdapter;
    private List<Long> mIds;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.twitter_followers_activity);

        ListView followers = findViewById(R.id.followers);
        mFollowersList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                mFollowersList);
        followers.setAdapter(mAdapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        AccessToken token = new AccessToken(mSharedPreferences.getString(
                Constants.PREF_KEY_OAUTH_TOKEN, ""),
                mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET,""));

        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        mTwitter.setOAuthAccessToken(token);
        getFollowersList();

        followers.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(FollowerListActivity.this,
                    TwitterConversation.class);
            intent.putExtra("id", mIds.get(position));
            startActivity(intent);
        });
    }

    private void getFollowersList(){
        IDs ids;
        this.mIds = new ArrayList<>();

        try {
            ids = mTwitter.getFollowersIDs(mSharedPreferences.getLong(Constants.PREF_ID, 0),
                    -1);
            for(long id : ids.getIDs()){
                mFollowersList.add(mTwitter.showUser(id).getName());
                this.mIds.add(id);
            }
        } catch (TwitterException twitterException) {
            twitterException.printStackTrace();
        }
    }
}
