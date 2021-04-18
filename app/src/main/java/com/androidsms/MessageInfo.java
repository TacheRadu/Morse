package com.androidsms;

public class MessageInfo {
    private final String person;

    private final String phoneNumber;

    private final String messageText;

    private final String date;

    private final String seen;

    public MessageInfo(String person, String phoneNumber, String messageText, String date, String seen) {
        this.person = person;
        this.phoneNumber = phoneNumber;
        this.messageText = messageText;
        this.date = date;
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "person='" + person + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", messageText='" + messageText + '\'' +
                ", date='" + date + '\'' +
                ", seen='" + seen + '\'' +
                '}';
    }

}
