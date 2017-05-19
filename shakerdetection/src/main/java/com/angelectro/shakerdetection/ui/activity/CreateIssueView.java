package com.angelectro.shakerdetection.ui.activity;

import com.angelectro.shakerdetection.base.MvpView;
import com.angelectro.shakerdetection.model.InformationLog;

/**
 * Created by Загит Талипов on 10.04.2017.
 */

public interface CreateIssueView extends MvpView {

    void sendResult(Boolean ok);
}
