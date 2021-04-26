package com.androidsms;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_main);


        listView = (ListView) findViewById(R.id.lstContacts);
        requestContactPermission();
    }

    public String getName(){
        return "SMS";
    }

    public String getDescription(){
        return "Direct SMS";
    }

    public int getImage(){
        return R.drawable.sms;
    }

    private void getContacts(){

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

        ContactInfo lastContactInList = contactInfoList.get(contactInfoList.size()-1);

        Boolean contactExistsInList = false;
        for (ContactInfo contactInfo : contactInfoList)
            if (currentContact.getDisplayName().equals(contactInfo.getDisplayName()))
                contactExistsInList = true;

        if (!contactExistsInList)
            contactInfoList.add(currentContact);
    }


    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_SMS)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Read contacts access needed");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setMessage("Please enable access to contacts.");
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                requestPermissions(
                                        new String[]
                                                {android.Manifest.permission.READ_SMS}
                                        , PERMISSIONS_REQUEST_READ_SMS);
                            }
                        });
                        builder.show();
                    }
                    else{
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                PERMISSIONS_REQUEST_READ_CONTACTS);
                    }

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_SMS},
                            PERMISSIONS_REQUEST_READ_SMS);
                }
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
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
    public Intent getIntent(){
        return new Intent(parentActivity, SmsChannel.class);
    }

    public SmsChannel(AppCompatActivity activity){
        parentActivity = activity;
    }

    public SmsChannel(){

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
