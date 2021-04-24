package com.androidsms;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.R;
import com.morse.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SmsContact extends AppCompatActivity implements Contact {

    private Context context;

    private String phNumber;

    private String name;

    private MyAdapterSendReceive adapter;

    ListView listView;
    List<String> nameList;
    List<String> messageList;

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

    @Override
    public void refreshMessageList() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SmsContact.this, new String[]{"android.permission.READ_SMS"}, 123);
        else {
            List<MessageInfo> messages = getMessages(phNumber);
            nameList = new ArrayList<>();
            messageList = new ArrayList<>();

            for (MessageInfo message : messages) {
                if(message.getPerson()==null)
                    nameList.add("message.getPerson()");
                else
                    nameList.add(message.getPerson());
                messageList.add(message.getMessageText());
            }
        }
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
                            cursor.getString(cursor.getColumnIndexOrThrow("address")),
                            cursor.getString(cursor.getColumnIndexOrThrow("body")),
                            cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("seen")));
                    System.out.println(cursor.getString(cursor.getColumnIndexOrThrow("person")));
                    messages.add(currentMessage);
                }

            } while (cursor.moveToNext());
        }

        return messages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_contact);


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
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 10000);
            }
        }, 1000);
    }

}
