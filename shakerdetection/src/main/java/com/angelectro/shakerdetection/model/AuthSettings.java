package com.angelectro.shakerdetection.model;

public class AuthSettings {
    private String slackClientId;
    private String slackClientSecret;
    private String slackRedirectUri;

    public String getSlackClientId() {
        return slackClientId;
    }

    public void setSlackClientId(String slackClientId) {
        this.slackClientId = slackClientId;
    }

    public String getSlackClientSecret() {
        return slackClientSecret;
    }

    public void setSlackClientSecret(String slackClientSecret) {
        this.slackClientSecret = slackClientSecret;
    }

    public String getSlackRedirectUri() {
        return slackRedirectUri;
    }

    public void setSlackRedirectUri(String slackRedirectUri) {
        this.slackRedirectUri = slackRedirectUri;
    }
}
