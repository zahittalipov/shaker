package com.angelectro.shakerdetection.data.interactor.composite;

import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.interactor.jira.CreateIssueUseCase;
import com.angelectro.shakerdetection.data.interactor.slack.SendMessageUseCase;
import com.angelectro.shakerdetection.model.InformationLog;

import rx.Observable;



public class CreateIssueAndSendMessageUseCase extends UseCase<Boolean> {

    private CreateIssueUseCase mCreateIssueUseCase;
    private SendMessageUseCase mSendMessageUseCase;
    private String title;
    private String messagePattern;
    private InformationLog mInformationLog;

    public CreateIssueAndSendMessageUseCase() {
        mCreateIssueUseCase = new CreateIssueUseCase();
        mSendMessageUseCase = new SendMessageUseCase();
    }

    public CreateIssueAndSendMessageUseCase setMessagePattern(String messagePattern) {
        this.messagePattern = messagePattern;
        return this;
    }

    public CreateIssueAndSendMessageUseCase setTitle(String title) {
        this.title = title;
        return this;
    }

    public CreateIssueAndSendMessageUseCase setInformationLog(InformationLog informationLog) {
        mInformationLog = informationLog;
        return this;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return mCreateIssueUseCase.setInformationLog(mInformationLog)
                .setTitle(title).buildUseCaseObservable()
                .flatMap(issueResponseModel -> mSendMessageUseCase.setInformation(mInformationLog, issueResponseModel.getKey(), messagePattern).buildUseCaseObservable())
                .map(response -> response.getOk());
    }
}
