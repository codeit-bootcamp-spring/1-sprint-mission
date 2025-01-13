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
        this.updateAt = System.currentTimeMillis();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{" + "user='" + user + '\'' +
                ", channelName='" + channelName + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
