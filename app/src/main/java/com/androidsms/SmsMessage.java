package com.androidsms;

import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morse.Message;

public class SmsMessage extends AppCompatActivity implements Message {

    private String messageText;

    @Override
    public void send() {

    }
    public void send(String message, String phNumber){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phNumber, null, message, null, null);
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Message failed", Toast.LENGTH_SHORT).show();
        }
    }
}
