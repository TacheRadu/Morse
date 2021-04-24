package com.androidsms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;
import com.morse.Contact;
import java.util.ArrayList;
import java.util.List;

public class SmsContact extends AppCompatActivity implements Contact {

    private Context context;

    private String phNumber;

    public SmsContact(Context context) {
        this.context = context;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phNumber = phoneNumber;
    }

    @Override
    public void refreshMessageList() {

    }

    @Override
    public List<MessageInfo> getMessages(String fromAddress) {

        if (context == null)
            throw new NullPointerException();

        List<MessageInfo> messages = new ArrayList<>();

        Cursor cursor = context.getContentResolver()
                .query(Uri.parse("content://sms"), null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String currentAddress = cursor.getString(
                        cursor.getColumnIndexOrThrow("address"));

                if (currentAddress.equals(fromAddress)) {
                    MessageInfo currentMessage = new MessageInfo(cursor.getString(cursor.getColumnIndexOrThrow("person")),
                            getContactName(context,
                                    cursor.getString(cursor.getColumnIndexOrThrow("address"))),
                            cursor.getString(cursor.getColumnIndexOrThrow("body")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("seen")));

                    messages.add(currentMessage);
                }

            } while (cursor.moveToNext());
        }

        return messages;
    }

    private String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

}
