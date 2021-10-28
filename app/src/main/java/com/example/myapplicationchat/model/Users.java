package com.example.myapplicationchat.model;

public class Users {
    private String name;
    private String about;
    private String userId;
    private String imageProfile;
    private String phoneNumber;

    public Users() {
    }

    public Users(String name, String about, String userId, String imageProfile, String phoneNumber) {
        this.name = name;
        this.about = about;
        this.userId = userId;
        this.imageProfile = imageProfile;
        this.phoneNumber = phoneNumber;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
