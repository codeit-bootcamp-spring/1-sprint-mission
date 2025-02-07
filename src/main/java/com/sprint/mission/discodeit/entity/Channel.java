package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ChannelType type;
    private String channelName;
    private String description;
    private final UUID userId;

    public Channel(ChannelType type,String channelName, String description, UUID userId) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
        this.userId = userId;
    }

    public void update(String channelName, String description) {
        boolean flag=false;
        if(channelName!=null&&!channelName.equals(this.channelName)){
            this.channelName = channelName;
            flag=true;
        }
        if(description!=null&&!description.equals(this.description)){
            this.description = description;
            flag=true;
        }
        if(flag){
            update();
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channel='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
