package com.angelectro.shaker;

import android.app.Application;
import android.graphics.Shader;

import com.angelectro.shakerdetection.Shaker;

import java.util.HashMap;
import java.util.Map;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Map<String,String> settings= new HashMap<>();
        settings.put(Shaker.SLACK_CLIENT_ID,getString(R.string.slack_client_id));
        settings.put(Shaker.SLACK_CLIENT_SECRET,getString(R.string.slack_client_secret));
        settings.put(Shaker.SLACK_REDIRECT_URI,getString(R.string.slack_redirect_uri));
        Shaker.initialize(this,settings);
    }
}
