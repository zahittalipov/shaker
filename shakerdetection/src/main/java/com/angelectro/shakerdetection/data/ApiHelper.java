package com.angelectro.shakerdetection.data;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Загит Талипов on 19.04.2017.
 */

public class ApiHelper {
    private static String API_SLACK_URL = "https://slack.com/api/";


    public static SlackService getSlackService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_SLACK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(SlackService.class);
    }
}
