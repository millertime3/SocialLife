package com.example.untitled.utils;

import android.util.Log;
import com.example.untitled.model.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/18/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookUtils {
    public static Collection<Message> convertJsonToMessageCollection(JSONArray dataArray) throws JSONException {
        Collection<Message> messages = new LinkedList<Message>();
        if(dataArray != null) {
            for(int i=0;i<dataArray.length();i++) {
                Message message = new Message();
                JSONObject o = dataArray.getJSONObject(i);
                Date createdDate = new Date(Long.parseLong(o.get("created_time").toString())*1000);
                message.setCreatedDate(createdDate);
                message.setBody(o.get("body").toString());
                messages.add(message);
            }
        } else {
            Log.w("tag","Dataarray cannot be null");
        }
        return messages;
    }
}
