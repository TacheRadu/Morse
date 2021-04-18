package com.morse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddChannel extends AppCompatActivity {
    ListView listView;
    String mTitle[] = { "Signal", "Twitter", "Reddit"};
    String mDescription[] = {"Channel for Signal", "Channel for Twitter", "Channel for Reddit"};
    int images[] = { R.drawable.signal, R.drawable.twitter, R.drawable.reddit};
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