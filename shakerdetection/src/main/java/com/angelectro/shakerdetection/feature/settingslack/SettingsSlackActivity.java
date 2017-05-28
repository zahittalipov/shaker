package com.angelectro.shakerdetection.feature.settingslack;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.base.progress.ProgressView;
import com.angelectro.shakerdetection.data.model.slack.SlackAccessTokenResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackAuthData;
import com.angelectro.shakerdetection.data.model.slack.SlackChannelResponse;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;
import com.angelectro.shakerdetection.utils.UiUtils;

import rx.functions.Action1;




public class SettingsSlackActivity extends AppCompatActivity implements SettingsSlackView {

    private static String QUERY_CODE = "code";
    private WebView mWebView;
    private SettingsSlackPresenter mPresenter;
    private ApplicationPreferences mPreferences;
    private Action1<String> mActionRequestToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mPreferences = new ApplicationPreferencesImpl();
        mPresenter = new SettingsSlackPresenter();
        mPresenter.attachView(this);
        initView();
    }

    private void initView() {
        SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
        if (slackAuthData.getToken() == null) {
            mWebView = (WebView) findViewById(R.id.web_view);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new SlackWebViewClient());
            mWebView.loadUrl(String.format(getString(R.string.slack_authorize),
                    slackAuthData.getClientId(),
                    slackAuthData.getScopeSlack(),
                    slackAuthData.getRedirectUri(),
                    "1"));//fixme
        } else
            finish();
    }

    @Override
    public void authResult(SlackAccessTokenResponse response) {
        if (response.getAccessToken() != null) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void requestTokenAction(Action1<String> stringAction) {
        mActionRequestToken = stringAction;
    }


    @Override
    public void showChannelList(SlackChannelResponse body) {
        UiUtils.showSingleChoiceDialog(this, R.string.select_channel, body.getChannels(), channel -> {
            SlackAuthData slackAuthData = mPreferences.getSlackAuthModel().get();
            slackAuthData.setChannelId(channel.getId());
            mPreferences.getSlackAuthModel().set(slackAuthData);
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public ProgressView getProgressView() {
        return (ProgressView) findViewById(R.id.progressContainer);
    }


    private class SlackWebViewClient extends WebViewClient {

        private boolean mCodeReceived;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(mPreferences.getSlackAuthModel().get().getRedirectUri())) {
                Uri uri = Uri.parse(url);
                if (!mCodeReceived) {
                    mCodeReceived = true;
                    mActionRequestToken.call(uri.getQueryParameter(QUERY_CODE));
                }
                return true;
            }
            return false;
        }
    }
}
