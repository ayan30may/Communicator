package com.example.myapplicationchat.model;

public class CallList {
    private String userId;
    private String userName;
    private String data;
    private String imageUrl;
    private String callType;

    public CallList() {
    }

    public CallList(String userId, String userName, String data, String imageUrl, String callType) {
        this.userId = userId;
        this.userName = userName;
        this.data = data;
        this.imageUrl = imageUrl;
        this.callType = callType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
