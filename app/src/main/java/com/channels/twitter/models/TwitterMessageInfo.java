package com.channels.twitter.models;

import java.util.Date;

import twitter4j.DirectMessage;

/**
 * @author Peiu Iulian
 *
 * This Class is a model for a TwitterMessage
 */
public class TwitterMessageInfo {
    private final long messageId;

    private final long senderId;

    private final Date date;

    private final String messageText;

    public TwitterMessageInfo(DirectMessage message) {
        this.messageId = message.getId();
        this.senderId = message.getSenderId();
        this.messageText = message.getText();
        this.date = message.getCreatedAt();
    }

    @Override
    public String toString() {
        return "TwitterMessageInfo{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", date=" + date +
                ", messageText='" + messageText + '\'' +
                '}';
    }
}
