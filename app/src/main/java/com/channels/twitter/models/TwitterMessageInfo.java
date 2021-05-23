package com.channels.twitter.models;

import java.util.Date;
import twitter4j.DirectMessage;
import org.jetbrains.annotations.NotNull;


/**
 * This Class is a model for a TwitterMessage
 *
 * @author Peiu Iulian
 */
public class TwitterMessageInfo {
    private final long mMessageId;
    private final long mSenderId;
    private final Date mDate;
    private final String mMessageText;

    public TwitterMessageInfo(DirectMessage message) {
        this.mMessageId = message.getId();
        this.mSenderId = message.getSenderId();
        this.mMessageText = message.getText();
        this.mDate = message.getCreatedAt();
    }

    public long getSenderId() {
        return mSenderId;
    }

    public long getMessageId() {
        return mMessageId;
    }

    public String getMessageText() {
        return mMessageText;
    }

    @NotNull
    @Override
    public String toString() {
        return "TwitterMessageInfo{" +
                "messageId=" + mMessageId +
                ", senderId=" + mSenderId +
                ", date=" + mDate +
                ", messageText='" + mMessageText + '\'' +
                '}';
    }
}
