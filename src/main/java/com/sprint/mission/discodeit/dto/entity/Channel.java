package com.sprint.mission.discodeit.dto.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class Channel extends BaseEntity implements Serializable {
    @NotEmpty
    private String channelName;
    private String description;
    private ChannelGroup channelGroup;
    private UUID madeUserId;

    public Channel(String channelName, String description, ChannelGroup channelGroup) {
        super();
        this.channelGroup = channelGroup;
        if (channelGroup.equals("PUBLIC")) {
            this.channelName = channelName;
            this.description = description;
            this.madeUserId = Participant.getUser
        }
    }
    public Channel(ChannelGroup channelGroup) {
        super();
        this.channelGroup = channelGroup;
    }


    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
