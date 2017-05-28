package com.angelectro.shakerdetection.feature.settingslack;

import com.angelectro.shakerdetection.base.MvpView;
import com.angelectro.shakerdetection.base.progress.ProgressView;
import com.angelectro.shakerdetection.data.model.slack.SlackAccessTokenResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackChannelResponse;

import rx.functions.Action1;


interface SettingsSlackView extends MvpView {

    void authResult(SlackAccessTokenResponse response);

    void requestTokenAction(Action1<String> stringAction);

    void showChannelList(SlackChannelResponse body);

    ProgressView getProgressView();
}
