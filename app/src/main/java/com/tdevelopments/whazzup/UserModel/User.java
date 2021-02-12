package com.tdevelopments.whazzup.UserModel;

public class User {

    String UserId , UserName , phoneNumber ,  UserProfilePic , userAbout ;

    public User () {}

    public User(String userId, String userName, String phoneNumber, String userProfilePic, String userAbout) {
        UserId = userId;
        UserName = userName;
        this.phoneNumber = phoneNumber;
        UserProfilePic = userProfilePic;
        this.userAbout = userAbout;
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

    public String getUserProfilePic() {
        return UserProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        UserProfilePic = userProfilePic;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }
}
