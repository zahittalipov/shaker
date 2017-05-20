package com.angelectro.shakerdetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.angelectro.shakerdetection.data.DataManager;
import com.angelectro.shakerdetection.detector.ShakeDetector;
import com.angelectro.shakerdetection.model.InformationLog;
import com.angelectro.shakerdetection.model.AuthSettings;
import com.angelectro.shakerdetection.ui.activity.CreateIssueActivity;
import com.angelectro.shakerdetection.utils.AppUtils;

import java.util.Map;


public class Shaker implements ShakeDetector.OnShakeListener {

    public static final String SLACK_CLIENT_SECRET = "SLACK_CLIENT_SECRET";
    public static final String SLACK_CLIENT_ID = "SLACK_CLIENT_ID";
    public static final String SLACK_REDIRECT_URI = "SLACK_REDIRECT_URI";
    private Context mContext;

    private Shaker(Context context) {
        mContext = context;
    }


    public static void initialize(@NonNull Context context, @NonNull Map<String, String> settings) {
        Shaker shaker = new Shaker(context);
        shaker.readSettings(settings);
        shaker.startDetection();
    }

    private void readSettings(Map<String, String> settingsMap) {
        AuthSettings settings = new AuthSettings();
        settings.setSlackClientId(settingsMap.get(SLACK_CLIENT_ID));
        settings.setSlackClientSecret(settingsMap.get(SLACK_CLIENT_SECRET));
        settings.setSlackRedirectUri(settingsMap.get(SLACK_REDIRECT_URI));
        DataManager.setSettings(settings);

    }

    private void startDetection() {
        ShakeDetector.create(mContext, this);
        ShakeDetector.start();
    }


    @Override
    public void OnShake() {
        Activity activity = AppUtils.getActivity();
        if (activity != null && !TextUtils.equals(activity.getClass().getPackage().toString(), CreateIssueActivity.class.getPackage().toString()))
            takeScreenshot(activity);
    }


    private void takeScreenshot(Activity activity) {
        try {
            InformationLog information = new InformationLog(
                    AppUtils.getScreenshot(activity),
                    AppUtils.getVersionCode(activity),
                    AppUtils.getVersionName(activity),
                    AppUtils.getLogs(),
                    AppUtils.getAndroidVersion(),
                    AppUtils.getDeviceName(),
                    AppUtils.getPackageName(activity),
                    AppUtils.getApplicationName(activity));
            Intent intent = new Intent(activity, CreateIssueActivity.class);
            intent.putExtra(CreateIssueActivity.INFO_BUNDLE, information);
            activity.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
