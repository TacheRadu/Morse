package com.morse;

import android.content.Intent;

import java.util.List;

/**
 *
 */
public interface Channel {
    /**
     *
     */
    void login();

    /**
     *
     */
    void refreshChannel();

    /**
     * @param contactNumber
     */
    void getContacts(int contactNumber);

    /**
     *
     */
    void checkCredentials();

    String getName();

    String getDescription();

    int getImage();

    /**
     * @param message
     * @param contact
     */
    void sendDelayedMessage(Message message, List<Contact> contact);

    Intent getIntent();

}