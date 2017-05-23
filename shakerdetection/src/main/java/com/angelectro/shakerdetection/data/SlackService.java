package com.angelectro.shakerdetection.data;

import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.angelectro.shakerdetection.data.model.SlackChannelResponse;
import com.angelectro.shakerdetection.data.model.SlackFileResponse;
import com.angelectro.shakerdetection.data.model.SlackResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface SlackService {


    @GET("oauth.access")
    Observable<SlackAuthData> getAccessToken(@Query("client_id") String clientId,
                                             @Query("client_secret") String clientSecret,
                                             @Query("code") String code,
                                             @Query("redirect_uri") String redirectUri);

    @Multipart
    @POST("files.upload")
    Observable<SlackFileResponse> upload(@Query("token") String token,
                                         @PartMap Map<String, RequestBody> params,
                                         @Query("filename") String filename, @Query("title") String title,
                                         @Query("initial_comment") String initialComment/*, @Query("channels") String channels*/);

    @GET("chat.postMessage")
    Observable<SlackResponse> postMessage(@Query("token") String token,
                                          @Query("channel") String channel,
                                          @Query("text") String text,
                                          @Query(value = "attachments") String attachments);


    @GET("channels.list")
    Observable<SlackChannelResponse> getChannelsList(
            @Query("token") String token,
            @Query("exclude_archived") boolean excludeArchived
    );
}
