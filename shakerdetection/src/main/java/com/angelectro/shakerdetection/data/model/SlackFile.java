package com.angelectro.shakerdetection.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Загит Талипов on 23.04.2017.
 */

public class SlackFile {
    private String id;
    private Integer created;
    private Integer timestamp;
    private String name;
    private String title;
    @SerializedName("url_private")
    private String urlPrivate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlPrivate() {
        return urlPrivate;
    }

    public void setUrlPrivate(String urlPrivate) {
        this.urlPrivate = urlPrivate;
    }
}
