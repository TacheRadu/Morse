package com.channels.androidsms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;

import androidx.annotation.RequiresApi;

import com.morse.Contact;

import java.util.ArrayList;
import java.util.List;


public class SmsContact implements Contact {

    private Context context;
    private String phNumber;
    private String name;

    public SmsContact() {

    }

    public SmsContact(Context context, String phNumber, String name) {
        this.context = context;
        this.phNumber = phNumber;
        this.name = name;
    }
    public SmsContact(Context context){
        this.context = context;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phNumber = phoneNumber;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<MessageInfo> getMessages(String fromAddress) {

        if (context == null)
            throw new NullPointerException();

        Cursor inboxSMSsCursor = context.getContentResolver()
                .query(Telephony.Sms.Inbox.CONTENT_URI,
                        new String[]{Telephony.Sms.Inbox._ID,
                                Telephony.Sms.Inbox.ADDRESS,
                                Telephony.Sms.Inbox.BODY,
                                Telephony.Sms.Inbox.DATE,
                                Telephony.Sms.Inbox.SEEN},
                        null, null, null);

        Cursor sentSMSsCursor = context.getContentResolver().query(
                Telephony.Sms.Sent.CONTENT_URI, new String[]{
                        Telephony.Sms.Sent._ID,
                        Telephony.Sms.Sent.ADDRESS,
                        Telephony.Sms.Sent.BODY,
                        Telephony.Sms.Sent.DATE,
                        Telephony.Sms.Sent.SEEN},
                null, null, null);

        List<MessageInfo> inboxMessagesFromAddress =
                getMessagesFromCursor(inboxSMSsCursor, fromAddress, getContactName(context, fromAddress));

        List<MessageInfo> sentMessagesToAddress =
                getMessagesFromCursor(sentSMSsCursor, fromAddress, "Me");

        List<MessageInfo> messages = new ArrayList<>();
        messages.addAll(inboxMessagesFromAddress);
        messages.addAll(sentMessagesToAddress);

        messages.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        return messages;
    }

    private List<MessageInfo> getMessagesFromCursor(Cursor currentCursor, String fromAddress, String givenName) {
        List<MessageInfo> messages = new ArrayList<>();

        if (currentCursor.moveToFirst()) {
            do {
                String currentAddress = currentCursor.getString(1);
                if (currentAddress.equals(fromAddress)) {
                    MessageInfo currentMessageSent = new MessageInfo(
                            Integer.parseInt(currentCursor.getString(0)),
                            givenName,
                            currentCursor.getString(1),
                            currentCursor.getString(2),
                            currentCursor.getString(3),
                            currentCursor.getString(4));
                    messages.add(currentMessageSent);
                }

            } while (currentCursor.moveToNext());
        }
        return messages;
    }

    public String getLastMessageText(String fromAddress) {

            if (context == null)
                throw new NullPointerException();

            Cursor cursor = context.getContentResolver()
                    .query(Telephony.Sms.CONTENT_URI,
                            new String[]{
                                    Telephony.Sms.ADDRESS,
                                    Telephony.Sms.BODY},
                            null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String currentAddress = cursor.getString(0);

                    if (currentAddress.equals(fromAddress)) {
                        return cursor.getString(1);
                    }
                } while (cursor.moveToNext());
            }

        cursor.close();
        return "";
    }

    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null)
            return null;

        String contactName = "";
        if (cursor.moveToFirst())
            contactName = cursor.getString(0);

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        if (contactName.equals(""))
            return phoneNumber;

        return contactName;
    }



}
