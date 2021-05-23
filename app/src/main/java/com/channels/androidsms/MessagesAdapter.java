package com.channels.androidsms;

import com.R;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;


/**
 * Class that handles populating the messages for contacts in the activity.
 *
 * @version 0.1.1
 */
public class MessagesAdapter extends ArrayAdapter<String> {
    final Context mContext;
    final List<String> mTitle;
    final List<String> mDescription;

    public MessagesAdapter(Context c, List<String> title, List<String> description) {
        super(c, R.layout.content_scrolling_send_receive_message, R.id.textView1, title);
        this.mContext = c;
        this.mTitle = title;
        this.mDescription = description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)
                mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.content_scrolling_send_receive_message, parent,
                false);
        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myDescription = row.findViewById(R.id.textView2);

        // now set our resources on views
        myTitle.setText(mTitle.get(position));
        myDescription.setText(mDescription.get(position));
        return row;
    }
}