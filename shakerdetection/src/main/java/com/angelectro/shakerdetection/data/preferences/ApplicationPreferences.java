package com.angelectro.shakerdetection.data.preferences;

import com.angelectro.shakerdetection.data.model.ViewStateModel;
import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.model.slack.SlackAccessTokenResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;



public interface ApplicationPreferences {

    ObjectPref<JiraAuthModel> getJiraAuthModel();
    ObjectPref<SlackAuthData> getSlackAuthModel();
    ObjectPref<ViewStateModel> getViewState();
}
