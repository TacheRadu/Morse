package com.channels.androidsms.activities;

import com.R;
import java.util.List;
import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.Toast;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import com.channels.androidsms.SmsChannel;
import com.channels.androidsms.ContactInfo;
import com.channels.androidsms.ContactsAdapter;
import androidx.appcompat.app.AppCompatActivity;
import static com.channels.androidsms.SmsChannel.PERMISSIONS_REQUEST_READ_SMS;


/**
 * Class that handles user actions regarding the the main SMS Channel activity.
 *
 * @version 0.1.1
 */
public class SmsChannelActivity extends AppCompatActivity {
    private static final int DELAY_MS = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
    };

    ContactsAdapter mDataAdapter = null;
    ListView mListView;
    private SmsChannel mSmsChannel;
    final Handler mHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSmsChannel = new SmsChannel(this);
        setContentView(R.layout.contacts_activity_main);
        mListView = (ListView) findViewById(R.id.lstContacts);
        requestContactPermission();
    }

    private void getContacts() {
        List<ContactInfo> contactInfoList = mSmsChannel.getContacts();
        mDataAdapter = new ContactsAdapter(this, R.layout.activity_contact_info, contactInfoList);
        mListView.setAdapter(mDataAdapter);

        // Write whatever to want to do after delay specified (1 sec)
        mHandler.postDelayed(this::getContacts, DELAY_MS);

        mListView.setOnItemClickListener((parent, view, position, id) -> {
            // This helps to refresh message when you enter the conversation.
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
            if (ActivityCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                Toast.makeText(this, "You have disabled a contacts permission",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
