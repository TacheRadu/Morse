package com.channels.androidsms;

import com.R;
import java.util.List;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;
import java.util.ArrayList;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import com.channels.androidsms.activities.SmsChannelActivity;


/**
 * Helper class for providing information and resources regarding the SMS channel.
 *
 * @version 0.1.1
 */
public class SmsChannel implements Channel {
    public static final int PERMISSIONS_REQUEST_READ_SMS = 1;
    Context mParentContext;

    public SmsChannel(Context context) {
        mParentContext = context;
    }

    public SmsChannel() {}

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
        Cursor cursor = mParentContext.getContentResolver()
                .query(Telephony.Sms.CONTENT_URI, null, null, null,
                        null);

        List<ContactInfo> contactInfoList = new ArrayList<>();
        SmsContact smsContact = new SmsContact(mParentContext);

        if (cursor.moveToFirst()) {
            do {
                String contactId = cursor.getString(
                        cursor.getColumnIndexOrThrow("thread_id"));
                String address = cursor.getString(
                        cursor.getColumnIndexOrThrow("address"));
                String displayName = smsContact.getContactName(mParentContext, address);
                String lastMessageText = smsContact.getLastMessageText(address);

                if (displayName == null) {
                    displayName = address;
                }

                ContactInfo currentContact = new ContactInfo(contactId, displayName, address,
                        lastMessageText);
                addToContactInfoListIfNotExists(currentContact, contactInfoList);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contactInfoList;
    }

    private void addToContactInfoListIfNotExists(ContactInfo currentContact,
                                                 List<ContactInfo> contactInfoList) {
        if (contactInfoList.isEmpty()) {
            contactInfoList.add(currentContact);
            return;
        }

        boolean contactExistsInList = false;
        for (ContactInfo contactInfo : contactInfoList) {
            if (currentContact.getDisplayName().equals(contactInfo.getDisplayName())) {
                contactExistsInList = true;
                break;
            }
        }

        if (!contactExistsInList)
            contactInfoList.add(currentContact);
    }

    @Override
    public void login() {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public Intent getIntent() {
        return new Intent(mParentContext, SmsChannelActivity.class);
    }

    @Override
    public void refreshChannel() {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public void getContacts(int contactNumber) {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public void checkCredentials() {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }

    @Override
    public void sendDelayedMessage(Message message, List<Contact> contact) {
        /*
         * Not needed to be implemented, but defined to comply with the interface
         */
    }
}
