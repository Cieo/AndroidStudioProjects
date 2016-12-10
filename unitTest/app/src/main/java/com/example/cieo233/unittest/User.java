package com.example.cieo233.unittest;

/**
 * Created by Cieo233 on 12/4/2016.
 */

class User {
    private String username, password;
    private int id;
    private String token;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";




    public User(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public User(String username, String password, int id, String token) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.token = token;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(String username, int id) {
        this.username = username;
        this.id = id;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
