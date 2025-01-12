package com.sprint.mission.entity;


public class Channel {
    private String channelName;
    private long createdAt;
    private User user;

    public Channel(User user, String channelName) {
        this.user = user;
        this.channelName = channelName;
        this.createdAt = System.currentTimeMillis();
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

    @Override
    public String toString() {
        return "Channel{" + "user='" + user + '\'' +
                ", channelName='" + channelName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
