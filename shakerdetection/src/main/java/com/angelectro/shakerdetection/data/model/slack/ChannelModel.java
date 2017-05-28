package com.angelectro.shakerdetection.data.model.slack;

import com.google.gson.annotations.SerializedName;


public class ChannelModel {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("is_member")
    boolean isMember;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }
}
