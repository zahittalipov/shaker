package com.angelectro.shakerdetection.data.model.slack;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Загит Талипов on 03.05.2017.
 */

public class SlackChannelResponse extends SlackMessageResponse {
    @SerializedName("channels")
    private List<ChannelModel> mChannels;

    public List<ChannelModel> getChannels() {
        return mChannels;
    }

    public void setChannels(List<ChannelModel> channels) {
        mChannels = channels;
    }


}
