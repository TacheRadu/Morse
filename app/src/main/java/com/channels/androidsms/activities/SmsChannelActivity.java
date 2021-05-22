package com.channels.androidsms.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.R;
import com.channels.androidsms.ContactInfo;
import com.channels.androidsms.ContactsAdapter;
import com.channels.androidsms.SmsChannel;

import java.util.List;
import android.os.Handler;

import static com.channels.androidsms.SmsChannel.PERMISSIONS_REQUEST_READ_SMS;

public class SmsChannelActivity extends AppCompatActivity {
    private final String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS};
    ContactsAdapter dataAdapter = null;
    ListView listView;
    private SmsChannel smsChannel;
    final Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smsChannel = new SmsChannel(this);
        setContentView(R.layout.contacts_activity_main);
        listView = (ListView) findViewById(R.id.lstContacts);
        requestContactPermission();
    }

    private void getContacts() {
        List<ContactInfo> contactInfoList = smsChannel.getContacts();
        dataAdapter = new ContactsAdapter(this, R.layout.contact_info, contactInfoList);
        listView.setAdapter(dataAdapter);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                getContacts();
            }
        }, 500);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            //This helps to refresh message when you enter the conversation.
            finish();
            startActivity(getIntent());

            Intent intent = new Intent(this, SmsContactActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", contactInfoList.get(position).getPhoneNumber());
            bundle.putString("name", contactInfoList.get(position).getDisplayName());
            bundle.putString("lastMessage", contactInfoList.get(position).getLastMessage());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    public void requestContactPermission() {
        if (!hasPermissions()) {
            requestPermissions(PERMISSIONS, 1);
        } else {
            getContacts();
        }
    }

    private boolean hasPermissions() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
