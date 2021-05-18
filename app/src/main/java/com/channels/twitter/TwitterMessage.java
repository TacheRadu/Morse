package com.channels.twitter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

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
public class TwitterMessage implements Message{
    private Twitter twitter;
    private Long toUserId;
    private String messageText;
    private List<String> adapterList;

    public void setAdapterList(List<String> adapterList) {
        this.adapterList = adapterList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    long delayedMilliSeconds;
    Context context;

    public TwitterMessage(Twitter twitter, Long toUserId, String messageText) {
        this.twitter = twitter;
        this.toUserId = toUserId;
        this.messageText = messageText;
    }

    public TwitterMessage(Twitter twitter) {
        super();
        this.twitter = twitter;
        toUserId = null;
        messageText = null;
    }

    public TwitterMessage(Twitter twitter, Long toUserId) {
        this.twitter = twitter;
        this.toUserId = toUserId;
        this.messageText = null;
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
        delayedMilliSeconds = delayedMinutes * 60 * 1000;
        new Thread(() -> {
            try {
                Thread.sleep(delayedMilliSeconds);
                send();
                adapterList.add(this.messageText);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
                    if (message.getRecipientId() == toUserId || message.getSenderId() == toUserId) {
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

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

}
