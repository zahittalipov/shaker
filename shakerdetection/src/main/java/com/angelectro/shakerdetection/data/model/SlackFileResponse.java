package com.angelectro.shakerdetection.data.model;

/**
 * Created by Загит Талипов on 23.04.2017.
 */

public class SlackFileResponse extends SlackResponse{

    private SlackFile file;

    public void setFile(SlackFile file) {
        this.file = file;
    }

    public SlackFile getFile() {
        return file;
    }
}
