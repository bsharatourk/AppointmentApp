package com.bshara.appointmentapp;

public class User {


    String userEmail;
    String fullName;
    String phoneNum;
    String password;

    public User(String fullName,String userEmail, String phoneNum, String password) {
        this.userEmail = userEmail;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPassword() {
        return password;
    }
}
