package com.example.untitled;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.untitled.model.Message;
import com.example.untitled.model.MessageType;
import com.example.untitled.model.dao.MessageDao;
import com.example.untitled.services.MySQLiteHelper;
import com.example.untitled.utils.FacebookUtils;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Util;
import com.google.android.gms.plus.PlusClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

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
