package com.channels.androidsms.activities;

import com.R;
import java.util.List;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.widget.Switch;
import android.widget.Toast;

import com.channels.androidsms.SmsContact;
import com.channels.androidsms.SmsMessage;
import androidx.core.content.ContextCompat;
import com.channels.androidsms.MessageInfo;
import com.channels.androidsms.MessagesAdapter;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Class responsible with handling (send, display) the message exchange between two users.
 *
 * @version 0.1.1
 */
public class SmsContactActivity extends AppCompatActivity {
    int mSize;
    Button mSendButton;
    ListView mListView;
    SmsContact mSmsContact;
    SwitchCompat mSwitchCompat;
    EditText mDelay;
    List<String> mNameList;
    List<String> mMessageList;
    private MessagesAdapter mAdapter;
    private static final int DELAY_MS = 1000;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_contact);

        mSendButton = findViewById(R.id.sendMessage);
        mListView = findViewById(R.id.listView);
        mSwitchCompat = findViewById(R.id.switchCompat);
        mDelay = findViewById(R.id.delay);
        mDelay.setVisibility(View.INVISIBLE);

        Bundle bundle = getIntent().getExtras();
        mSmsContact = new SmsContact(getApplicationContext(), bundle.getString("phoneNumber"),
                bundle.getString("name"));
        refreshMessageList();

        mAdapter = new MessagesAdapter(SmsContactActivity.this, mNameList, mMessageList);
        mListView.setAdapter(mAdapter);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshMessageList();
                handler.postDelayed(this, 1000);
            }
        }, DELAY_MS);

        mSendButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    SmsMessage messenger = new SmsMessage(this, mSmsContact.getPhoneNumber(),
                            ((EditText) findViewById(R.id.message)).getText().toString());
                    ((EditText) findViewById(R.id.message)).getText().clear();
                    if(!mSwitchCompat.isChecked())
                        messenger.send();
                    else{
                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(this.getApplicationContext(),
                                "Message will be sent", Toast.LENGTH_SHORT).show());
                        messenger.sendDelayed(Integer.parseInt(mDelay.getText().toString()));
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
            }
        });

        mSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                mSendButton.setText(getString(R.string.delayed));
                mDelay.setVisibility(View.VISIBLE);
            }
            else{
                mSendButton.setText(getString(R.string.send_message));
                mDelay.setVisibility(View.INVISIBLE);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void refreshMessageList() {
        if (ContextCompat.checkSelfPermission(getBaseContext(),
                "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SmsContactActivity.this,
                    new String[]{"android.permission.READ_SMS"}, 123);
        } else {
            List<MessageInfo> messages = mSmsContact.getMessages(mSmsContact.getPhoneNumber());
            if (mNameList != null)
                mSize = mNameList.size();

            mNameList = new ArrayList<>();
            mMessageList = new ArrayList<>();

            for (MessageInfo message : messages) {
                mNameList.add(message.getPerson());
                mMessageList.add(message.getMessageText());
            }

            if (mSize != mNameList.size()) {
                mAdapter = new MessagesAdapter(SmsContactActivity.this, mNameList, mMessageList);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
