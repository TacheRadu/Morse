package com.channels.androidsms;

public class MessageInfo {
    private final String person;

    private final String phoneNumber;

    private final String messageText;

    private final String date;

    private final String seen;

    private final Integer id;

    public Integer getId() {
        return id;
    }

    public MessageInfo(String person, String phoneNumber, String messageText, String date, String seen, Integer id) {
        this.person = person;
        this.phoneNumber = phoneNumber;
        this.messageText = messageText;
        this.date = date;
        this.seen = seen;
        this.id = id;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "person='" + person + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", messageText='" + messageText + '\'' +
                ", date='" + date + '\'' +
                ", seen='" + seen + '\'' +
                ", id=" + id +
                '}';
    }

    public String getPerson() {
        return person;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getDate() {
        return date;
    }

    public String getSeen() {
        return seen;
    }
}
