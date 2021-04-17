package com.morse;

/**
 *
 */
public interface Message {

    /**
     *
     */
    void send();

    /**
     * @author  Peiu Iulian
     */
    void sendDelayed(long delayedMinutes);

}