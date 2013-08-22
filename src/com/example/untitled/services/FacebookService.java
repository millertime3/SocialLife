package com.example.untitled.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FacebookService {
    void requestOauthentication(Activity activity, int requestCode, int responseCode, Intent intent);

    void loadMessages();
}
