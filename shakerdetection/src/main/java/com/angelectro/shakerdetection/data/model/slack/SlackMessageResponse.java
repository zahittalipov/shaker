package com.angelectro.shakerdetection.data.model.slack;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Загит Талипов on 24.04.2017.
 */

public class SlackMessageResponse {
    @SerializedName("ok")
    private Boolean ok;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

}
