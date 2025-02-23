package com.sprint.mission.discodeit.web.dto;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelGroup;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


@Getter
public class PublicChannelDto {
    private UUID id;
    // @NotEmpty
    private String channelName;
    private String description;
    private ChannelGroup group;
    private Instant lastMessageTime;

    public PublicChannelDto(Channel channel) {
        this.id = channel.getId();
        this.channelName = channel.getChannelName();
        this.description = channel.getDescription();
        this.group = channel.getGroup();
    }
    public PublicChannelDto(Channel channel, Instant lastMessageTime) {
        this.id = channel.getId();
        this.channelName = channel.getChannelName();
        this.description = channel.getDescription();
        this.group = channel.getGroup();
        this.lastMessageTime=lastMessageTime;
    }

    @Override
    public String toString() {
        return "PublicChannelDto{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", group=" + group +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }
}
