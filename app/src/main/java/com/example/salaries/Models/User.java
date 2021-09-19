package com.example.salaries.Models;

public class User {
    String authUid;
    String userName;
    String companyName;
    String mobile;
    String email;

    public User() {
    }

    public User(String authUid, String userName, String companyName, String mobile, String email) {
        this.authUid = authUid;
        this.userName = userName;
        this.companyName = companyName;
        this.mobile = mobile;
        this.email = email;
    }

    public String getAuthUid() {
        return authUid;
    }

    public void setAuthUid(String authUid) {
        this.authUid = authUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
