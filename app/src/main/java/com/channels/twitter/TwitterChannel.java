package com.channels.twitter;

import com.R;
import java.util.List;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;
import android.content.Intent;
import android.content.Context;


/**
 * Helper class for providing information and resources regarding the Twitter channel.
 *
 * @version 0.1.1
 */
public class TwitterChannel implements Channel {
    private Context mParentContext;

    public TwitterChannel(Context parentContext) {
        this.mParentContext = parentContext;
    }

    public TwitterChannel() {}

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
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public Intent getIntent() {
        return new Intent(mParentContext, HomeActivity.class);
    }
}
