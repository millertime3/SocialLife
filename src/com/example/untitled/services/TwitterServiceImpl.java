package com.example.untitled.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import com.example.untitled.R;
import com.example.untitled.model.Message;
import com.example.untitled.model.dao.MessageDao;
import com.example.untitled.utils.TwitterUtils;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TwitterServiceImpl implements TwitterService{
    private static String PREFERENCE_NAME = "twitter_oauth";
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    private static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    private static final String URL_TWITTER_AUTH = "auth_url";
    private static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    private static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    private RequestToken requestToken;
    private SharedPreferences sharedPreferences;
    private Context context;
    private MessageDao messageDao;

    public TwitterServiceImpl(Context context) {
        super();
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("MyPref", 0);
        this.messageDao = new MessageDao(context);
    }

    @Override
    public void requestOauthentication() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                // Check if already logged in
                if (!isLoggedin()) {
                    try {
                        Twitter twitter = buildTwitter();
                        requestToken = twitter
                                .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                                .parse(requestToken.getAuthenticationURL()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        th.start();
    }

    @Override
    public boolean isLoggedin() {
        return sharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    @Override
    public void processOauthCallback(final Uri uri) {
        Thread th = new Thread(new Runnable() {
            public void run() {
                //Uri uri = getIntent().getData();
                if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                    // oAuth verifier
                    String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
                    try {
                        Twitter twitter = buildTwitter();
                        // Get the access token
                        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                        storeOauthToken(accessToken);
                        Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
                    } catch (Exception e) {
                        // Check log for login errors
                        Log.e("Twitter Login Error", "> " + e.getMessage());
                    }
                }
            }
        });
        th.start();
    }

    @Override
    public void loadMessages() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                Looper.prepare();;
                Twitter twitter = buildTwitter();
                try {
                    StringBuilder sb = new StringBuilder();
                    ResponseList<Status> responseList = twitter.getHomeTimeline(new Paging(1,10));
                    Iterator<Status> iterator = responseList.iterator();
                    Collection<Message> messages = new LinkedList<Message>();
                    while(iterator.hasNext()) {
                       Status status = iterator.next();
                       messages.add(TwitterUtils.getMessageFromStatus(status));
                    }
                    messageDao.open();
                    for (Message message : messages) {
                        messageDao.insert(message);
                    }
                    messageDao.close();
                } catch (TwitterException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                Looper.loop();
            }});
        th.start();

    }

    private void storeOauthToken(AccessToken accessToken) {
        // Shared Preferences
        SharedPreferences.Editor e = sharedPreferences.edit();

        // After getting access token, access token secret
        // store them in application preferences
        e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
        e.putString(PREF_KEY_OAUTH_SECRET,
                accessToken.getTokenSecret());
        // Store login status - true
        e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
        e.commit(); // save changes
    }

    private Twitter buildTwitter() {
        String token = sharedPreferences.getString(PREF_KEY_OAUTH_TOKEN,null);
        String secret = sharedPreferences.getString(PREF_KEY_OAUTH_SECRET,null);
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(context.getString(R.string.twitter_consumer_key));
        builder.setOAuthConsumerSecret(context.getString(R.string.twitter_consumer_secret));
        builder.setOAuthAccessToken(token);
        builder.setOAuthAccessTokenSecret(secret);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        return twitter;
    }
}
