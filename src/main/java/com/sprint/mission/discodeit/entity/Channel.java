package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
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


}
