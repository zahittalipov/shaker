package com.angelectro.shakerdetection.data.interactor.jira;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.JiraService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.jira.ProjectModel;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;

import java.util.List;

import rx.Observable;



public class GetJiraProjectListUseCase extends UseCase<List<ProjectModel>> {
    private JiraService mJiraService;
    private ApplicationPreferences mPreferences;

    public GetJiraProjectListUseCase() {
        mPreferences = new ApplicationPreferencesImpl();
        mJiraService = ApiFactory.getJiraService(mPreferences);
    }

    @Override
    protected Observable<List<ProjectModel>> buildUseCaseObservable() {
        return mJiraService.getProject();
    }
}
