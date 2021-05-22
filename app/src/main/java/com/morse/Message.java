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
    Boolean delete(long id);

}