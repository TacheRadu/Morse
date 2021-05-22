package com.channels.twitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import java.util.List;

public class TwitterChannel implements Channel {
    private Context parentContext;
    public TwitterChannel(Context parentContext) {
        this.parentContext = parentContext;
    }

    public TwitterChannel() {

    }

    @Override
    public void login() {


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
        return "Twitter";
    }

    @Override
    public String getDescription() {
        return "It's what's happening / Twitter";
    }

    @Override
    public int getImage() {
        return R.mipmap.twitter;
    }

    @Override
    public void sendDelayedMessage(Message message, List<Contact> contact) {

    }

    @Override
    public Intent getIntent() {
        return new Intent(parentContext, HomeActivity.class);
    }
}
