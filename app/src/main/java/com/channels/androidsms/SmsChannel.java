package com.channels.androidsms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.R;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import java.util.ArrayList;
import java.util.List;

public class SmsChannel extends AppCompatActivity implements Channel {

    public static final int PERMISSIONS_REQUEST_READ_SMS = 1;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    MyCustomAdapter dataAdapter = null;
    ListView listView;
    List<ContactInfo> contactInfoList;
    AppCompatActivity parentActivity;
    private String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS};

    public SmsChannel(AppCompatActivity activity) {
        parentActivity = activity;
    }

    public SmsChannel() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_main);


        listView = (ListView) findViewById(R.id.lstContacts);
        requestContactPermission();
    }

    public String getName() {
        return "SMS";
    }

    public String getDescription() {
        return "Direct SMS";
    }

    public int getImage() {
        return R.drawable.sms;
    }

    private void getContacts() {

        Cursor cursor = getContentResolver()
                .query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        contactInfoList = new ArrayList<ContactInfo>();

        SmsContact smsContact = new SmsContact(this);

        if (cursor.moveToFirst()) {
            do {

                String contactId = cursor.getString(cursor.getColumnIndexOrThrow("thread_id"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String displayName = smsContact.getContactName(this, address);
                String lastMessageText = smsContact.getLastMessageText(address);

                if (displayName == null)
                    displayName = address;

                ContactInfo currentContact = new ContactInfo(contactId, displayName, address, lastMessageText);

                addToContactInfoListIfNotExists(currentContact, contactInfoList);

            } while (cursor.moveToNext());
        }

        System.out.println(contactInfoList);

        cursor.close();

        dataAdapter = new MyCustomAdapter(SmsChannel.this, R.layout.contact_info, contactInfoList);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(SmsChannel.this, SmsContact.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", contactInfoList.get(position).getPhoneNumber());
            bundle.putString("name", contactInfoList.get(position).getDisplayName());
            bundle.putString("lastMessage", contactInfoList.get(position).getLastMessage());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void addToContactInfoListIfNotExists(ContactInfo currentContact, List<ContactInfo> contactInfoList) {
        if (contactInfoList.isEmpty()) {
            contactInfoList.add(currentContact);
            return;
        }

        ContactInfo lastContactInList = contactInfoList.get(contactInfoList.size() - 1);

        Boolean contactExistsInList = false;
        for (ContactInfo contactInfo : contactInfoList)
            if (currentContact.getDisplayName().equals(contactInfo.getDisplayName()))
                contactExistsInList = true;

        if (!contactExistsInList)
            contactInfoList.add(currentContact);
    }

    private boolean hasPermissions() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestContactPermission() {
        if (!hasPermissions()) {
            requestPermissions(PERMISSIONS, 1);
        } else
            getContacts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void login() {

    }

    @Override
    public Intent getIntent() {
        return new Intent(parentActivity, SmsChannel.class);
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
