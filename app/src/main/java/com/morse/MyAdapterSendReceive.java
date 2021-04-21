package com.morse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


class MyAdapterSendReceive extends ArrayAdapter<String> {

    Context context;
    String rTitle[];
    String rDescription[];

    MyAdapterSendReceive(Context c, String title[], String description[]) {
        super(c, R.layout.content_scrolling, R.id.textView1, title);
        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.content_scrolling, parent, false);
        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myDescription = row.findViewById(R.id.textView2);

        // now set our resources on views
        myTitle.setText(rTitle[position]);
        myDescription.setText(rDescription[position]);
        return row;
    }
}