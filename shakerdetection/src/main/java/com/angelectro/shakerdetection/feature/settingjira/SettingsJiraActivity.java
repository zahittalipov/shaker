package com.angelectro.shakerdetection.feature.settingjira;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;
import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.base.progress.ProgressView;
import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.model.jira.ProjectModel;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.angelectro.shakerdetection.utils.UiUtils;

import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;


public class SettingsJiraActivity extends AppCompatActivity implements SettingsJiraView {


    private SettingsJiraPresenter mSettingsJiraPresenter;
    private ApplicationPreferences mPreferences;
    private WebView mWebView;
    private Action1<String> mGetAccessTokenAction;
    private Action0 mActionGetProjectList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = new ApplicationPreferencesImpl();
        setContentView(R.layout.activity_auth);
        mWebView = (WebView) findViewById(R.id.web_view);
        initWebView();
        mSettingsJiraPresenter = new SettingsJiraPresenter();
        mSettingsJiraPresenter.attachView(this);
    }

    @Override
    public void resultGetRequestToken(boolean confirmed) {
        if (confirmed) {
            JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
            mWebView.loadUrl(String.format(getString(R.string.auth_jira), jiraAuthModel.getBaseUrl(), jiraAuthModel.getAccessToken()));
        }
    }

    @Override
    public void resultGetAccessToken(boolean ok) {
        if (ok && mPreferences.getJiraAuthModel().get().getProjectId() == null) {
            mActionGetProjectList.call();
        } else if (ok) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void showProjects(List<ProjectModel> projectModels) {
        UiUtils.showSingleChoiceDialog(this, R.string.select_project, projectModels, projectModel -> {
            JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
            jiraAuthModel.setProjectId(projectModel.getId());
            mPreferences.getJiraAuthModel().set(jiraAuthModel);
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public ProgressView getProgressView() {
        return (ProgressView) findViewById(R.id.progressContainer);
    }

    @Override
    public void getAccessTokenAction(Action1<String> stringAction1) {
        mGetAccessTokenAction = stringAction1;
    }

    @Override
    public void getProjectListAction(Action0 action0) {
        mActionGetProjectList = action0;
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new JiraWebViewClient());
    }


    private class JiraWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
            if (url.startsWith(jiraAuthModel.getCallback())) {
                mGetAccessTokenAction.call(url);
                return true;
            }
            return false;
        }
    }
}
