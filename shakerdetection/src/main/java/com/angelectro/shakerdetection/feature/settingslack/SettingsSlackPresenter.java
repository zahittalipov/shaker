package com.angelectro.shakerdetection.feature.settingslack;

import com.angelectro.shakerdetection.base.BasePresenter;
import com.angelectro.shakerdetection.data.interactor.internal.PresentationDefaultSubscriber;
import com.angelectro.shakerdetection.data.interactor.slack.GetSlackAccessTokenUseCase;
import com.angelectro.shakerdetection.data.interactor.slack.GetSlackChannelListUseCase;
import com.angelectro.shakerdetection.data.model.slack.SlackAccessTokenResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;


public class SettingsSlackPresenter extends BasePresenter<SettingsSlackView> {


    private ApplicationPreferences mPreferences;

    private GetSlackAccessTokenUseCase mGetSlackAccessTokenUseCase;
    private GetSlackChannelListUseCase mGetSlackChannelListUseCase;

    public SettingsSlackPresenter() {
        mPreferences = new ApplicationPreferencesImpl();
        mGetSlackAccessTokenUseCase = new GetSlackAccessTokenUseCase();
        mGetSlackChannelListUseCase = new GetSlackChannelListUseCase();
    }

    @Override
    public void attachView(SettingsSlackView mvpView) {
        super.attachView(mvpView);
        mvpView.requestTokenAction(code -> mGetSlackAccessTokenUseCase.setCode(code).execute(PresentationDefaultSubscriber.<SlackAccessTokenResponse>onPositive(mvpView.getProgressView(),
                slackAccessTokenResponse -> {
                    SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
                    if (slackAuthData.getChannelId() == null) {
                        mGetSlackChannelListUseCase.execute(PresentationDefaultSubscriber.onPositive(mvpView.getProgressView(),
                                mvpView::showChannelList));
                    } else {
                        mvpView.authResult(slackAccessTokenResponse);
                    }
                })));
    }
}
