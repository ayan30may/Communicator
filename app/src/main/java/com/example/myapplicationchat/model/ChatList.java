package com.example.myapplicationchat.model;

public class ChatList {
    private String userId;
    private String name;
    private String message;
    private String description;
    private String imageProfile;

    public ChatList(String userId, String name, String message, String description, String imageProfile) {
        this.userId = userId;
        this.name = name;
        this.message = message;
        this.description = description;
        this.imageProfile = imageProfile;
    }

    public ChatList() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
