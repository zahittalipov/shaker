package com.angelectro.shakerdetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.angelectro.shakerdetection.data.DataManager;
import com.angelectro.shakerdetection.detector.ShakeDetector;
import com.angelectro.shakerdetection.model.InformationLog;
import com.angelectro.shakerdetection.ui.activity.CreateIssueActivity;
import com.angelectro.shakerdetection.utils.AppUtils;

import java.io.InputStream;
import java.util.Properties;


public class Shaker implements ShakeDetector.OnShakeListener {

    private static final String TAG = "ShakerLOG";
    private Context mContext;

    private Shaker(Context context) {
        mContext = context;
    }


    public static void initialize(Context context) {
        Shaker shaker = new Shaker(context);
        shaker.readProperties(context);
        shaker.startDetection();
    }

    private void readProperties(Context context) {
        try {
            Properties properties = new Properties();
            String fileName = "settings_shaker.properties";
            InputStream inputStream = context.getAssets().open(fileName);
            properties.load(inputStream);
            DataManager.CLIENT_ID_SLACK = properties.getProperty("slack_client_id");
            DataManager.CLIENT_SECRET_SLACK = properties.getProperty("slack_client_secret");
            DataManager.REDIRECT_URI_SLACK = properties.getProperty("slack_redirect_uri");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
