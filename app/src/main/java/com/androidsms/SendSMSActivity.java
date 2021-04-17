package com.androidsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.morse.R;

/**
 * @author  Ionuț Roșca
 * @version 0.1.0
 */
public class SendSMSActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        EditText number = findViewById(R.id.number);
        EditText message = findViewById(R.id.message);
        Button sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    String phoneNumber = number.getText().toString().trim();
                    String smsMessage = message.getText().toString().trim();
                    SmsMessage messenger = new SmsMessage(this, phoneNumber, smsMessage);

                    messenger.send();
                } else {
                    requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
                }
            }
        });
    }
}
