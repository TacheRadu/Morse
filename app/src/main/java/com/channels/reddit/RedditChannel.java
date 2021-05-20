package com.channels.reddit;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;

import java.util.List;

import twitter4j.User;

import static android.os.Build.VERSION_CODES.R;

public class RedditChannel implements Channel {

    private AppCompatActivity parentActivity;
    public RedditChannel(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public void login() {
        final String username = "morse_boss_user";
        final String password = "dajsndajdbsad232";

        UserAgent userAgent = new UserAgent("Morse","com.reddit", "v0.1", "user");
//        RedditClient redditClient = new RedditClient(userAgent);
    }

    @Override
    public void refreshChannel() {

    }

    @Override
    public void getContacts(int contactNumber) {

    }

    @Override
    public void checkCredentials() {

    }

    @Override
    public String getName() {
        return "Reddit";
    }

    @Override
    public String getDescription() {
        return "The front page of the internet";
    }

    @Override
    public int getImage() {
        return com.R.mipmap.reddit;
    }

    @Override
    public void sendDelayedMessage(Message message, List<Contact> contact) {

    }

    @Override
    public Intent getIntent() {
        return new Intent(parentActivity, RedditLoginActivity.class);
    }
}
