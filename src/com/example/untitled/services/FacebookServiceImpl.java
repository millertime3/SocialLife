package com.example.untitled.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.example.untitled.R;
import com.example.untitled.model.Message;
import com.example.untitled.model.dao.MessageDao;
import com.example.untitled.utils.FacebookUtils;
import com.facebook.*;
import com.facebook.model.GraphUser;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookServiceImpl implements FacebookService {


    private Context context;
    private MessageDao messageDao;

    public FacebookServiceImpl(Context context) {
        this.context = context;
        this.messageDao = new MessageDao(context);
    }

    @Override
    public void requestOauthentication(Activity activity,int requestCode, int responseCode, Intent intent) {
        //Facebook
        Session.getActiveSession().onActivityResult(activity, requestCode, responseCode, intent);
        // start Facebook Login
        Session.openActiveSession(activity, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
//                            context.user = user;
//                            if (user != null) {
//                                TextView welcome = (TextView) findViewById(R.id.welcome);
//                                welcome.setText("Hello " + user.getName() + "!");
//                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void loadMessages(){
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
                            messageDao.open();
                            for (Message message : messages) {
                                messageDao.insert(message);
                            }
                            messageDao.close();
                        } catch (JSONException e) {
                            Log.w("tag","Unable to parse json response from facebook");
                        }
                    }
                });
        Request.executeBatchAsync(request);
    }


}
