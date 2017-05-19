package com.angelectro.shakerdetection.ui.settingslack.presenter;

import android.content.Context;

import com.angelectro.shakerdetection.base.BasePresenter;
import com.angelectro.shakerdetection.data.DataManager;
import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.angelectro.shakerdetection.data.model.SlackChannelResponse;
import com.angelectro.shakerdetection.ui.settingslack.view.SettingsSlackView;
import com.angelectro.shakerdetection.utils.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Загит Талипов on 23.04.2017.
 */

public class SettingsSlackPresenter extends BasePresenter<SettingsSlackView> {

    DataManager mDataManager;
    private Context mContext;


    public SettingsSlackPresenter(Context context) {
        mContext = context;
        mDataManager = DataManager.getInstance();
    }

    public void getAccessToken(String code) {
        mDataManager.getAccessToken(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(slackAuthData -> {
                    PrefUtils.Slack.saveAuthData(mContext, slackAuthData);
                    return slackAuthData;
                })
                .flatMap(slackAuthData -> mDataManager.getChannelsList(slackAuthData.getAccessToken()))
                .subscribe(getMvpView()::showChannelsList,getMvpView()::showError);
    }

    public void getChannelsList(Context context){
        SlackAuthData slackAuthData = PrefUtils.Slack.getAuthData(context);
        mDataManager.getChannelsList(slackAuthData.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::showChannelsList,getMvpView()::showError);
    }
}
