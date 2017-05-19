package com.angelectro.shakerdetection.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.base.BasePresenter;
import com.angelectro.shakerdetection.data.DataManager;
import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.angelectro.shakerdetection.data.model.SlackFileResponse;
import com.angelectro.shakerdetection.exception.SlackUserNotSettingsException;
import com.angelectro.shakerdetection.model.InformationLog;
import com.angelectro.shakerdetection.utils.PrefUtils;

import java.io.ByteArrayOutputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CreateIssuePresenter extends BasePresenter<CreateIssueView> {


    Context mContext;
    private SlackAuthData mAuthData;

    public CreateIssuePresenter(Context context) {
        mContext = context;
        mDataManager = DataManager.getInstance();
        mAuthData = PrefUtils.Slack.getAuthData(mContext);
    }

    DataManager mDataManager;

    void sendInfoSlack(@NonNull final InformationLog informationLog) {
        if (mAuthData == null || PrefUtils.Slack.getChannelId(mContext) == null) {
            getMvpView().showError(new SlackUserNotSettingsException());
            return;
        }
        Observable<SlackFileResponse> responseSendLogs = mDataManager.uploadLogs(mAuthData, informationLog.getLogs()).subscribeOn(Schedulers.io());
        Observable.just(informationLog)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(informationLog1 -> {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    informationLog1.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    return stream.toByteArray();
                })
                .flatMap(bytes -> mDataManager.uploadScreenshot(mAuthData, bytes))
                .zipWith(responseSendLogs, (slackFileResponse, slackFileResponse2) -> {
                    if (!slackFileResponse.getOk() || !slackFileResponse2.getOk()) {
                        getMvpView().showError(new Exception(mContext.getString(R.string.general_error)));
                    }
                    return mDataManager.postMessage(mContext,
                            mAuthData,
                            informationLog,
                            PrefUtils.Slack.getChannelId(mContext),
                            slackFileResponse2.getFile().getUrlPrivate(),
                            slackFileResponse.getFile().getUrlPrivate()
                    );
                })
                .flatMap(responseBodyObservable -> responseBodyObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(getMvpView()::showLoading)
                .doOnTerminate(getMvpView()::hideLoading)
                .subscribe(responseBody -> getMvpView().sendResult(responseBody.getOk()),getMvpView()::showError);

    }

}
