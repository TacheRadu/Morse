package com.twitterchannel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.R;
import com.androidsms.SmsChannel;
import com.morse.Channel;
import com.morse.Contact;
import com.morse.Message;

import java.util.List;

/**
 * @author  Ionuț Roșca
 * @version 0.1.0
 */
public class TwitterChannelActivity extends AppCompatActivity implements Channel {
    private final AppCompatActivity parentActivity;
    private String accountName = "@rreloaded_"; /* Hard-coded for now, just to see it works */

    public TwitterChannelActivity(AppCompatActivity activity){
        this.parentActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_login_activity);

        EditText number = findViewById(R.id.number);
        EditText message = findViewById(R.id.message);
        Button sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    String phoneNumber = number.getText().toString().trim();
                    String smsMessage = message.getText().toString().trim();
                } else {
                    requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
                }
            }
        });
    }

    @Override
    public Intent getIntent(){
        return new Intent(parentActivity, SmsChannel.class);
    }

    @Override
    public void login() {

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

    @Override
    public String toString() {
        return "Twitter: " + this.accountName;
    }
}
