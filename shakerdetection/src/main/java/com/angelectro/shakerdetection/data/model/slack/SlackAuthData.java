package com.angelectro.shakerdetection.data.model.slack;


public class SlackAuthData {
    private static String SCOPE_SLACK = "channels:write,files:write:user,chat:write:user,channels:read";
    private String channelId;
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String token;

    public SlackAuthData(String apiSlackUrlDefault) {
        baseUrl = apiSlackUrlDefault;
    }

    public String getScopeSlack() {
        return SCOPE_SLACK;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null)
            this.channelId = channelId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        if (baseUrl != null)
            this.baseUrl = baseUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
