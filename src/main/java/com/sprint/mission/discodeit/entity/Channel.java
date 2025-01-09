package com.sprint.mission.discodeit.entity;

public class Channel extends Entity {
    private String channelName;
    public Channel(String channelName){
        super();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateName(String channelName) {
        this.channelName = channelName;
        setUpdatedAt(System.currentTimeMillis());

    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                '}';
    }
}
