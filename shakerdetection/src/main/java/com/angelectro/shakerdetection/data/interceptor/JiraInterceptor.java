package com.angelectro.shakerdetection.data.interceptor;

import com.angelectro.shakerdetection.data.model.jira.JiraAuthModel;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.utils.RsaUtil;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class JiraInterceptor implements Interceptor {

    ApplicationPreferences mPreferences;

    public JiraInterceptor(ApplicationPreferences preferences) {
        mPreferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        OAuthParameters oAuthParameters = new OAuthParameters();
        JiraAuthModel jiraAuthModel = mPreferences.getJiraAuthModel().get();
        String authHeader = null;
        try {
            GenericUrl genericUrl = new GenericUrl();
            genericUrl.setHost(chain.request().url().host());
            genericUrl.setScheme(chain.request().url().scheme());
            List<String> parts = new ArrayList<>();
            parts.add("");
            parts.addAll(chain.request().url().pathSegments());
            genericUrl.setPathParts(parts);
            genericUrl.setPort(chain.request().url().port());
            oAuthParameters.signer = RsaUtil.getOAuthRsaSigner(jiraAuthModel.getPrivateKey());
            oAuthParameters.consumerKey = jiraAuthModel.getConsumerKey();
            oAuthParameters.token = jiraAuthModel.getAccessToken();
            oAuthParameters.verifier = jiraAuthModel.getSecret();
            oAuthParameters.callback = jiraAuthModel.getCallback();
            oAuthParameters.computeNonce();
            oAuthParameters.computeTimestamp();
            oAuthParameters.computeSignature(chain.request().method(), genericUrl);
            authHeader = oAuthParameters.getAuthorizationHeader();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Request authorization = chain.request().newBuilder().addHeader("Authorization", authHeader).build();
        return chain.proceed(authorization);
    }
}
