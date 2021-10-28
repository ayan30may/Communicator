package com.example.myapplicationchat.model;

public class ContactList {
    private String userId;
    private String name;
    private String about;
    private String imageProfile;

    public ContactList() {
    }

    public ContactList(String userId, String userName, String about, String imageProfile) {
        this.userId = userId;
        this.name = userName;
        this.about = about;
        this.imageProfile = imageProfile;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
