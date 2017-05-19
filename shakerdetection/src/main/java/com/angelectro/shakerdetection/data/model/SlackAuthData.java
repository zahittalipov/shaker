package com.angelectro.shakerdetection.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Загит Талипов on 23.04.2017.
 */

public class SlackAuthData {
    private Boolean ok;
    @SerializedName("access_token")
    private String accessToken;
    private String scope;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("team_name")
    private String teamName;
    @SerializedName("team_id")
    private String teamId;

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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

}
