package com.channels.androidsms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.R;

import java.util.List;


class MyAdapterSendReceive extends ArrayAdapter<String> {

    Context context;
    List<String> rTitle;
    List<String> rDescription;

    MyAdapterSendReceive(Context c, List<String> title, List<String> description) {
        super(c, R.layout.content_scrolling_send_receive_message, R.id.textView1, title);
        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.content_scrolling_send_receive_message, parent, false);
        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myDescription = row.findViewById(R.id.textView2);

        // now set our resources on views
        myTitle.setText(rTitle.get(position));
        myDescription.setText(rDescription.get(position));
        return row;
    }
}