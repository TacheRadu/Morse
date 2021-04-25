package com.androidsms;

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
import java.util.Comparator;
import java.util.List;


public class SmsContact extends AppCompatActivity implements Contact {

    private Context context;

    private String phNumber;

    private String name;

    private MyAdapterSendReceive adapter;

    ListView listView;
    List<String> nameList;
    List<String> messageList;
    Button sendButton;
    int size;


    public SmsContact(){

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
            if(nameList != null)
                size = nameList.size();

            nameList = new ArrayList<>();
            messageList = new ArrayList<>();

            for (MessageInfo message : messages) {
                nameList.add(message.getPerson());
                messageList.add(message.getMessageText());
            }

           if(size!=nameList.size()){
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

        List<MessageInfo> messages = new ArrayList<>();

        Cursor cursor = context.getContentResolver()
                .query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                String currentAddress = cursor.getString(
                        cursor.getColumnIndexOrThrow("address"));

                if (currentAddress.equals(fromAddress)) {
                    MessageInfo currentMessageReceived = new MessageInfo(getContactName(context,
                            cursor.getString(cursor.getColumnIndexOrThrow("address"))),
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getString(cursor.getColumnIndexOrThrow("body")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("seen")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                    messages.add(currentMessageReceived);
                }

            } while (cursor.moveToNext());
        }
        Cursor cursor2 = context.getContentResolver()
                .query(Uri.parse("content://sms/sent"), null, null, null, null);

        if (cursor2.moveToFirst()) {
            do {
                String currentAddress = cursor2.getString(
                        cursor2.getColumnIndexOrThrow("address"));
                if (currentAddress.equals(fromAddress)) {
                    MessageInfo currentMessageSent = new MessageInfo("Me",
                            cursor2.getString(cursor2.getColumnIndexOrThrow("address")),
                            cursor2.getString(cursor2.getColumnIndexOrThrow("body")),
                            cursor2.getString(cursor2.getColumnIndexOrThrow("date")),
                            cursor2.getString(cursor2.getColumnIndexOrThrow("seen")),
                            cursor2.getInt(cursor2.getColumnIndexOrThrow("_id")));
                    System.out.println(cursor2);
                    messages.add(currentMessageSent);
                }

            } while (cursor2.moveToNext());

        }

        messages.sort(new Comparator<MessageInfo>() {

            @Override
            public int compare(MessageInfo o1, MessageInfo o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        return messages;
    }

    public String getLastMessageText(String fromAddress){
        if (context == null)
            throw new NullPointerException();

        Cursor cursor = context.getContentResolver()
                .query(Uri.parse("content://sms"), null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                String currentAddress = cursor.getString(
                        cursor.getColumnIndexOrThrow("address"));

                if (currentAddress.equals(fromAddress)) {
                    String lastMessageText =
                            cursor.getString(cursor.getColumnIndexOrThrow("body"));
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
                            ((EditText)findViewById(R.id.message)).getText().toString());
                    ((EditText)findViewById(R.id.message)).getText().clear();
                    messenger.send();
                } else {
                    requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
                }
            }
        });
    }

}
