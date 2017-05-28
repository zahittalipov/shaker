package com.angelectro.shaker;

import android.app.Application;

import com.angelectro.shakerdetection.Shaker;

import java.util.HashMap;
import java.util.Map;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Map<String, String> settings = new HashMap<>();
        settings.put(Shaker.SLACK_CLIENT_ID, getString(R.string.slack_client_id));
        settings.put(Shaker.SLACK_CLIENT_SECRET, getString(R.string.slack_client_secret));
        settings.put(Shaker.SLACK_REDIRECT_URI, getString(R.string.slack_redirect_uri));
        settings.put(Shaker.JIRA_BASE_URL, getString(R.string.jira_home));
        settings.put(Shaker.JIRA_CONSUMER_KEY, getString(R.string.consumer_key));
        settings.put(Shaker.JIRA_CALLBACK, getString(R.string.callback));
        settings.put(Shaker.JIRA_PRIVATE_KEY, getString(R.string.private_key));
        Shaker.initialize(this, settings);
    }
}
