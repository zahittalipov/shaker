package com.angelectro.shakerdetection.data.model.slack;

import com.google.gson.annotations.SerializedName;


public class SlackAccessTokenResponse {
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("access_token")
    private String accessToken;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
