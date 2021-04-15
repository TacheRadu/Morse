package com.androidsms;

import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morse.Contact;
import com.morse.Message;

public class SmsContact extends AppCompatActivity implements Contact {
    @Override
    public void getMessages(int messageNumber) {

    }

    private String phNumber;

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phNumber, null, message.toString(), null, null);
            //in the class Message, toString will need to be overridden to return a string of the phone number
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Message failed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void refreshMessageList() {

    }
}
