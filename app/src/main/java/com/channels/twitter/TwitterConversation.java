package com.channels.twitter;

import com.R;
import twitter4j.Twitter;
import android.os.Bundle;
import android.view.View;
import com.morse.Constants;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Class that handles communications withing a conversation on Twitter.
 *
 * @version 0.1.1
 */
public class TwitterConversation extends AppCompatActivity {
    private TwitterMessage mTwitterMessage;
    private Twitter mTwitter;
    private SwitchCompat mSwitchCompat;
    private EditText mDelay;
    private TextView mText;
    private Button mSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        createSession();
        createList();
        mSend.setOnClickListener(f -> sendMessage());

        mSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                mSend.setText(getString(R.string.delayed));
                mDelay.setVisibility(View.VISIBLE);
            }
            else{
                mSend.setText(getString(R.string.type_message));
                mDelay.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void init(){
        setContentView(R.layout.twitter_conversation_activity);
        mSend = findViewById(R.id.sendMessage);
        mSwitchCompat = findViewById(R.id.switchCompat);
        mDelay = findViewById(R.id.delay);
        mDelay.setVisibility(View.INVISIBLE);
        mText = findViewById(R.id.mesaj);

    }

    private void createSession(){
        AccessToken token = new AccessToken(
                HomeActivity.sSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, ""),
                HomeActivity.sSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET,""));
        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        mTwitter.setOAuthAccessToken(token);
    }

    private void createList(){
        long id = getIntent().getLongExtra("id", -1);
        mTwitterMessage = new TwitterMessage(mTwitter, id);
        mTwitterMessage.setContext(getApplicationContext());

        try {
            mText.setText(String.format(getResources().getString(R.string.send_text), mTwitter.showUser(id).getName()));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(){
        EditText textBox = findViewById(R.id.message);
        mTwitterMessage.setMessageText(textBox.getText().toString());

        if (!mSwitchCompat.isChecked()) {
            mTwitterMessage.send();
        } else{
            Toast.makeText(getApplicationContext(), "Delayed message will be send",
                    Toast.LENGTH_SHORT).show();
            mTwitterMessage.sendDelayed(Integer.parseInt(mDelay.getText().toString()));
        }
        textBox.setText("");
    }
}
