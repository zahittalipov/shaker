package com.angelectro.shakerdetection.data.interactor.jira;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.JiraService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.jira.CreateIssueBodyModel;
import com.angelectro.shakerdetection.data.model.jira.IssueResponseModel;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.angelectro.shakerdetection.model.InformationLog;

import rx.Observable;



public class CreateIssueUseCase extends UseCase<IssueResponseModel> {

    private ApplicationPreferences mApplicationPreferences;
    private JiraService mJiraService;
    private String title;
    private InformationLog mInformationLog;

    public CreateIssueUseCase() {
        mApplicationPreferences = new ApplicationPreferencesImpl();
        mJiraService = ApiFactory.getJiraService(mApplicationPreferences);
    }

    public CreateIssueUseCase setTitle(String title) {
        this.title = title;
        return this;
    }

    public CreateIssueUseCase setInformationLog(InformationLog informationLog) {
        mInformationLog = informationLog;
        return this;
    }

    @Override
    public Observable<IssueResponseModel> buildUseCaseObservable() {
        CreateIssueBodyModel model = new CreateIssueBodyModel(title, mInformationLog.getComment(), mApplicationPreferences.getJiraAuthModel().get().getProjectId());
        return mJiraService.createIssue(model);
    }
}
