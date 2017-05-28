package com.angelectro.shakerdetection.feature.createissui;

import android.content.Context;
import android.text.TextUtils;

import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.base.BasePresenter;
import com.angelectro.shakerdetection.data.interactor.composite.CreateIssueAndSendMessageUseCase;
import com.angelectro.shakerdetection.data.interactor.internal.PresentationDefaultSubscriber;
import com.angelectro.shakerdetection.data.interactor.jira.CreateIssueUseCase;
import com.angelectro.shakerdetection.data.interactor.slack.SendMessageUseCase;
import com.angelectro.shakerdetection.data.model.ViewStateModel;
import com.angelectro.shakerdetection.data.model.jira.IssueResponseModel;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.model.slack.SlackMessageResponse;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.angelectro.shakerdetection.exception.JiraUserNotAuthenticatedException;
import com.angelectro.shakerdetection.exception.SlackUserNotSettingsException;


public class CreateIssuePresenter extends BasePresenter<CreateIssueView> {
    private Context mContext;
    private ApplicationPreferences mPreferences;
    private CreateIssueUseCase mCreateIssueUseCase;
    private SendMessageUseCase mSendMessageUseCase;
    private CreateIssueAndSendMessageUseCase mCompositionUseCase;

    public CreateIssuePresenter(Context context) {
        mContext = context;
        mPreferences = new ApplicationPreferencesImpl();
        mCreateIssueUseCase = new CreateIssueUseCase();
        mSendMessageUseCase = new SendMessageUseCase();
        mCompositionUseCase = new CreateIssueAndSendMessageUseCase();
    }


    @Override
    public void attachView(CreateIssueView mvpView) {
        super.attachView(mvpView);
        mvpView.sendLogAction((informationLog, isSendSlack, isSendJira, titleIssue) -> {
            SlackAuthData mAuthData = mPreferences.getSlackAuthModel().get();
            if (isSendSlack && (TextUtils.isEmpty(mAuthData.getToken()) || TextUtils.isEmpty(mAuthData.getChannelId()))) {
                mvpView.showError(new SlackUserNotSettingsException());
                return;
            }
            if (isSendJira && mPreferences.getJiraAuthModel().get().getAccessToken() == null) {
                mvpView.showError(new JiraUserNotAuthenticatedException());
                return;
            }
            if (isSendJira && isSendSlack) {
                mCompositionUseCase.setInformationLog(informationLog)
                        .setTitle(titleIssue)
                        .setMessagePattern(mContext.getString(R.string.text_body_json_with_issue))
                        .execute(PresentationDefaultSubscriber.<Boolean>onPositive(mvpView.getProgressView(),
                                o -> mvpView.sendResult(o)));
            } else if (isSendSlack) {
                mSendMessageUseCase.setInformation(informationLog, null, mContext.getString(R.string.text_body_json))
                        .execute(PresentationDefaultSubscriber.<SlackMessageResponse>onPositive(mvpView.getProgressView(),
                                response -> mvpView.sendResult(response.getOk())));
            } else if (isSendJira) {
                mCreateIssueUseCase.setInformationLog(informationLog).setTitle(titleIssue).execute(PresentationDefaultSubscriber.<IssueResponseModel>onPositive(mvpView.getProgressView(),
                        issueResponseModel -> {
                            if (issueResponseModel.getSelf() != null)
                                mvpView.sendResult(true);
                        }));
            }
        });
    }

    public void saveSettings(boolean isSendSlack, boolean isSendJira) {
        ViewStateModel stateModel = new ViewStateModel();
        stateModel.setCheckedJira(isSendJira);
        stateModel.setCheckedSlack(isSendSlack);
        mPreferences.getViewState().set(stateModel);
    }

}
