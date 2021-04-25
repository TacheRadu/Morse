package com.androidsms;

import java.util.Objects;

public class ContactInfo {
    private String contactId;
    private String displayName;
    private String phoneNumber;
    private String lastMessage;

    public ContactInfo(String contactId, String displayName, String phoneNumber, String lastMessage) {
        this.contactId = contactId;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.lastMessage = lastMessage;
    }

    public ContactInfo(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return contactId.equals(that.contactId) &&
                displayName.equals(that.displayName) &&
                phoneNumber.equals(that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId, displayName, phoneNumber);
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "contactId='" + contactId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                '}';
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}