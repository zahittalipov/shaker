package com.angelectro.shakerdetection.data.interactor.slack;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.SlackService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.model.slack.SlackChannelResponse;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;

import rx.Observable;

/**
 * Created by Загит Талипов on 28.05.2017.
 */

public class GetSlackChannelListUseCase extends UseCase<SlackChannelResponse> {
    private SlackService mSlackService;
    private ApplicationPreferences mPreferences;

    public GetSlackChannelListUseCase() {
        mPreferences = new ApplicationPreferencesImpl();
        mSlackService = ApiFactory.getSlackService(mPreferences);
    }

    @Override
    protected Observable<SlackChannelResponse> buildUseCaseObservable() {
        SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
        return mSlackService.getChannelsList(slackAuthData.getToken(), true);
    }
}
