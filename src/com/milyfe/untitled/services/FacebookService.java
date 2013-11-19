package com.milyfe.untitled.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/20/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FacebookService extends MessageService{
    void requestOauthentication(Activity activity, int requestCode, int responseCode, Intent intent);

    void loadMessages(Context context);

    void onSaveInstanceState(Bundle outState);
}
