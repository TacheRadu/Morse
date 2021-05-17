package com.channels.androidsms.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.R;
import com.channels.androidsms.MessageInfo;
import com.channels.androidsms.MyAdapterSendReceive;
import com.channels.androidsms.SmsContact;
import com.channels.androidsms.SmsMessage;

import java.util.ArrayList;
import java.util.List;

public class SmsContactActivity extends AppCompatActivity {

    Button sendButton;
    ListView listView;
    SmsContact smsContact;
    int size;
    private MyAdapterSendReceive adapter;
    List<String> nameList;
    List<String> messageList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_contact);
        sendButton = findViewById(R.id.sendDelayedMessage);

        listView = findViewById(R.id.listView);
        Bundle bundle = getIntent().getExtras();
        smsContact = new SmsContact(getApplicationContext(), bundle.getString("phoneNumber"), bundle.getString("name"));
        refreshMessageList();
        adapter = new MyAdapterSendReceive(SmsContactActivity.this, nameList, messageList);
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
                    SmsMessage messenger = new SmsMessage(this, smsContact.getPhNumber(),
                            ((EditText) findViewById(R.id.message)).getText().toString());
                    ((EditText) findViewById(R.id.message)).getText().clear();
                    messenger.send(); // send
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void refreshMessageList() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SmsContactActivity.this, new String[]{"android.permission.READ_SMS"}, 123);
        else {
            List<MessageInfo> messages = smsContact.getMessages(smsContact.getPhNumber());
            if (nameList != null)
                size = nameList.size();

            nameList = new ArrayList<>();
            messageList = new ArrayList<>();

            for (MessageInfo message : messages) {
                nameList.add(message.getPerson());
                messageList.add(message.getMessageText());
            }

            if (size != nameList.size()) {
                adapter = new MyAdapterSendReceive(SmsContactActivity.this, nameList, messageList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }
    }

}
