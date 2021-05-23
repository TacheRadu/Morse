package com.channels.androidsms;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.morse.Message;

public class SmsMessage implements Message {
    private final Context mContext;
    private final String mPhoneNumber;
    private final String mMessageText;

    public SmsMessage(Context context, String phoneNumber, String messageText) {
        this.mContext = context;
        this.mPhoneNumber = phoneNumber;
        this.mMessageText = messageText;
    }

    /**
     * Sends the smsMessage with which the object has been initialized in the constructor to the
     * phone number used to initialize the object. See `this.phoneNumber` and `this.messageText`
     */
    @Override
    public void send() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mPhoneNumber, null, mMessageText, null,
                    null);
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext,
                    "Message was sent!", Toast.LENGTH_LONG).show());

        } catch (Exception e) {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext,
                    "Failed to send message", Toast.LENGTH_LONG).show());

        }
    }

    /**
     * Sends the smsMessage with delayedMinutes minutes by calling: ``this.send();`` after
     * the delayedMinutes minutes. See ``this.send()``
     *
     * @author Peiu Iulian
     */
    @Override
    public void sendDelayed(long delayedMinutes) {
        long delayedMilliSeconds = delayedMinutes * 60 * 1000;
        new Thread(() ->{
            try {
                Thread.sleep(delayedMilliSeconds);
                send();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //(new Handler(Looper.getMainLooper())).postDelayed(this::send, delayedMilliSeconds);
    }

    /**
     * Delete a message from content://sms/{id}, where id is set from the 2 param constructor
     *
     * @author Peiu Iulian
     */
    @Override
    public Boolean delete(long id) {
        try {
            mContext.getContentResolver().delete(
                    Uri.parse("content://sms/" + id), null, null);
        } catch (Exception exp) {
            return false;
        }

        return true;
    }
}
