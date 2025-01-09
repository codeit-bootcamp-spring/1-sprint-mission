package com.srint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity{

    private String channelname;

    private final UUID userId;


    public Channel(UUID userId, String channelname) {
        super();
        this.userId = userId;
        this.channelname = channelname;
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelname='" + channelname + '\'' +
                ", userId=" + userId +
                '}'+"  "+ super.toString();
    }
}
