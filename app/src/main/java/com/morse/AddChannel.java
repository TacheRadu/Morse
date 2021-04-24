package com.morse;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.R;
import androidx.appcompat.app.AppCompatActivity;

public class AddChannel extends AppCompatActivity {
    ListView listView;
    String mTitle[] = {"Signal", "Twitter", "Reddit"};
    String mDescription[] = {"Channel for Signal", "Channel for Twitter", "Channel for Reddit"};
    int images[] = {R.drawable.sms, R.drawable.twitter, R.drawable.reddit};
    // so our images and other things are set in array

    // now paste some images in drawable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);

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
                //we receive from this page a number so that we will know what to show back on our AddChannel page
                Intent intent = new Intent();
                intent.putExtra("Channel", position);

                setResult(RESULT_OK, intent);
                finish();
                //Toast.makeText(SelectChannel.this, "You selected " +  mTitle[0] , Toast.LENGTH_SHORT).show();

            }
        });
        // so item click is done now check list view
    }


}