package com.channels.twitter;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.channels.twitter.models.TwitterMessageInfo;
import com.morse.Constants;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterConversation extends AppCompatActivity {

    private TwitterMessage messages;
    private Twitter twitter;
    private List<String> messageList;
    private ArrayAdapter<String> adapter;
    private ListView messagesView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_conversation_activity);
        messagesView = findViewById(R.id.conversationList);

        AccessToken token = new AccessToken(HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET,""));
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        twitter.setOAuthAccessToken(token);

        long id = getIntent().getLongExtra("id", -1);
        messages = new TwitterMessage(twitter, id);

        messageList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        List<TwitterMessageInfo> messagesList = messages.getMessages(id);

        for(TwitterMessageInfo message : messagesList){
            messageList.add(message.getMessageText());
        }
        messagesView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


}
