package com.channels.twitter;

import android.os.Handler;

import com.channels.twitter.models.TwitterMessageInfo;
import com.morse.Message;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * @author Peiu Iulian
 *
 * This Class is responsible for message management: list, send, delete
 */
public class TwitterMessage implements Message {
    private Twitter twitter;
    private Long toUserId;
    private String messageText;

    public TwitterMessage(Twitter twitter, Long toUserId, String messageText) {
        this.twitter = twitter;
        this.toUserId = toUserId;
        this.messageText = messageText;
    }

    public TwitterMessage(Twitter twitter) {
        this.twitter = twitter;
        toUserId = null;
        messageText = null;
    }

    /**
     * Send a message with the properties: toUserId, messageText that were initialized via constructor
     * If the operation was done successfully, on the screen it will be printed the message:
     * "Direct message successfully sent to ...", otherwise "Failed to send a direct message: [cause]"
     */
    @Override
    public void send() {
        try {
            System.out.println("send message to: " + twitter.showUser(toUserId).getName());
            DirectMessage message = twitter.sendDirectMessage(twitter.showUser(toUserId).getScreenName(), messageText);
            System.out.println("Direct message successfully sent to " + message.getId());
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to send a direct message: " + te.getMessage());
        }
    }

    /**
     * Sends a message after an amount of minutes specified in params, delayedMinutes by
     * making a delayed call to send() method.
     * See ``TwitterMessage.send()``
     * @param delayedMinutes
     */
    @Override
    public void sendDelayed(long delayedMinutes) {
        long delayedMilliSeconds = delayedMinutes * 60 * 1000;
        (new Handler()).postDelayed(() -> {
            send();
        }, delayedMilliSeconds);
    }

    /**
     * List all message of a user specified by its id via params and will return them
     * @param fromUserId
     * @return messagesFromUserId
     */
    public List<TwitterMessageInfo> getMessages(Long fromUserId){
        try {
            String cursor = null;
            int count = 20;
            DirectMessageList currentMessages;
            List<TwitterMessageInfo> messagesFromUserId = new ArrayList<>();

            do {
                currentMessages = cursor == null ? twitter.getDirectMessages(count)
                        : twitter.getDirectMessages(count, cursor);
                for (DirectMessage message : currentMessages) {
                    if (message.getRecipientId() == toUserId) {
                        messagesFromUserId.add(new TwitterMessageInfo(message));
                    }
                }
                cursor = currentMessages.getNextCursor();
            } while (currentMessages.size() > 0 && cursor != null);

            return messagesFromUserId;

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get messages: " + te.getMessage());
        }
        return null;
    }

    @Override
    public Boolean delete(long id) {
        try {
            twitter.destroyDirectMessage(id);
            System.out.println("Successfully deleted message with id: " +id);
            return true;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to delete message: " + te.getMessage());
        }
        return false;
    }
}
