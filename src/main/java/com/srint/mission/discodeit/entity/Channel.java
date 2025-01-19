package com.srint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity{

    private String channelName;

    private final UUID userId;


    public Channel(UUID userId, String channelName) {
        super();
        this.userId = userId;
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelname='" + channelName + '\'' +
                ", userId=" + userId +
                '}'+"  "+ super.toString();
    }
}
