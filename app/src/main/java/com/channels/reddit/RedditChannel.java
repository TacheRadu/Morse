package com.channels.reddit;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import net.dean.jraw.http.UserAgent;

import java.util.List;

public class RedditChannel implements Channel {

    private AppCompatActivity parentActivity;
    public RedditChannel(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public void login() {


        UserAgent userAgent = new UserAgent("Morse","com.reddit", "v0.1", "Morse6969");
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
        return new Intent(parentActivity, MainActivity.class);
    }
}
