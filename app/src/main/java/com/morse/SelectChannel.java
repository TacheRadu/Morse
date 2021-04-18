package com.morse;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.R;
import com.androidsms.SmsChannel;

import java.util.ArrayList;
import java.util.List;

public class SelectChannel extends AppCompatActivity {
    ListView listView;
    private Button button;
    List<Channel> channelList;
    ArrayAdapter<Channel> adapter;
    String[] mTitle = { "Signal", "Twitter", "Reddit"};
    String[] mDescription = {"Channel for Signal", "Channel for Twitter", "Channel for Reddit"};
    int[] images = { R.drawable.signal, R.drawable.twitter, R.drawable.reddit};
    String[] final_title;
    String[] final_description;
    int[] final_images;
    int contor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_channel);
        listView = findViewById(R.id.listView);
        channelList = new ArrayList<>();
        adapter = new ArrayAdapter<Channel>(this,
                android.R.layout.simple_list_item_1, channelList);
        listView.setAdapter(adapter);

        //tried to get the value from the SelectChannel button
        //update : we receive the value from the SelectChannel but can't print the button on our AddChannel page
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //in final_value we store what we receive back from SelectChannel
            int final_value = extras.getInt("Channel");
            System.out.println(final_value);
//            final_title[contor] = mTitle[final_value];
//            final_description[contor] = mDescription[final_value];
//            final_images[contor] = images[final_value];
//            contor++;
//
//            listView = findViewById(R.id.listView);
//            MyAdapter adapter = new MyAdapter(this, final_title, final_description, final_images);
//            listView.setAdapter(adapter);

            Toast.makeText(SelectChannel.this, "You selected " +  mTitle[final_value] , Toast.LENGTH_SHORT).show();

            //The key argument here must match that used in the other activity
        }

        //this button will redirect you to the SelectChannel Page
        button= (Button) findViewById(R.id.addchannelbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectChannel();
            }

        });
    }
    public void openSelectChannel(){
        Intent intent =  new Intent(this, AddChannel.class);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra("Channel", 0);
                //Add this value to your adapter and call notifyDataSetChanged();
                channelList.add(new SmsChannel());
                adapter.notifyDataSetChanged();
            }

        }

    }
}