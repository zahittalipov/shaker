package com.angelectro.shakerdetection.data;

import com.angelectro.shakerdetection.data.model.slack.SlackAccessTokenResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackChannelResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackFileResponse;
import com.angelectro.shakerdetection.data.model.slack.SlackMessageResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;


public interface SlackService {


    @GET("oauth.access")
    Observable<SlackAccessTokenResponse> getAccessToken(@Query("client_id") String clientId,
                                                        @Query("client_secret") String clientSecret,
                                                        @Query("code") String code,
                                                        @Query("redirect_uri") String redirectUri);

    @Multipart
    @POST("files.upload")
    Observable<SlackFileResponse> upload(@Query("token") String token,
                                         @PartMap Map<String, RequestBody> params,
                                         @Query("filename") String filename, @Query("title") String title,
                                         @Query("initial_comment") String initialComment);

    @GET("chat.postMessage")
    Observable<SlackMessageResponse> postMessage(@Query("token") String token,
                                                 @Query("channel") String channel,
                                                 @Query("text") String text,
                                                 @Query(value = "attachments") String attachments);


    @GET("channels.list")
    Observable<SlackChannelResponse> getChannelsList(
            @Query("token") String token,
            @Query("exclude_archived") boolean excludeArchived
    );
}
