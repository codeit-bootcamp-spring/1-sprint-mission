package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BaseEntity;
import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.ChannelGroup;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
public class PublicChannelDto extends BaseEntity {
    private final UUID id;
    private final Instant createdAt;
    private String channelName;
    private String description;
    private ChannelGroup channelGroup;

    public PublicChannelDto(Channel channel) {
        this.id = channel.getId();
        this.createdAt=channel.getCreatedAt();
        this.channelName = channel.getChannelName();
        this.description = channel.getDescription();
        this.channelGroup = channel.getChannelGroup();
    }
}
