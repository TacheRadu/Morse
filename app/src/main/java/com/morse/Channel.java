package com.morse;

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

    /**
     * @param message
     * @param contact
     */
    void sendDelayedMessage(Message message, List<Contact> contact);

}