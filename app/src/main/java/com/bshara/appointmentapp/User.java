package com.bshara.appointmentapp;

public class User {

    String userId;
    String userEmail;
    String fullName;
    String phoneNum;
    String password;

    public User(String userId, String fullName,String userEmail, String phoneNum, String password) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public  String getUserEmail(){
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPassword() {
        return password;
    }
}
