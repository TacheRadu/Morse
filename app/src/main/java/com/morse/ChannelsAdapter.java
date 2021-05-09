package com.morse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.R;

import java.util.List;


public class ChannelsAdapter extends ArrayAdapter<String> {
    Context context;
    List<String> rApplicationTitlesList;
    List<String> rApplicationDescriptionsList;
    List<Integer> rApplicationImagesList;

    public ChannelsAdapter(Context c, List<String> title, List<String> description, List<Integer> imgs) {
        super(c, R.layout.content_scrolling, R.id.textView1, title);
        this.context = c;
        this.rApplicationTitlesList = title;
        this.rApplicationDescriptionsList = description;
        this.rApplicationImagesList = imgs;
    }

    public void addAll(List<Channel> channels) {
        for (Channel channel : channels) {
            rApplicationTitlesList.add(channel.getName());
            rApplicationDescriptionsList.add(channel.getDescription());
            rApplicationImagesList.add(channel.getImage());
        }
    }

    public void add(Channel channel) {
        rApplicationTitlesList.add(channel.getName());
        rApplicationDescriptionsList.add(channel.getDescription());
        rApplicationImagesList.add(channel.getImage());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.content_scrolling, parent, false);
        ImageView images = row.findViewById(R.id.image);
        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myDescription = row.findViewById(R.id.textView2);

        // now set our resources on views
        images.setImageResource(rApplicationImagesList.get(position));
        myTitle.setText(rApplicationTitlesList.get(position));
        myDescription.setText(rApplicationDescriptionsList.get(position));
        return row;
    }

    @Override
    public String getItem(int position) {
        return rApplicationTitlesList.get(position);
    }
}

