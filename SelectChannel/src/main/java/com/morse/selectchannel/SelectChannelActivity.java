package com.morse.selectchannel;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectChannelActivity extends AppCompatActivity {
    ListView listView;
    String mTitle[] = {"Messenger", "Signal", "Twitter", "Instagram"};
    String mDescription[] = {"Channel for Messenger", "Channel for Signal", "Channel for Twitter", "Channel for Instagram"};
    int images[] = {R.drawable.facebook, R.drawable.signal, R.drawable.twitter, R.drawable.instagram};
    // so our images and other things are set in array

    // now paste some images in drawable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_channel);

        listView = findViewById(R.id.listView);
        // now create an adapter class

        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription, images);
        listView.setAdapter(adapter);
        // there is my mistake...
        // now again check this..

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(SelectChannelActivity.this, "You selected a channel. " + mTitle[0], Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    Toast.makeText(SelectChannelActivity.this, "You selected a channel. " + mTitle[1], Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    Toast.makeText(SelectChannelActivity.this, "You selected a channel. " + mTitle[2], Toast.LENGTH_SHORT).show();
                }
                if (position == 3) {
                    Toast.makeText(SelectChannelActivity.this, "You selected a channel. " + mTitle[3], Toast.LENGTH_SHORT).show();
                }
            }
        });
        // so item click is done now check list view
    }


}