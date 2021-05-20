package com.channels.reddit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.channels.reddit.activities.RedditGetSubredditsActivity;
import com.morse.Constants;


public class HomeActivity extends AppCompatActivity {
    TextView name;
    String user;
    static SharedPreferences mSharedPreferences;
    private Button mGetSubredditsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSharedPreferences();
        initializeActivity();

        /* Add listeners for different buttons of the activity */
        mGetSubredditsButton.setOnClickListener(f -> getSubreddits());
    }

    private void initializeActivity(){
        setContentView(R.layout.activity_reddit_home);
        mGetSubredditsButton = findViewById(R.id.get_subreddits);
//        name = (TextView) findViewById(R.id.nametextView);
//        name.setText(String.format(getResources().getString(R.string.hello_twitter),
//                mSharedPreferences.getString(Constants.PREF_USER, "")));
    }

    private void createSharedPreferences(){
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (!mSharedPreferences.getBoolean(Constants.PREF_KEY_REDDIT_USER_LOGGED_IN, false))
            startActivityForResult(new Intent(com.channels.reddit.HomeActivity.this,
                    RedditChannelLoginActivity.class), 0);
    }

    private void getSubreddits(){
        Intent intent = new Intent(this, RedditGetSubredditsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            name = (TextView) findViewById(R.id.nametextView);
            name.setText(mSharedPreferences.getString(Constants.PREF_USER, ""));
        }
    }

}
