package com.example.untitled;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.example.untitled.model.Message;
import com.example.untitled.model.MessageType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/21/13
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageRowAdapter implements ListAdapter {
        List<Message> items;
        Activity context;
    public MessageRowAdapter(Activity context, List<Message> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Message m = items.get(i);
        View view = context.getLayoutInflater().inflate(R.layout.message_row_layout,null);
        TextView body = (TextView) view.findViewById(R.id.messagebody);
        TextView date = (TextView) view.findViewById(R.id.messageDate);
        ImageView image = (ImageView) view.findViewById(R.id.messageImage);
        switch (m.getType()) {
            case TWEET:
                image.setImageResource(R.drawable.twitter_logo);
                break;
            case FACEBOOK_INBOX:
                image.setImageResource(R.drawable.com_facebook_icon);
                break;
            default:
                break;

        }

        body.setText(m.getBody());
        date.setText(DateFormat.format("MM/dd HH:mm",m.getCreatedDate()));
        return view;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return  1;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }
}