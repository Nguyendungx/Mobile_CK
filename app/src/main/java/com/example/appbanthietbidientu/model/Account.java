package com.example.appbanthietbidientu.model;

public class Account {

    String email;
    String userid;

    int role; // role 1 = admin , role 2 = user

    public Account(String email, String userid, int role) {
        this.email = email;
        this.userid = userid;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return userid;
    }

    public int getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String userid) {
        this.userid = userid;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
