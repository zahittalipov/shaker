package com.angelectro.shakerdetection.data;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Загит Талипов on 19.04.2017.
 */

public interface ApiService {


    @Multipart
    @POST("rest/files/upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description, @Part MultipartBody.Part file);
}
