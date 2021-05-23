package com.morse.activities;


import com.R;
import com.morse.App;
import java.util.List;
import java.util.Arrays;
import com.morse.Channel;
import android.os.Bundle;
import com.morse.Constants;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.ListView;
import com.morse.ChannelsAdapter;
import com.channels.reddit.RedditChannel;
import com.channels.androidsms.SmsChannel;
import com.channels.twitter.TwitterChannel;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Class that handles the process of adding a new channel.
 *
 * @version 0.1.1
 */
public class AddChannelActivity extends AppCompatActivity {
    final List<String> mTitle = new ArrayList<>(Arrays.asList("SMS", "Reddit", "Twitter"));
    final List<String> mDescription = new ArrayList<>(Arrays.asList("Direct SMS", "Reddit",
            "It's what's happening / Twitter"));
    final List<Integer> mImages = new ArrayList<>(Arrays.asList(R.mipmap.sms, R.mipmap.reddit,
            R.mipmap.twitter));
    ListView mListview;
    ChannelsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_channel);
        mListview = findViewById(R.id.listView);

        // now create an adapter class
        disableAlreadyExistent();
        mAdapter = new ChannelsAdapter(this, mTitle, mDescription, mImages);
        mListview.setAdapter(mAdapter);

        // now set item click on list view
        mListview.setOnItemClickListener((parent, view, position, id) -> {
            // we receive from this page a number so that we will know what to show back on our
            // AddChannel page
            Intent intent = new Intent();
            switch (mAdapter.getItem(position)) {
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

        });
    }

    private void disableAlreadyExistent() {
        List<Channel> channels = App.getChannels();
        for (Channel channel : channels) {
            for (int index = 0; index < mTitle.size(); index++) {
                if (channel instanceof SmsChannel && mTitle.get(index).equals("SMS")) {
                    mTitle.remove(index);
                    mDescription.remove(index);
                    mImages.remove(index);
                } else if (channel instanceof RedditChannel && mTitle.get(index).equals("Reddit")) {
                    mTitle.remove(index);
                    mDescription.remove(index);
                    mImages.remove(index);
                } else if (channel instanceof TwitterChannel && mTitle.get(index).equals("Twitter")) {
                    mTitle.remove(index);
                    mDescription.remove(index);
                    mImages.remove(index);
                }
            }
        }
    }

}