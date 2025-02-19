package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.util.type.ChannelFormat;
import com.sprint.mission.discodeit.util.type.ChannelType;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class PublicChannel extends Channel {
    private String name;
    private String description;
    private ChannelFormat channelFormat;
    private ChannelType channelType;

    public PublicChannel(String name, String description, ChannelFormat channelFormat, List<UUID> joinMember) {
        super(joinMember);
        this.name = name;
        this.description = description;
        this.channelFormat = channelFormat;
        this.channelType = ChannelType.PUBLIC;
    }

    public void update(String name, String description, UUID newUserID) {
        this.name = name;
        this.description = description;
        super.getJoinMembers().add(newUserID);
    }
}
