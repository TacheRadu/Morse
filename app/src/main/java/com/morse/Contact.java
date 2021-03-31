package com.morse;

/**
 *
 */
public interface Contact {

    /**
     * @param messageNumber
     */
    void getMessages(int messageNumber);

    /**
     * @param message
     */
    void sendMessage(Message message);

    /**
     *
     */
    void refreshMessageList();

}