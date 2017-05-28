package com.angelectro.shakerdetection.data.interactor.jira;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.JiraService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.http.UrlEncodedParser;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;



public class GetJiraAccessTokenUseCase extends UseCase<Boolean> {

    private JiraService mJiraService;
    private ApplicationPreferences mPreferences;

    public GetJiraAccessTokenUseCase() {
        mPreferences = new ApplicationPreferencesImpl();
        mJiraService = ApiFactory.getJiraService(mPreferences);
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            Response<ResponseBody> execute = mJiraService.getAccessToken().execute();
            return execute.body().string();
        })
                .map(s -> {
                    OAuthCredentialsResponse oauthResponse = new OAuthCredentialsResponse();
                    UrlEncodedParser.parse(s, oauthResponse);
                    return oauthResponse;
                })
                .map(oAuthCredentialsResponse -> {
                    JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
                    jiraAuthModel.setAccessToken(oAuthCredentialsResponse.token);
                    mPreferences.getJiraAuthModel().set(jiraAuthModel);
                    return jiraAuthModel.getAccessToken() != null;
                });
    }
}
