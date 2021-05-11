package com.channels.androidsms;

import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.channels.androidsms.activities.SmsChannelActivity;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import java.util.ArrayList;
import java.util.List;

public class SmsChannel implements Channel {

    public static final int PERMISSIONS_REQUEST_READ_SMS = 1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    AppCompatActivity parentActivity;


    public SmsChannel(AppCompatActivity activity) {
        parentActivity = activity;
    }

    public SmsChannel() {

    }


    public String getName() {
        return "SMS";
    }

    public String getDescription() {
        return "Direct SMS";
    }

    public int getImage() {
        return R.mipmap.sms;
    }

    public List<ContactInfo> getContacts() {

        Cursor cursor = parentActivity.getContentResolver()
                .query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        List<ContactInfo> contactInfoList = new ArrayList<>();

        SmsContact smsContact = new SmsContact(parentActivity);

        if (cursor.moveToFirst()) {
            do {

                String contactId = cursor.getString(cursor.getColumnIndexOrThrow("thread_id"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String displayName = smsContact.getContactName(parentActivity, address);
                String lastMessageText = smsContact.getLastMessageText(address);

                if (displayName == null)
                    displayName = address;

                ContactInfo currentContact = new ContactInfo(contactId, displayName, address, lastMessageText);

                addToContactInfoListIfNotExists(currentContact, contactInfoList);

            } while (cursor.moveToNext());
        }

        System.out.println(contactInfoList);

        cursor.close();

        return contactInfoList;

    }

    private void addToContactInfoListIfNotExists(ContactInfo currentContact, List<ContactInfo> contactInfoList) {
        if (contactInfoList.isEmpty()) {
            contactInfoList.add(currentContact);
            return;
        }

        ContactInfo lastContactInList = contactInfoList.get(contactInfoList.size() - 1);

        boolean contactExistsInList = false;
        for (ContactInfo contactInfo : contactInfoList)
            if (currentContact.getDisplayName().equals(contactInfo.getDisplayName())) {
                contactExistsInList = true;
                break;
            }

        if (!contactExistsInList)
            contactInfoList.add(currentContact);
    }





    @Override
    public void login() {

    }

    @Override
    public Intent getIntent() {
        return new Intent(parentActivity, SmsChannelActivity.class);
    }

    @Override
    public void refreshChannel() {

    }

    @Override
    public void getContacts(int contactNumber) {

    }

    @Override
    public void checkCredentials() {

    }

    @Override
    public void sendDelayedMessage(Message message, List<Contact> contact) {

    }
}
