package com.morse;


/**
 * Interface that provides methods to be implemented by the message classes of the channels that
 * Morse provides.
 *
 * @version 0.1.1
 */
public interface Message {
    void send();
    void sendDelayed(long delayedMinutes);

    Boolean delete(long id);
}