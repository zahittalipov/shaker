package com.angelectro.shakerdetection.data;


import com.angelectro.shakerdetection.data.model.jira.CreateIssueBodyModel;
import com.angelectro.shakerdetection.data.model.jira.IssueResponseModel;
import com.angelectro.shakerdetection.data.model.jira.ProjectModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface JiraService {

    @POST("plugins/servlet/oauth/request-token")
    Call<ResponseBody> requestToken();

    @POST("plugins/servlet/oauth/access-token")
    Call<ResponseBody> getAccessToken();

    @GET("rest/api/2/project")
    Observable<List<ProjectModel>> getProject();

    @POST("rest/api/2/issue")
    Observable<IssueResponseModel> createIssue(@Body CreateIssueBodyModel model);
}
