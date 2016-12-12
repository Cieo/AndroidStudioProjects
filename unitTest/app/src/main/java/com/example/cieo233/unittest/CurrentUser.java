package com.example.cieo233.unittest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 12/4/2016.
 */

class CurrentUser {
    private static CurrentUser currentUser;
    private User user;
    private List<Channel> unsubscribeChannels;
    private List<Channel> creatorChannels;
    private List<Channel> subscribeChannels;
    private List<Reminder> reminders;

    public List<Channel> getAllChannels() {
        List<Channel> allChannel = new ArrayList<>();
        allChannel.addAll(unsubscribeChannels);
        allChannel.addAll(subscribeChannels);
        allChannel.addAll(creatorChannels);
        return allChannel;
    }

    List<Channel> getUnsubscribeChannels() {
        return unsubscribeChannels;
    }

    void setUnsubscribeChannels(List<Channel> unsubscribeChannels) {
        this.unsubscribeChannels = unsubscribeChannels;
    }

    List<Channel> getCreatorChannels() {
        return creatorChannels;
    }

    void setCreatorChannels(List<Channel> creatorChannels) {
        this.creatorChannels = creatorChannels;
    }

    public List<Channel> getSubscribeChannels() {
        return subscribeChannels;
    }

    void setSubscribeChannels(List<Channel> subscribeChannels) {
        this.subscribeChannels = subscribeChannels;
    }

    List<Reminder> getReminders() {
        return reminders;
    }

    void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    private CurrentUser() {
        unsubscribeChannels = new ArrayList<>();
        subscribeChannels = new ArrayList<>();
        creatorChannels = new ArrayList<>();
        reminders = new ArrayList<>();
    }

    static CurrentUser getInstance() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }
}
