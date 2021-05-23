package com.channels.androidsms;

import java.util.Objects;
import androidx.annotation.NonNull;


/**
 * Helper class for setting/getting the metadata of a particular contact of the user.
 *
 * @version 0.1.1
 */
public class ContactInfo {
    private String mContactId;
    private String mDisplayName;
    private String mPhoneNumber;
    private String mLastMessage;

    public ContactInfo() {}

    public ContactInfo(String contactId, String displayName, String phoneNumber,
                       String lastMessage) {
        this.mContactId = contactId;
        this.mDisplayName = displayName;
        this.mPhoneNumber = phoneNumber;
        this.mLastMessage = lastMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactInfo that = (ContactInfo) o;
        return mDisplayName.equals(that.mDisplayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mContactId, mDisplayName, mPhoneNumber);
    }

    @NonNull
    @Override
    public String toString() {
        return "ContactInfo{" +
                "contactId='" + mContactId + '\'' +
                ", displayName='" + mDisplayName + '\'' +
                ", phoneNumber='" + mPhoneNumber + '\'' +
                ", lastMessage='" + mLastMessage + '\'' +
                '}';
    }

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getLastMessage() {
        return mLastMessage;
    }
}