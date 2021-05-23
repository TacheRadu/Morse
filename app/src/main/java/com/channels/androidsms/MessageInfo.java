package com.channels.androidsms;

import androidx.annotation.NonNull;

/**
 * Helper class for handling the message metadata.
 *
 * @version 0.1.1
 */
public class MessageInfo {
    private final Integer mId;
    private final String mSeen;
    private final String mDate;
    private final String mPerson;
    private final String mPhoneNumber;
    private final String mMessageText;

    public MessageInfo(Integer id, String person, String phoneNumber, String messageText,
                       String date, String seen) {
        this.mPerson = person;
        this.mPhoneNumber = phoneNumber;
        this.mMessageText = messageText;
        this.mDate = date;
        this.mSeen = seen;
        this.mId = id;
    }

    public Integer getId() {
        return mId;
    }

    @NonNull
    @Override
    public String toString() {
        return "MessageInfo{" +
                "person='" + mPerson + '\'' +
                ", phoneNumber='" + mPhoneNumber + '\'' +
                ", messageText='" + mMessageText + '\'' +
                ", date='" + mDate + '\'' +
                ", seen='" + mSeen + '\'' +
                ", id=" + mId +
                '}';
    }

    public String getPerson() {
        return mPerson;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getMessageText() {
        return mMessageText;
    }

    public String getDate() {
        return mDate;
    }

    public String getSeen() {
        return mSeen;
    }
}
