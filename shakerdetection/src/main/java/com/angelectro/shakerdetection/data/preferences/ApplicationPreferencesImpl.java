package com.angelectro.shakerdetection.data.preferences;

import android.support.annotation.Nullable;

import com.angelectro.shakerdetection.data.model.ViewStateModel;
import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.orhanobut.hawk.Hawk;

/**
 * Created by Загит Талипов on 27.05.2017.
 */

public class ApplicationPreferencesImpl implements ApplicationPreferences {
    private static final String JIRA_MODEL = "jira_model";
    private static final String SLACK_MODEL = "slack_model";
    private static final String VIEW_STATE = "view_state";
    private static String API_SLACK_URL_DEFAULT = "https://slack.com/api/";

    @Override
    public ObjectPref<JiraAuthModel> getJiraAuthModel() {
        return new ObjectPref<JiraAuthModel>() {
            @Override
            public JiraAuthModel get() {
                return Hawk.get(JIRA_MODEL, new JiraAuthModel());
            }

            @Override
            public void set(@Nullable JiraAuthModel value) {
                Hawk.put(JIRA_MODEL, value);
            }
        };
    }

    @Override
    public ObjectPref<SlackAuthData> getSlackAuthModel() {
        return new ObjectPref<SlackAuthData>() {
            @Override
            public SlackAuthData get() {
                return Hawk.get(SLACK_MODEL, new SlackAuthData(API_SLACK_URL_DEFAULT));
            }

            @Override
            public void set(@Nullable SlackAuthData value) {
                Hawk.put(SLACK_MODEL, value);
            }
        };

    }

    @Override
    public ObjectPref<ViewStateModel> getViewState() {
        return new ObjectPref<ViewStateModel>() {
            @Override
            public ViewStateModel get() {
                return Hawk.get(VIEW_STATE, new ViewStateModel());
            }

            @Override
            public void set(@Nullable ViewStateModel value) {
                Hawk.put(VIEW_STATE, value);
            }
        };
    }
}
