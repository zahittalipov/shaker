package com.angelectro.shakerdetection.ui.settingslack.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;
import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.action.Action;
import com.angelectro.shakerdetection.data.model.Channel;
import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.angelectro.shakerdetection.data.model.SlackChannelResponse;
import com.angelectro.shakerdetection.ui.settingslack.presenter.SettingsSlackPresenter;
import com.angelectro.shakerdetection.ui.settingslack.view.SettingsSlackView;
import com.angelectro.shakerdetection.utils.PrefUtils;
import com.angelectro.shakerdetection.utils.UiUtils;

import static com.angelectro.shakerdetection.data.DataManager.CLIENT_ID_SLACK;
import static com.angelectro.shakerdetection.data.DataManager.REDIRECT_URI_SLACK;
import static com.angelectro.shakerdetection.data.DataManager.SCOPE_SLACK;
import static com.angelectro.shakerdetection.data.DataManager.TEAM_SLACK;

/**
 * Created by Загит Талипов on 23.04.2017.
 */

public class SettingsSlackActivity extends AppCompatActivity implements SettingsSlackView {


    WebView mWebView;
    SettingsSlackPresenter mPresenter;
    private MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_slack);
        init();
    }

    private void init() {
        SlackAuthData authData = PrefUtils.Slack.getAuthData(this);
        mPresenter = new SettingsSlackPresenter(this);
        mPresenter.attachView(this);
        if (authData == null) {
            mWebView = (WebView) findViewById(R.id.web_view);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new SlackWebViewClient());
            mWebView.loadUrl(String.format(getString(R.string.slack_authorize),
                    CLIENT_ID_SLACK,
                    SCOPE_SLACK,
                    REDIRECT_URI_SLACK,
                    TEAM_SLACK));
        } else {
            showLoading();
            mPresenter.getChannelsList(getApplicationContext());
        }
    }

    @Override
    public void authResult(SlackAuthData response) {
        mPresenter.getChannelsList(this);
    }

    @Override
    public void showChannelsList(SlackChannelResponse body) {
        hideLoading();
        UiUtils.showSingleChoiceChannelDialog(this, body.getChannels(), new Action<Channel>() {
            @Override
            public void call(Channel channel) {
                PrefUtils.Slack.saveChannel(getApplicationContext(), channel.getId());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void showError(Throwable throwable) {
        hideLoading();
        throwable.printStackTrace();
        finish();
        setResult(RESULT_CANCELED);
    }

    @Override
    public void showLoading() {
        mProgressDialog = UiUtils.showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    private class SlackWebViewClient extends WebViewClient {

        private boolean mCodeReceived;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(REDIRECT_URI_SLACK)) {
                Uri uri = Uri.parse(url);
                if (!mCodeReceived) {
                    String code = uri.getQueryParameter("code");
                    mCodeReceived = true;
                    requestAccessCode(code);
                }
                return true;
            }
            return false;
        }
    }

    private void requestAccessCode(String code) {
        showLoading();
        mPresenter.getAccessToken(code);
    }
}
