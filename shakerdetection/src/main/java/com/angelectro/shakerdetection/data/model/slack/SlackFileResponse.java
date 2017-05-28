package com.angelectro.shakerdetection.data.model.slack;


import com.google.gson.annotations.SerializedName;

public class SlackFileResponse extends SlackMessageResponse {

    @SerializedName("file")
    private SlackFileModel file;

    public void setFile(SlackFileModel file) {
        this.file = file;
    }

    public SlackFileModel getFile() {
        return file;
    }
}
