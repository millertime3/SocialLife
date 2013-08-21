package com.example.untitled;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.untitled.model.Message;
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

import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/18/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageActivity extends Activity {

    private PlusClient plusClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.message_layout);
//        loadMessageContent();
    }

    public void loadMessageContent(){
        String fqlQuery = "SELECT body,created_time FROM message WHERE thread_id in (SELECT thread_id FROM thread WHERE folder_id = 0)";
        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Request request = new Request(session,
                "/fql",
                params,
                HttpMethod.GET,
                new Request.Callback(){
                    public void onCompleted(Response response) {
                        Log.i("s", "Result: " + response.toString());
                        try {
                            String responseString = response.toString().replaceFirst(".*state=\\{","{");//;.replaceFirst("}, error.*","");
                            JSONObject json  = new JSONObject(responseString);
                            Collection<Message> messages = FacebookUtils.convertJsonToMessageCollection(json.getJSONArray("data"));
                            showMessages(messages);

                        } catch (JSONException e) {
                           Log.w("tag","Unable to parse json response from facebook");
                        }
                    }
                });
        Request.executeBatchAsync(request);
    }

    private void showMessages(Collection<Message> messages) {
        TableLayout messageTable = (TableLayout) findViewById(R.id.messageTable);
        for(Message message : messages) {
            messageTable.addView(createMessageRow(message));
        }
    }

    private View createMessageRow(Message message) {
        TableRow messageRow = new TableRow(this);
        TextView createdDate = new TextView(this);
        createdDate.setText(message.getCreatedDate().toString());
        messageRow.addView(createdDate);
        TextView messagebody = new TextView(this);
        messageRow.addView(messagebody);
        messagebody.setText(message.getBody());
        return messageRow;
    }


}
