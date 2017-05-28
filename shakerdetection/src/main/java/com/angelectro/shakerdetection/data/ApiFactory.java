package com.angelectro.shakerdetection.data;


import com.angelectro.shakerdetection.data.interceptor.JiraInterceptor;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiFactory {

    private static SlackService slackService;
    private static JiraService jiraService;

    public static SlackService getSlackService(ApplicationPreferences preferences) {
        if (slackService == null) {
            slackService = new Retrofit.Builder()
                    .baseUrl(preferences.getSlackAuthModel().get().getBaseUrl())
                    .client(getClientSlack())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build().create(SlackService.class);

        }
        return slackService;
    }

    public static JiraService getJiraService(ApplicationPreferences applicationPreferences) {
        if (jiraService == null) {
            jiraService = new Retrofit.Builder()
                    .baseUrl(applicationPreferences.getJiraAuthModel().get().getBaseUrl())
                    .client(getClientJira(applicationPreferences))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build().create(JiraService.class);
        }
        return jiraService;
    }

    private static OkHttpClient getClientJira(ApplicationPreferences applicationPreferences) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(new JiraInterceptor(applicationPreferences))
                .addNetworkInterceptor(interceptor)
                .build();
    }

    private static OkHttpClient getClientSlack() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
    }
}
