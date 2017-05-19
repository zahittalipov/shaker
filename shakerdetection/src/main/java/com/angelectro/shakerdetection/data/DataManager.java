package com.angelectro.shakerdetection.data;

import android.content.Context;

import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.angelectro.shakerdetection.data.model.SlackChannelResponse;
import com.angelectro.shakerdetection.data.model.SlackFileResponse;
import com.angelectro.shakerdetection.data.model.SlackResponse;
import com.angelectro.shakerdetection.model.InformationLog;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;


public class DataManager {
    public static String CLIENT_ID_SLACK;
    public static String CLIENT_SECRET_SLACK;
    public static String SCOPE_SLACK = "channels:write,files:write:user,chat:write:user,channels:read";
    public static String REDIRECT_URI_SLACK;
    public static String TEAM_SLACK = "1";
    private SlackService mSlackService;
    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        mSlackService = ApiHelper.getSlackService();
    }

    public Observable<SlackFileResponse> uploadScreenshot(SlackAuthData data, byte[] fileBytes) {
        String filename = UUID.randomUUID().toString() + ".jpg";
        RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data"), fileBytes);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\"" + filename + "\"", file);
        return mSlackService.upload(data.getAccessToken(), map, filename, "test", "text");
    }

    public Observable<SlackFileResponse> uploadLogs(SlackAuthData data, String logs) {
        String filename = UUID.randomUUID().toString() + ".txt";
        RequestBody file = RequestBody.create(MediaType.parse("multipart/form-data"), logs.getBytes());

        Map<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\"" + filename + "\"", file);
        return mSlackService.upload(data.getAccessToken(), map, filename, "test", "text");
    }

    public Observable<SlackAuthData> getAccessToken(String code) {
        return mSlackService.getAccessToken(CLIENT_ID_SLACK,
                CLIENT_SECRET_SLACK,
                code,
                REDIRECT_URI_SLACK);
    }

    public Observable<SlackResponse> postMessage(Context context, SlackAuthData data, InformationLog informationLog, String channel,
                                                 String logUrl, String screenUri) {
        String attachment = String.format(context.getString(R.string.text_body_json),
                informationLog.getDeviceName(),
                informationLog.getAndroidVersion(),
                logUrl,
                informationLog.getProjectName(),
                informationLog.getVersionName(),
                informationLog.getVersionCode(),
                screenUri,
                String.valueOf(new Date().getTime() / 1000),
                screenUri
        );
        return mSlackService.postMessage(data.getAccessToken(),
                channel,
                informationLog.getComment(),
                attachment
        );
    }


    public Observable<SlackChannelResponse> getChannelsList(String token) {
        return mSlackService.getChannelsList(token, true);
    }
}
