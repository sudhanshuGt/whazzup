package com.tdevelopments.whazzup.UserModel;

public class User {

    String userId , userName , phoneNumber ,  userProfilePic , userAbout ;

    public User () {}

    public User(String userId, String userName, String phoneNumber, String userProfilePic, String userAbout) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userProfilePic = userProfilePic;
        this.userAbout = userAbout;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }
}
