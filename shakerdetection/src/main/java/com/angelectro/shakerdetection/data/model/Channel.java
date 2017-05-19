package com.angelectro.shakerdetection.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Загит Талипов on 03.05.2017.
 */

public class Channel {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
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

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    @SerializedName("is_member")
    boolean isMember;
}
