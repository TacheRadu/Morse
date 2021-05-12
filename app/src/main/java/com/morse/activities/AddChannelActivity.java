package com.morse.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.channels.androidsms.SmsChannel;
import com.channels.twitter.TwitterChannelLoginActivity;
import com.morse.App;
import com.morse.Channel;
import com.morse.ChannelsAdapter;
import com.morse.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddChannelActivity extends AppCompatActivity {
    final List<String> mTitle = new ArrayList<>(Arrays.asList("SMS", "Reddit", "Twitter"));
    final List<String> mDescription = new ArrayList<>(Arrays.asList("Direct SMS", "Reddit",
            "It's what's happening / Twitter"));
    final List<Integer> images = new ArrayList<>(Arrays.asList(R.mipmap.sms, R.mipmap.reddit,
            R.mipmap.twitter));
    ListView listView;
    App app;
    ChannelsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = new App(this);

        setContentView(R.layout.activity_add_channel);

        listView = findViewById(R.id.listView);
        // now create an adapter class
        disableAlreadyExistent();
        adapter = new ChannelsAdapter(this, mTitle, mDescription, images);
        listView.setAdapter(adapter);
        // there is my mistake...
        // now again check this..

        // now set item click on list view
        listView.setOnItemClickListener((parent, view, position, id) -> {
            //we receive from this page a number so that we will know what to show back on our AddChannel page
            Intent intent = new Intent();
            switch (adapter.getItem(position)) {
                case "SMS":
                    intent.putExtra(Constants.CHANNEL_TYPE, Constants.CHANNEL_ANDROID_SMS);
                    break;
                case "Reddit":
                    intent.putExtra(Constants.CHANNEL_TYPE, Constants.CHANNEL_REDDIT);
                    break;
                case "Twitter":
                    intent.putExtra(Constants.CHANNEL_TYPE, Constants.CHANNEL_TWITTER);
                    break;
            }

            setResult(RESULT_OK, intent);
            finish();
            //Toast.makeText(SelectChannel.this, "You selected " +  mTitle[0] , Toast.LENGTH_SHORT).show();

        });
        // so item click is done now check list view
    }

    private void disableAlreadyExistent() {
        List<Channel> channels = app.getChannels();
        for (Channel channel : channels) {
            for (int index = 0; index < mTitle.size(); index++) {
                if (channel instanceof SmsChannel && mTitle.get(index).equals("SMS")) {
                    mTitle.remove(index);
                    mDescription.remove(index);
                    images.remove(index);
                } else if (channel instanceof TwitterChannelLoginActivity && mTitle.get(index).equals("Twitter")) {
                    mTitle.remove(index);
                    mDescription.remove(index);
                    images.remove(index);
                }
            }
        }
    }

}