package com.morse;

import java.util.List;
import android.content.Intent;


/**
 * Interface that provides a "protocol" that must be followed by all channels that Morse provides.
 *
 * @version 0.1.1
 */
@SuppressWarnings("EmptyMethod")
public interface Channel {
    void login();
    void refreshChannel();
    void checkCredentials();

    int getImage();
    String getName();
    Intent getIntent();
    String getDescription();
    void getContacts(int contactNumber);

    void sendDelayedMessage(Message message, List<Contact> contact);
}