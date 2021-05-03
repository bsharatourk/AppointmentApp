package com.bshara.appointmentapp;

public class User {


    String fullName;
    String phoneNum;

    public User(String fullName, String phoneNum) {
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

}
