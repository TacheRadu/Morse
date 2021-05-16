package com.channels.twitter;

import android.os.Handler;

import com.morse.Message;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterMessage implements Message {
    private Twitter twitter;
    private Long toUserId;
    private String messageText;

    public TwitterMessage(Twitter twitter, Long toUserId, String messageText) {
        this.twitter = twitter;
        this.toUserId = toUserId;
        this.messageText = messageText;
    }

    @Override
    public void send() {
        try {
            System.out.println("send message to: " + twitter.showUser(toUserId).getName());
            DirectMessage message = twitter.sendDirectMessage(twitter.showUser(toUserId).getScreenName(), messageText);
            System.out.println("Direct message successfully sent to " + message.getId());
            System.out.println(" details:" + message.toString());
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to send a direct message: " + te.getMessage());
        }
    }

    @Override
    public void sendDelayed(long delayedMinutes) {
        long delayedMilliSeconds = delayedMinutes * 60 * 1000;
        (new Handler()).postDelayed(() -> {
            send();
        }, delayedMilliSeconds);
    }

    @Override
    public Boolean delete(int id) {
        return false;
    }
}
