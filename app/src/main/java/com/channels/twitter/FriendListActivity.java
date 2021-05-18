package com.channels.twitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import com.R;
import com.morse.Constants;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class FriendListActivity extends AppCompatActivity {

    private Twitter twitter;
    private ListView followers;
    private List<String> followersList;
    ArrayAdapter<String> adapter;
    private List<Long> ids;
    private SharedPreferences mSharedPreferences;

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
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        AccessToken token = new AccessToken(mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET,""));
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        twitter.setOAuthAccessToken(token);
        getFriendsList();

        followers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendListActivity.this, TwitterConversation.class);
                intent.putExtra("id", ids.get(position));
                startActivity(intent);
            }
        });

    }

    private void getFriendsList(){
        IDs iDs;
        ids = new ArrayList<>();
        try {
            iDs = twitter.getFollowersIDs(mSharedPreferences.getLong(Constants.PREF_ID, 0), -1);
            for(long id : iDs.getIDs()){
                followersList.add(twitter.showUser(id).getName());
                ids.add(id);
            }
        } catch (TwitterException twitterException) {
            twitterException.printStackTrace();
        }
    }
}
