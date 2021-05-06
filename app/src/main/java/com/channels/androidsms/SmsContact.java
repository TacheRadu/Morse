package com.channels.androidsms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.R;
import com.morse.Contact;

import java.util.ArrayList;
import java.util.List;


public class SmsContact extends AppCompatActivity implements Contact {

    ListView listView;
    List<String> nameList;
    List<String> messageList;
    Button sendButton;
    int size;
    private Context context;
    private String phNumber;
    private String name;
    private MyAdapterSendReceive adapter;

    public SmsContact() {

    }

    public SmsContact(Context context) {
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
    public void refreshMessageList() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SmsContact.this, new String[]{"android.permission.READ_SMS"}, 123);
        else {
            List<MessageInfo> messages = getMessages(phNumber);
            if (nameList != null)
                size = nameList.size();

            nameList = new ArrayList<>();
            messageList = new ArrayList<>();

            for (MessageInfo message : messages) {
                nameList.add(message.getPerson());
                messageList.add(message.getMessageText());
            }

            if (size != nameList.size()) {
                adapter = new MyAdapterSendReceive(context, nameList, messageList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }
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
                    String lastMessageText =
                            cursor.getString(1);
                    return lastMessageText;
                }
            } while (cursor.moveToNext());
        }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_contact);
        sendButton = findViewById(R.id.sendMessage);

        listView = findViewById(R.id.listView);
        context = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        phNumber = bundle.getString("phoneNumber");
        name = bundle.getString("name");
        refreshMessageList();
        adapter = new MyAdapterSendReceive(context, nameList, messageList);
        listView.setAdapter(adapter);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                refreshMessageList();
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        sendButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    SmsMessage messenger = new SmsMessage(this, phNumber,
                            ((EditText) findViewById(R.id.message)).getText().toString());
                    ((EditText) findViewById(R.id.message)).getText().clear();
                    messenger.send();
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
            }
        });
    }

}
