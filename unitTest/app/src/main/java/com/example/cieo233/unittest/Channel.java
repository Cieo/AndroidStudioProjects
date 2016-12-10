package com.example.cieo233.unittest;

/**
 * Created by Cieo233 on 12/4/2016.
 */

class Channel {
    private int id, type;
    private String name, last_update;
    public static final String ACTION = "action";
    public static final int UNSUBSCRIBE = 0;
    public static final int EXIT = 1;
    public static final int JOIN = 0;
    public static final int CREATOR = 1;
    public static final int SUBSCRIBE = 2;
    public static final String NAME = "name";


    public Channel(int id, int type, String name, String last_update) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.last_update = last_update;
    }

    public Channel(int id, int type, String last_update) {
        this.id = id;
        this.type = type;
        this.last_update = last_update;
    }

    public Channel(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }
}
