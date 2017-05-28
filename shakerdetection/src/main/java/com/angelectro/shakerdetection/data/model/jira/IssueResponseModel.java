package com.angelectro.shakerdetection.data.model.jira;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Загит Талипов on 28.05.2017.
 */

public class IssueResponseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("self")
    private String self;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }
}
