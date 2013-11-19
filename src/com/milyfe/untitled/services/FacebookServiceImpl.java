package com.milyfe.untitled.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import com.activeandroid.ActiveAndroid;
import com.facebook.*;
import com.milyfe.untitled.R;
import com.milyfe.untitled.model.Message;
import com.milyfe.untitled.utils.FacebookUtils;
import com.facebook.model.GraphUser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookServiceImpl implements FacebookService {

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private Activity context;

    private boolean isResumed = false;

    public FacebookServiceImpl(Activity context,Bundle savedInstanceState) {
        this.context = context;

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(context, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(context);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(context).setCallback(statusCallback));
            }
        }
        Session.getActiveSession().addCallback(statusCallback);
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
//                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//                        // callback after Graph API response with user object
//                        @Override
//                        public void onCompleted(GraphUser user, Response response) {
//                            context.user = user;
//                            if (user != null) {
//                                TextView welcome = (TextView) findViewById(R.id.welcome);
//                                welcome.setText("Hello " + user.getName() + "!");
//                            }
//                        }
//                    });
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(context).setCallback(statusCallback));
        } else {
            Session.openActiveSession(context, true, statusCallback);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    @Override
    public void loadMessages(final Context context){

                Session session = Session.getActiveSession();
            String fqlQuery = "SELECT message,created_time,actor_id FROM stream where source_id = me() LIMIT 1000";
            Bundle params = new Bundle();
            params.putString("q", fqlQuery);
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
                            ActiveAndroid.initialize(context);
                            for (Message message : messages) {
                                message.save();
                            }
                        } catch (JSONException e) {
                            Log.w("tag","Unable to parse json response from facebook",e);
                        }
                    }
                });
        Request.executeBatchAsync(request);
    }


    @Override
    public List<Message> getMessagesByDays(int days) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasAuthentication() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (isResumed) {
//            FragmentManager manager = getSupportFragmentManager();
//            int backStackSize = manager.getBackStackEntryCount();
//            for (int i = 0; i < backStackSize; i++) {
//                manager.popBackStack();
//            }
//            // check for the OPENED state instead of session.isOpened() since for the
//            // OPENED_TOKEN_UPDATED state, the selection fragment should already be showing.
//            if (state.equals(SessionState.OPENED)) {
//                showFragment(SELECTION, false);
//            } else if (state.isClosed()) {
//                showFragment(SPLASH, false);
//            }
        }
    }
}
