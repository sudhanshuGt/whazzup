package com.tdevelopments.whazzup.UserModel;

public class User {

    private String UserId, UserName, phoneNumber, profileUrl, userAbout;

    public User() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }

    public User(String userId, String userName, String phoneNumber, String profileUrl, String userAbout) {
        UserId = userId;
        UserName = userName;
        this.phoneNumber = phoneNumber;
        this.profileUrl = profileUrl;
        this.userAbout = userAbout;
    }
}