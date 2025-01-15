package com.sprint.misson.discordeit.dto;

public class ChannelDTO {

    //채널명
    private String channelName;
    //채널 공개 여부
    private boolean isHidden;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setPublic(boolean isHidden) {
        this.isHidden = isHidden;
    }
}
