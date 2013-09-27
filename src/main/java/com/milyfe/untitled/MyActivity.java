package com.milyfe.untitled;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.milyfe.untitled.model.Message;
import com.milyfe.untitled.services.FacebookService;
import com.milyfe.untitled.services.FacebookServiceImpl;
import com.milyfe.untitled.services.TwitterService;
import com.milyfe.untitled.services.TwitterServiceImpl;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import twitter4j.*;
import twitter4j.auth.RequestToken;

import java.util.Collection;
import java.util.LinkedList;

public class MyActivity extends Activity implements ConnectionCallbacks,OnConnectionFailedListener,View.OnClickListener {
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    private LocationClient mLocationClient;
    private LoginButton loginButton;
    private GraphUser user;
    public static Collection<Message> plusMessages = new LinkedList<Message>();

    /**
     * Called when the activity is first created.
     */

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    private UiLifecycleHelper uiHelper;
    private TwitterService twitterService;
    private FacebookService facebookService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterService = new TwitterServiceImpl(getApplicationContext());
        facebookService = new FacebookServiceImpl(getApplicationContext());
        setContentView(R.layout.main);
        mPlusClient = new PlusClient.Builder(this, this, this)
                .setVisibleActivities("http://schemas.google.com/AddActivity",
                        "http://schemas.google.com/BuyActivity",
                        "https://www.googleapis.com/auth/plus.moments.read",
                        "https://www.googleapis.com/auth/plus.moments.write")
                .build();
        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        mLocationClient = new LocationClient(this, this, this);

        //Oauth callback
        twitterService.processOauthCallback(getIntent().getData());

    }

    private void loadMPlusMessages() {
        mPlusClient.loadMoments(new PlusClient.OnMomentsLoadedListener() {
            @Override
            public void onMomentsLoaded(ConnectionResult connectionResult, MomentBuffer moments, String s, String s2) {
                int count = moments.getCount();
            }
        });
    }

    public void loadTwitterMessages(View view) {
        twitterService.loadMessages();
        facebookService.loadMessages();
    }

    public void showTwitterMessage(View view) {
        showMessagePage(view);
    }

    public void authenticateTwitter(View view) {
        twitterService.requestOauthentication();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (this.getIntent()!=null && this.getIntent().getData()!=null){
//            Uri uri = this.getIntent().getData();
//            if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
//                String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
//                try {
//                    // this will populate token and token_secret in consumer
//                    provider.retrieveAccessToken(consumer, verifier);
//
//                    // Get Access Token and persist it
//                    AccessToken a = new AccessToken(consumer.getToken(), consumer.getTokenSecret());
//                    storeAccessToken(a);
//
//                    // initialize Twitter4J
//                    twitter = new TwitterFactory().getInstance();
//                    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
//                    twitter.setOAuthAccessToken(a);
//                    ((TwitterApplication)getApplication()).setTwitter(twitter);
//                    //Log.e("Login", "Twitter Initialised");
//
//                    startFirstActivity();
//
//                } catch (Exception e) {
//                    //Log.e(APP, e.getMessage());
//                    e.printStackTrace();
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }



    public void showMessagePage(View view) {
        startActivity(new Intent(getApplicationContext(), MessageActivity.class));
    }

    public void showMessage(View view) {
       EditText editText = (EditText) findViewById(R.id.editText);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(editText.getText()+getHangoutParticipants());
        dialog.show();
    }

    public void showinfo(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        if(mPlusClient.isConnected()) {
            dialog.setMessage(getHangoutParticipants());
        } else {
           dialog.setMessage("Please Sign in");
        }
        loadMPlusMessages();
        //dialog.show();
//        Plus.Activities.List listActivities = plus.activities().list("me", "public");
//        listActivities.setMaxResults(5L);
//        // Pro tip: Use partial responses to improve response time considerably
//        listActivities.setFields("nextPageToken,items(id,url,object/content)");
//        ActivityFeed feed = listActivities.execute();
    }

    public void onClick(View view){

        if(view.getId() == R.id.sign_in_button && mPlusClient.isConnected()) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage("Already signed in");
            dialog.show();
        }
        if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
            if (mConnectionResult == null) {
                mConnectionProgressDialog.show();
            } else {
                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
    }

    private String getHangoutParticipants() {
        Person currentPerson = mPlusClient.getCurrentPerson();
        StringBuilder sb = new StringBuilder();
        sb.append("The crazy information people can get when you accept permissions:").append("\n");
        sb.append("Name:" + currentPerson.getDisplayName()).append("\n");
        sb.append("Birthday:" + currentPerson.getBirthday()).append("\n");
        Location mCurrentLocation = mLocationClient.getLastLocation();
        sb.append("Current location:").append("\n").append("Lat:").append(mCurrentLocation.getLatitude()).append("\n").append("Log:").append(mCurrentLocation.getLongitude());
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        // We've resolved any connection errors.
        mConnectionProgressDialog.dismiss();
    }

    @Override
    public void onDisconnected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mConnectionProgressDialog.isShowing()) {
            // The user clicked the sign-in button already. Start to resolve
            // connection errors. Wait until onConnected() to dismiss the
            // connection dialog.
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }

        // Save the intent so that we can start an activity when the user clicks
        // the sign-in button.
        mConnectionResult = result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlusClient.connect();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
        mLocationClient.disconnect();
    }
}
