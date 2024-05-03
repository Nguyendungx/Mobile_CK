package com.example.appbanthietbidientu.model;

public class Account {

    String email;
    String uid;
    int role; // role 1 = admin , role 2 = user

    public Account(String email, String uid, int role) {
        this.email = email;
        this.uid = uid;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public int getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
