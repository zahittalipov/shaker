package com.angelectro.shakerdetection.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Загит Талипов on 03.05.2017.
 */

public class SlackChannelResponse extends SlackResponse {
    @SerializedName("channels")
    List<Channel> mChannels;

    public List<Channel> getChannels() {
        return mChannels;
    }

    public void setChannels(List<Channel> channels) {
        mChannels = channels;
    }


}
