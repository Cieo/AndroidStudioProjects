package com.example.cieo233.unittest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 12/4/2016.
 */

class CurrentUser {
    private static CurrentUser currentUser;
    private String token;
    private User user;
    private List<Channel> channels;
    private List<Reminder> reminders;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public CurrentUser(){
        channels = new ArrayList<>();
        reminders = new ArrayList<>();
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
