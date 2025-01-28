package com.sprint.misson.discordeit.dto;

import com.sprint.misson.discordeit.entity.ChannelType;

public class ChannelDTO {

    //채널명
    private String channelName;
    //채널 공개 여부

    private ChannelType channelType;

    private String description;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
