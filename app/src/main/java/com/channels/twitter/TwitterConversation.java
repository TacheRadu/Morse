package com.channels.twitter;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.R;
import com.channels.androidsms.MessagesAdapter;
import com.channels.androidsms.activities.SmsContactActivity;
import com.channels.twitter.models.TwitterMessageInfo;
import com.morse.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterConversation extends AppCompatActivity {

    private TwitterMessage messages;
    private Twitter twitter;
    private List<String> adapterList;
    private List<String> nameList;
    private List<TwitterMessageInfo> messagesList;
    private SwitchCompat switchCompat;
    private MessagesAdapter adapter;
    private ListView messagesView;
    private EditText delay;
    private Button send;
    private long id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createSession();
        createList();
        getMessageList();
        send.setOnClickListener(f -> sendMessage());

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    send.setText(getString(R.string.delayed));
                    delay.setVisibility(View.VISIBLE);
                }
                else{
                    send.setText(getString(R.string.type_message));
                    delay.setVisibility(View.INVISIBLE);
                }


            }
        });
    }

    private void init(){
        setContentView(R.layout.twitter_conversation_activity);
        messagesView = findViewById(R.id.conversationList);
        send = findViewById(R.id.sendMessage);
        switchCompat = findViewById(R.id.switchCompat);
        delay = findViewById(R.id.delay);
        delay.setVisibility(View.INVISIBLE);

    }

    private void createSession(){
        AccessToken token = new AccessToken(HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                HomeActivity.mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET,""));
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        twitter.setOAuthAccessToken(token);

    }

    private void createList(){
        id = getIntent().getLongExtra("id", -1);
        messages = new TwitterMessage(twitter, id);

        adapterList = new ArrayList<>();
        nameList = new ArrayList<>();
        adapter = new MessagesAdapter(TwitterConversation.this, nameList, adapterList);
        messagesView.setAdapter(adapter);
    }

    private void sendMessage(){
        EditText textBox = findViewById(R.id.message);
        messages.setMessageText(textBox.getText().toString());
        if(!switchCompat.isChecked()) {
            messages.send();
            adapterList.add(textBox.getText().toString());
            textBox.setText("");
            Toast.makeText(getApplicationContext(),
                    "Message sent!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Delayed message will be sent", Toast.LENGTH_SHORT).show();
            messages.setAdapterList(this.adapterList);
            textBox.setText("");
            messages.sendDelayed(Integer.parseInt(delay.getText().toString()));
            Toast.makeText(getApplicationContext(), "Delayed message sent", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    private void getMessageList(){
        messagesList = messages.getMessages(id);

        for(TwitterMessageInfo message : messagesList){
            adapterList.add(message.getMessageText());
            try {
                nameList.add(twitter.showUser(message.getSenderId()).getName());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(adapterList);
        Collections.reverse(nameList);
        adapter.notifyDataSetChanged();
    }

}
