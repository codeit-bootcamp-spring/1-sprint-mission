package com.sprint.mission.discodeit.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class Channel extends BaseEntity implements Serializable {
    @NotEmpty
    private String channelName;
    private String description;
    private ChannelGroup channelGroup;
    private ReadStatus readStatus;

    public Channel(String channelName, String description, ChannelGroup channelGroup) {
        super();
        this.channelGroup = channelGroup;
        if (channelGroup.equals("PUBLIC")) {
            this.channelName = channelName;
            this.description = description;
        }
    }
    public Channel(ReadStatus readStatus,ChannelGroup channelGroup) {
        super();
        this.channelGroup = channelGroup;
        if (channelGroup.equals("PRIVATE")) {
            this.readStatus=readStatus;
        }
    }


    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
