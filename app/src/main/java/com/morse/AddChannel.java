package com.morse;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.R;
import com.channels.androidsms.SmsChannel;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddChannel extends AppCompatActivity {
    ListView listView;
    App app;
    MyAdapter adapter;
    List<String> mTitle = new ArrayList<>(Arrays.asList("SMS", "Reddit", "Twitter"));
    List<String> mDescription = new ArrayList<>(Arrays.asList("Direct SMS", "Reddit",
            "It's what's happening / Twitter"));
    List<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.sms, R.drawable.reddit,
            R.drawable.twitter));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = new App(this);

        setContentView(R.layout.activity_add_channel);

        listView = findViewById(R.id.listView);
        // now create an adapter class
        disableAlreadyExistent();
        adapter = new MyAdapter(this, mTitle.toArray(new String[mTitle.size()]),
                mDescription.toArray(new String[mDescription.size()]), images.toArray(new Integer[images.size()]));
        listView.setAdapter(adapter);
        // there is my mistake...
        // now again check this..

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //we receive from this page a number so that we will know what to show back on our AddChannel page
                Intent intent = new Intent();
                intent.putExtra(Constants.CHANNEL_TYPE, position);

                setResult(RESULT_OK, intent);
                finish();
                //Toast.makeText(SelectChannel.this, "You selected " +  mTitle[0] , Toast.LENGTH_SHORT).show();

            }
        });
        // so item click is done now check list view
    }

    private void disableAlreadyExistent(){
        List<Channel> channels = app.getChannels();
        for(Channel channel : channels){
            for(int index = 0; index < mTitle.size(); index++){
                if(channel instanceof SmsChannel && mTitle.get(index).equals("SMS")){
                    mTitle.remove(index);
                    mDescription.remove(index);
                    images.remove(index);
                }
            }
        }
    }

}