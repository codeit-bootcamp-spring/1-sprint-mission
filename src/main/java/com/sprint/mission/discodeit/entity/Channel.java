package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends User{
    private String channelName;

    public Channel(UUID id, String userName, String channelName){
            super(id,userName);
            this.channelName = channelName;
    }

    public String getChannelName(){
        return channelName;
    }

    public void setChannelName(String newChannelName) {
        this.channelName = newChannelName;
    }

}
