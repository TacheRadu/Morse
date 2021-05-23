package com.channels.androidsms;

import com.R;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;


/**
 * Class that handles populating the contacts list.
 *
 * @version 0.1.1
 */
public class ContactsAdapter extends ArrayAdapter<ContactInfo> {
    private final List<ContactInfo> mContactInfoList;
    private final Context mContext;

    public ContactsAdapter(@NonNull Context context, int resource,
                           @NonNull List<ContactInfo> objects) {
        super(context, resource, objects);
        this.mContactInfoList = objects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.activity_contact_info, null);

            holder = new ViewHolder();
            holder.displayName = convertView.findViewById(R.id.displayName);
            holder.lastMessage = convertView.findViewById(R.id.lastMessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactInfo contactInfo = mContactInfoList.get(position);
        holder.displayName.setText(contactInfo.getDisplayName());
        holder.lastMessage.setText(contactInfo.getLastMessage());

        return convertView;
    }

    private static class ViewHolder {
        TextView displayName;
        TextView lastMessage;
    }
}