package com.channels.twitter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.R;
import com.morse.Constants;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterConversation extends AppCompatActivity {

    private TwitterMessage messages;
    private Twitter twitter;
    private SwitchCompat switchCompat;
    private EditText delay;
    private TextView text;
    private Button send;
    private long id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createSession();
        createList();
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
        send = findViewById(R.id.sendMessage);
        switchCompat = findViewById(R.id.switchCompat);
        delay = findViewById(R.id.delay);
        delay.setVisibility(View.INVISIBLE);
        text = findViewById(R.id.mesaj);

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
        messages.setContext(getApplicationContext());
        try {
            text.setText(String.format(getResources().getString(R.string.send_text),twitter.showUser(id).getName()));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(){
        EditText textBox = findViewById(R.id.message);
        messages.setMessageText(textBox.getText().toString());
        if(!switchCompat.isChecked()) {
            messages.send();
        }
        else{
            Toast.makeText(getApplicationContext(), "Delayed message will be send", Toast.LENGTH_SHORT).show();
            messages.sendDelayed(Integer.parseInt(delay.getText().toString()));
        }
        textBox.setText("");

    }

}
