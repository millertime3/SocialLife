package com.milyfe.untitled;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.milyfe.untitled.model.Message;
import com.milyfe.untitled.model.dao.MessageDao;
import com.google.android.gms.plus.PlusClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/18/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageActivity extends Activity {

    private SimpleCursorAdapter mAdapter;
    private PlusClient plusClient;
    private MessageDao messageDao;
    private ArrayList<View> results = new ArrayList<View>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.message_layout);
        messageDao = new MessageDao(this);
//        showMessages(messageDao.getMessages());
        listView = (ListView) findViewById(R.id.List);
        listView.setAdapter(new MessageRowAdapter(this,new ArrayList<Message>(messageDao.getMessages())));

    }



    private void showMessages(Collection<Message> messages) {
        for(Message message : messages) {
            results.add(createMessageRow(message));
        }
    }

    private Map<String, String> createMessageMap(Message message) {
        Map<String,String> map = new HashMap<String, String>();
        map.put(message.getType().toString(),message.getBody());
        return map;
    }

    private View createMessageRow(Message message) {
//        TextView createdDate = new TextView(this);
//        createdDate.setText(DateFormat.format("MM/dd hh:mm",message.getCreatedDate()));
//        messageRow.addView(createdDate);
        TextView textView = new TextView(this);
        textView.setText(message.getBody());
        return textView;
    }


}
