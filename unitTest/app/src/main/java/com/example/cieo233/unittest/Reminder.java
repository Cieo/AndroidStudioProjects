package com.example.cieo233.unittest;

import java.io.Serializable;

/**
 * Created by Cieo233 on 12/4/2016.
 */

class Reminder implements Serializable {
    private Channel channel;
    private String title, content, due, remark, last_update;
    private int id, priority, type, creatorID, state;

    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DUE = "due";
    public static final String TYPE = "type";
    public static final String CHANNEL_ID = "channel_id";
    public static final String PRIORITY = "priority";
    public static final String REMARK = "remark";
    public static final String STATE = "state";

    public Reminder(Channel channel, String title, String content, String due, String remark, String last_update, int id, int priority, int type, int creatorID, int state) {
        this.channel = channel;
        this.title = title;
        this.content = content;
        this.due = due;
        this.remark = remark;
        this.last_update = last_update;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.creatorID = creatorID;
        this.state = state;
    }

    public Reminder(String title, String content, String due, String remark, String last_update, int id, int priority, int type, int creatorID, int state) {
        this.title = title;
        this.content = content;
        this.due = due;
        this.remark = remark;
        this.last_update = last_update;
        this.id = id;
        this.priority = priority;
        this.type = type;
        this.creatorID = creatorID;
        this.state = state;
    }

    public Reminder(String title, String content, String due, int priority, int type, Channel channel) {
        this.title = title;
        this.content = content;
        this.due = due;
        this.priority = priority;
        this.type = type;
        this.channel = channel;
    }

    public Reminder(String title, String content, String due, int priority, int type) {
        this.title = title;
        this.content = content;
        this.due = due;
        this.priority = priority;
        this.type = type;
    }

    public Reminder(Channel channel, String title, String content, String due, int priority, int type) {
        this.channel = channel;
        this.title = title;
        this.content = content;
        this.due = due;
        this.priority = priority;
        this.type = type;
    }

    public Reminder(String title, String content, int priority, int type) {
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.type = type;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
