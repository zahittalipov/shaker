package com.angelectro.shakerdetection.feature.settingjira;

import com.angelectro.shakerdetection.base.MvpView;
import com.angelectro.shakerdetection.base.progress.ProgressView;
import com.angelectro.shakerdetection.data.model.jira.ProjectModel;

import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Загит Талипов on 26.05.2017.
 */

public interface SettingsJiraView extends MvpView {
    void resultGetRequestToken(boolean b);

    void resultGetAccessToken(boolean b);

    void showProjects(List<ProjectModel> projectModels);

    ProgressView getProgressView();

    void getAccessTokenAction(Action1<String>stringAction1);

    void getProjectListAction(Action0 action0);
}
