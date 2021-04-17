package com.androidsms;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.morse.Contact;
import com.morse.Message;
import com.R;
import java.util.ArrayList;
import java.util.List;

public class SmsContact implements Contact {


    @Override
    public void getMessages(int messageNumber) {

    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void refreshMessageList() {

    }
}
