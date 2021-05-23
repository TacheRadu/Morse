package com.morse.activities;

import com.R;
import com.morse.App;
import java.util.List;
import com.morse.Channel;
import android.os.Bundle;
import com.morse.Constants;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import com.morse.ChannelsAdapter;
import com.channels.reddit.RedditChannel;
import com.channels.androidsms.SmsChannel;
import com.channels.twitter.TwitterChannel;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Class responsible for correctly selecting a channel chosen by the user and creating a new
 * activity for the channel selected.
 *
 * @version 0.1.1
 */
public class SelectChannelActivity extends AppCompatActivity {
    ListView mListView;
    List<Channel> mChannelList;
    ChannelsAdapter mAdapter;

    private void createAdapter() {
        List<String> titles = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        List<Integer> images = new ArrayList<>();
        for (Channel channel : mChannelList) {
            titles.add(channel.getName());
            descriptions.add(channel.getDescription());
            images.add(channel.getImage());
        }

        mAdapter = new ChannelsAdapter(SelectChannelActivity.this, titles, descriptions, images);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_channel);
        mListView = findViewById(R.id.listView);
        mChannelList = new ArrayList<>();
        mChannelList.addAll(App.getChannels());

        createAdapter();
        mAdapter.notifyDataSetChanged();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(
                (parent, view, position, id) -> startActivity(mChannelList.get(position).getIntent())
        );

        Button button = findViewById(R.id.addchannelbtn);
        button.setOnClickListener(v -> openAddChannel());
    }

    public void openAddChannel() {
        Intent intent = new Intent(this, AddChannelActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                int code = data.getIntExtra(Constants.CHANNEL_TYPE, -1);
                if (code == Constants.CHANNEL_ANDROID_SMS) {
                    mChannelList.add(new SmsChannel(SelectChannelActivity.this));
                    mAdapter.add(mChannelList.get(mChannelList.size() - 1));
                    mAdapter.notifyDataSetChanged();
                    App.insertIntoChannels("sms");
                } else if (code == Constants.CHANNEL_REDDIT) {
                    mChannelList.add(new RedditChannel(SelectChannelActivity.this));
                    mAdapter.add(mChannelList.get(mChannelList.size() - 1));
                    mAdapter.notifyDataSetChanged();
                    App.insertIntoChannels("reddit");
                } else if (code == Constants.CHANNEL_TWITTER) {
                    mChannelList.add(new TwitterChannel(SelectChannelActivity.this));
                    mAdapter.add(mChannelList.get(mChannelList.size() - 1));
                    mAdapter.notifyDataSetChanged();
                    App.insertIntoChannels("twitter");
                }
            }
        }
    }
}