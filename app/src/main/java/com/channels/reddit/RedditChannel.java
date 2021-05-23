package com.channels.reddit;

import java.util.List;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;
import android.content.Intent;
import android.content.Context;


/**
 * Helper class for providing information and resources regarding the Reddit channel.
 *
 * @version 0.1.1
 */
public class RedditChannel implements Channel {
    private final Context mParentContext;

    public RedditChannel(Context parentContext) {
        this.mParentContext = parentContext;
    }

    @Override
    public void login() {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public void refreshChannel() {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public void getContacts(int contactNumber) {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public void checkCredentials() {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
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
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public Intent getIntent() {
        return new Intent(mParentContext, MainActivity.class);
    }
}
