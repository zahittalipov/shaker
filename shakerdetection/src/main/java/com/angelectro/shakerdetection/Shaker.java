package com.angelectro.shakerdetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.angelectro.shakerdetection.data.model.InformationLog;
import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.angelectro.shakerdetection.feature.createissui.CreateIssueActivity;
import com.angelectro.shakerdetection.feature.detector.ShakeDetector;
import com.angelectro.shakerdetection.utils.AppUtils;
import com.orhanobut.hawk.Hawk;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Shaker {

    public static final String SLACK_CLIENT_SECRET = "SLACK_CLIENT_SECRET";
    public static final String SLACK_CLIENT_ID = "SLACK_CLIENT_ID";
    public static final String SLACK_CHANNEL_ID = "SLACK_CHANNEL_ID";
    public static final String SLACK_BASE_URL = "SLACK_BASE_URL";
    public static final String SLACK_REDIRECT_URI = "SLACK_REDIRECT_URI";
    public static final String JIRA_CONSUMER_KEY = "JIRA_CONSUMER_KEY";
    public static final String JIRA_PRIVATE_KEY = "JIRA_PRIVATE_KEY";
    public static final String JIRA_BASE_URL = "JIRA_BASE_URL";
    public static final String JIRA_PROJECT_KEY = "JIRA_PROJECT_KEY";
    public static final String JIRA_CALLBACK = "JIRA_CALLBACK";
    public static Context context;
    ApplicationPreferences mPreferences;

    private Shaker(Context context) {
        Shaker.context = context;
        mPreferences = new ApplicationPreferencesImpl();
    }


    public static void initialize(@NonNull Context context, @NonNull Map<String, String> settings) {
        Hawk.init(context).build();
        Shaker shaker = new Shaker(context);
        shaker.readSettings(settings);
        shaker.startDetection();
    }

    private void readSettings(Map<String, String> settingsMap) {
        JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
        jiraAuthModel.setBaseUrl(settingsMap.get(JIRA_BASE_URL));
        jiraAuthModel.setConsumerKey(settingsMap.get(JIRA_CONSUMER_KEY));
        jiraAuthModel.setPrivateKey(settingsMap.get(JIRA_PRIVATE_KEY));
        jiraAuthModel.setCallback(settingsMap.get(JIRA_CALLBACK));
        jiraAuthModel.setProjectId(settingsMap.get(JIRA_PROJECT_KEY));
        mPreferences.getJiraAuthModel().set(jiraAuthModel);
        SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
        slackAuthData.setBaseUrl(settingsMap.get(SLACK_BASE_URL));
        slackAuthData.setChannelId(settingsMap.get(SLACK_CHANNEL_ID));
        slackAuthData.setClientId(settingsMap.get(SLACK_CLIENT_ID));
        slackAuthData.setRedirectUri(settingsMap.get(SLACK_REDIRECT_URI));
        slackAuthData.setClientSecret(settingsMap.get(SLACK_CLIENT_SECRET));
        mPreferences.getSlackAuthModel().set(slackAuthData);

    }

    private void startDetection() {
        ShakeDetector.createAndStart(context, o -> {
            Activity activity = AppUtils.getActivity();
            if (activity != null &&
                    !TextUtils.equals(activity.getClass().getPackage().toString(),
                            CreateIssueActivity.class.getPackage().toString())) {
                Observable.fromCallable(() -> collectInformation(activity))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(informationLog -> {
                            if (informationLog != null) {
                                Intent intent = new Intent(activity, CreateIssueActivity.class);
                                intent.putExtra(CreateIssueActivity.INFO_BUNDLE, informationLog);
                                activity.startActivity(intent);
                            } else {
                                Toast.makeText(context, context.getString(R.string.collect_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }


    private InformationLog collectInformation(Activity activity) {
        try {
            return new InformationLog(
                    AppUtils.getScreenshot(activity),
                    AppUtils.getVersionCode(activity),
                    AppUtils.getVersionName(activity),
                    AppUtils.getLogs(),
                    AppUtils.getAndroidVersion(),
                    AppUtils.getDeviceName(),
                    AppUtils.getPackageName(activity),
                    AppUtils.getApplicationName(activity));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
