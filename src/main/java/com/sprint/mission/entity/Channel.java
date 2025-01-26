package com.sprint.mission.entity;


import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Channel implements Serializable {
    private String channelName;
    private Long createdAt;
    private User user;
    private Long updateAt;

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


    @Override
    public String toString() {
        return "\n채널 : " + channelName
                + "\n유저 : " + user;
    }
}
