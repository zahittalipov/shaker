package com.angelectro.shakerdetection.feature.settingjira;

import android.net.Uri;

import com.angelectro.shakerdetection.base.BasePresenter;
import com.angelectro.shakerdetection.data.interactor.internal.PresentationDefaultSubscriber;
import com.angelectro.shakerdetection.data.interactor.jira.GetJiraAccessTokenUseCase;
import com.angelectro.shakerdetection.data.interactor.jira.GetJiraProjectListUseCase;
import com.angelectro.shakerdetection.data.interactor.jira.GetJiraRequestTokenUseCase;
import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;


public class SettingsJiraPresenter extends BasePresenter<SettingsJiraView> {
    private static final String QUERY_CODE = "oauth_verifier";
    private ApplicationPreferences mPreferences;
    private GetJiraRequestTokenUseCase mGetRequestTokenUseCase;
    private GetJiraAccessTokenUseCase mGetAccessTokenUseCase;
    private GetJiraProjectListUseCase mGetProjectListUseCase;

    public SettingsJiraPresenter() {
        mPreferences = new ApplicationPreferencesImpl();
        mGetRequestTokenUseCase = new GetJiraRequestTokenUseCase();
        mGetAccessTokenUseCase = new GetJiraAccessTokenUseCase();
        mGetProjectListUseCase = new GetJiraProjectListUseCase();
    }


    @Override
    public void attachView(SettingsJiraView mvpView) {
        super.attachView(mvpView);
        mvpView.getAccessTokenAction(url -> {
            Uri uri = Uri.parse(url);
            String code = uri.getQueryParameter(QUERY_CODE);
            JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
            jiraAuthModel.setSecret(code);
            mPreferences.getJiraAuthModel().set(jiraAuthModel);
            mGetAccessTokenUseCase.execute(PresentationDefaultSubscriber.onPositive(mvpView.getProgressView(),
                    mvpView::resultGetAccessToken));
        });
        mvpView.getProjectListAction(() -> mGetProjectListUseCase.execute(PresentationDefaultSubscriber.onPositive(mvpView.getProgressView(), mvpView::showProjects)));
        mGetRequestTokenUseCase.execute(PresentationDefaultSubscriber.onPositive(mvpView.getProgressView(), mvpView::resultGetRequestToken));
    }

}
