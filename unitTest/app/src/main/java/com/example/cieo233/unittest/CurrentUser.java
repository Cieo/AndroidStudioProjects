package com.example.cieo233.unittest;

/**
 * Created by Cieo233 on 12/4/2016.
 */

class CurrentUser {
    private static CurrentUser currentUser;
    private String token;
    private User user;

    public CurrentUser(){

    }

    public static CurrentUser getInstance() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
