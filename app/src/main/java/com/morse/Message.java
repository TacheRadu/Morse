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
     *
     */
    void sendDelayed(long delayedMinutes);

    /**
     *
     */
    Boolean delete(int id);

}