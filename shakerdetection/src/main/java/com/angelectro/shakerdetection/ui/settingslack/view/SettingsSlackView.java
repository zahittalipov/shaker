package com.angelectro.shakerdetection.ui.settingslack.view;

import com.angelectro.shakerdetection.base.MvpView;
import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.angelectro.shakerdetection.data.model.SlackChannelResponse;

/**
 * Created by Загит Талипов on 23.04.2017.
 */

public interface SettingsSlackView extends MvpView{

    void authResult(SlackAuthData response);

    void showChannelsList(SlackChannelResponse body);
}
