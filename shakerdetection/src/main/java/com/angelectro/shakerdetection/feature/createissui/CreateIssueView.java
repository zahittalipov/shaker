package com.angelectro.shakerdetection.feature.createissui;

import com.angelectro.shakerdetection.base.MvpView;
import com.angelectro.shakerdetection.base.progress.ProgressView;
import com.angelectro.shakerdetection.data.model.InformationLog;

import rx.functions.Action4;


public interface CreateIssueView extends MvpView {

    void sendResult(Boolean ok);
    void showError(Throwable throwable);
    ProgressView getProgressView();
    void sendLogAction(Action4<InformationLog,Boolean,Boolean,String> action4);
}
