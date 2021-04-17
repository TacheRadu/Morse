package com.androidsms;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morse.Message;

public class SmsMessage extends AppCompatActivity implements Message {
    private final Context context;
    private final String phoneNumber;
    private final String messageText;

    public SmsMessage(Context context, String phoneNumber, String messageText) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.messageText = messageText;
    }

    /**
     * Sends the smsMessage with which the object has been initialized in the constructor to the
     * phone number used to initialize the object. See `this.phoneNumber` and `this.messageText`
     */
    @Override
    public void send() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, messageText, null, null);
            Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Unable to send SMS message.", Toast.LENGTH_SHORT).show();
        }
    }
}
