package com.spirnt.mission.discodeit.entity;

public class Channel extends BaseEntity{
    private String channelName;
    private String description;

    public Channel(String channelName, String description){
        this.channelName = channelName;
        this.description = description;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public void updateChannelName(String channelName){
        this.channelName = channelName;
        update();
    }

    public void updateDescription(String description) {
        this.description = description;
        update();
    }
}
