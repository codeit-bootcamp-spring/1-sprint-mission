package com.sprint.mission.discodeit.entity;

public class Channel extends Base{
    private String channelName;
    private String description;

    public Channel(String channelName, String description) {
        super();
        this.channelName = channelName;
        this.description = description;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        setUpdatedAt();
    }

    public void updateDescription(String description) {
        this.description = description;
        setUpdatedAt();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
