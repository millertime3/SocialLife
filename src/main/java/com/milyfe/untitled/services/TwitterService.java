package com.milyfe.untitled.services;

import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TwitterService extends MessageService{
    void processOauthCallback(Uri uri);

    boolean isLoggedin();

    void requestOauthentication();

    void loadMessages();
}
