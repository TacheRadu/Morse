package com.channels.twitter;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import com.R;
import com.morse.Constants;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class FriendListActivity extends AppCompatActivity {

    private Twitter twitter;
    private ListView followers;
    private List<String> followersList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.twitter_friends_activity);
        followers = findViewById(R.id.followers);
        followersList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, followersList);
        followers.setAdapter(adapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        AccessToken token = new AccessToken(HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET,""));
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        twitter.setOAuthAccessToken(token);
        getFriendsList();

    }

    private void getFriendsList(){
        IDs iDs;
        try {
            iDs = twitter.getFollowersIDs(HomeActivity.mSharedPreferences.getLong(Constants.PREF_ID, 0), -1);
            for(long id : iDs.getIDs()){
                followersList.add(twitter.showUser(id).getName());
                System.out.println(twitter.showUser(id).getName());
                TwitterMessage message = new TwitterMessage(twitter, id, "");
                System.out.println(message.getMessages(id));

            }
            System.out.println("done");
        } catch (TwitterException twitterException) {
            twitterException.printStackTrace();
        }
    }
}
