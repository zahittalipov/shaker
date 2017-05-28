package com.angelectro.shakerdetection.data.interactor.slack;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.SlackService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.slack.SlackAccessTokenResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;

import rx.Observable;


public class GetSlackAccessTokenUseCase extends UseCase<SlackAccessTokenResponse> {

    private String code;
    private SlackService mSlackService;
    private ApplicationPreferences mPreferences;

    public GetSlackAccessTokenUseCase() {
        mPreferences = new ApplicationPreferencesImpl();
        mSlackService = ApiFactory.getSlackService(mPreferences);
    }

    public GetSlackAccessTokenUseCase setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    protected Observable<SlackAccessTokenResponse> buildUseCaseObservable() {
        SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
        return mSlackService.getAccessToken(slackAuthData.getClientId(),
                slackAuthData.getClientSecret(),
                code,
                slackAuthData.getRedirectUri())
                .map(slackAuthDataResponse -> {
                    slackAuthData.setToken(slackAuthDataResponse.getAccessToken());
                    mPreferences.getSlackAuthModel().set(slackAuthData);
                    return slackAuthDataResponse;
                });
    }
}
