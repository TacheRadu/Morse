package com.androidsms;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to send SMS message.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @author Peiu Iulian
     * Sends the smsMessage with delayedMinutes minutes by calling: ``this.send();`` after
     * the delayedMinutes minutes. See ``this.send()``
     */
    @Override
    public void sendDelayed(long delayedMinutes) {
        long delayedMilliSeconds = delayedMinutes * 60 * 1000;
        (new Handler()).postDelayed(() -> {
            send();
        }, delayedMilliSeconds);
    }

    /**
     * @author Peiu Iulian
     * Delete a message from content://sms/{id}, where id is set from the 2 param constructor
     */
    @Override
    public Boolean delete(int id){

        try{
            context.getContentResolver().delete(
                    Uri.parse("content://sms/" + id), null, null);
        } catch(Exception exp){
            return false;
        }
        return true;
    }

}
