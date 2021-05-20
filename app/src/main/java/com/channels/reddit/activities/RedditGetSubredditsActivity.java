package com.channels.reddit.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.R;

import net.dean.jraw.RedditClient;


/**
 * Activity responsible with displaying the subreddits of a logged in user.
 *
 * @author  Ionuț Roșca
 * @version 0.1.0
 */
public class RedditGetSubredditsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reddit_get_subreddits);
    }
}
