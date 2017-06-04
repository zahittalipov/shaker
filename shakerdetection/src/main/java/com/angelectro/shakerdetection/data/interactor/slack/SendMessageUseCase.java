package com.angelectro.shakerdetection.data.interactor.slack;

import android.graphics.Bitmap;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.SlackService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.model.slack.SlackMessageResponse;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.angelectro.shakerdetection.data.model.InformationLog;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import rx.Observable;

import static com.angelectro.shakerdetection.data.interactor.slack.FileUploadUseCase.JPEG_FORMAT;
import static com.angelectro.shakerdetection.data.interactor.slack.FileUploadUseCase.TEXT_FORMAT;



public class SendMessageUseCase extends UseCase<SlackMessageResponse> {
    private SlackService mSlackService;
    private ApplicationPreferences mPreferences;
    private InformationLog mInformationLog;
    private String jiraIssueKey;
    private String messagePattern;
    private FileUploadUseCase mScreenUploadUseCase;
    private FileUploadUseCase mLogUploadUseCase;

    public SendMessageUseCase() {
        mPreferences = new ApplicationPreferencesImpl();
        mSlackService = ApiFactory.getSlackService(mPreferences);
        mScreenUploadUseCase = new FileUploadUseCase();
        mLogUploadUseCase = new FileUploadUseCase();
    }

    public SendMessageUseCase setInformation(InformationLog informationLog,
                                             String jiraIssueUrl,
                                             String messagePattern) {
        mInformationLog = informationLog;
        this.jiraIssueKey = jiraIssueUrl;
        this.messagePattern = messagePattern;
        return this;
    }

    @Override
    public Observable<SlackMessageResponse> buildUseCaseObservable() {
        SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
        return Observable.just(mInformationLog.getBitmap())
                .map(bitmap -> {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    return stream.toByteArray();
                })
                .flatMap(bytes -> mScreenUploadUseCase.setData(bytes, JPEG_FORMAT).buildUseCaseObservable())
                .zipWith(mLogUploadUseCase.setData(mInformationLog.getLogs().getBytes(), TEXT_FORMAT).buildUseCaseObservable(),
                        (slackFileResponse, slackFileResponse2) -> mSlackService.postMessage(
                                slackAuthData.getToken(),
                                slackAuthData.getChannelId(),
                                mInformationLog.getComment(),
                                getAttachment(slackFileResponse.getFile().getUrlPrivate(),
                                        slackFileResponse2.getFile().getUrlPrivate())
                        )).flatMap(observable -> observable);

    }

    private String getAttachment(String urlPrivateScreen, String urlPrivateLog) {
        if (jiraIssueKey == null)
            return String.format(messagePattern,
                    mInformationLog.getDeviceName(),
                    mInformationLog.getAndroidVersion(),
                    urlPrivateLog,
                    mInformationLog.getProjectName(),
                    mInformationLog.getVersionName(),
                    mInformationLog.getVersionCode(),
                    urlPrivateScreen,
                    String.valueOf(new Date().getTime() / 1000),
                    urlPrivateScreen
            );
        return String.format(messagePattern,
                mInformationLog.getDeviceName(),
                mInformationLog.getAndroidVersion(),
                urlPrivateLog,
                mInformationLog.getProjectName(),
                mInformationLog.getVersionName(),
                mInformationLog.getVersionCode(),
                urlPrivateScreen,
                String.valueOf(new Date().getTime() / 1000),
                urlPrivateScreen,
                String.format("%s%s%s",mPreferences.getJiraAuthModel().get().getBaseUrl(),"/browse/", jiraIssueKey)
        );
    }
}
