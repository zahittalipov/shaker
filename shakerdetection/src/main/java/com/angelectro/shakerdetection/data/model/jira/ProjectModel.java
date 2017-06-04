package com.angelectro.shakerdetection.data.model.jira;

import com.google.gson.annotations.SerializedName;



public class ProjectModel {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
