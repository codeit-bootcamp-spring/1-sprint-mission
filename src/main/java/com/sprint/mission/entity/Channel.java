package com.sprint.mission.entity;


public class Channel {
    private String channelName;
    private long createdAt;
    private User user;
    private long updateAt;

    public Channel(User user, String channelName) {
        this.user = user;
        this.channelName = channelName;
        this.createdAt = System.currentTimeMillis();
        this.updateAt = this.createdAt;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public long getCreatedAt() {
        return createdAt;
    }

}
