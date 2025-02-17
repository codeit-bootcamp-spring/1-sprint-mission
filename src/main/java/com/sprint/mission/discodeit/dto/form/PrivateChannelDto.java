package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BaseEntity;
import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.ChannelGroup;
import com.sprint.mission.discodeit.dto.entity.ReadStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


@Getter
public class PrivateChannelDto{

    private UUID id;
    private Instant createdAt;
    private ReadStatus readStatus;

    private ChannelGroup channelGroup;

    public PrivateChannelDto(Channel channel) {
        this.id=channel.getId();
        this.createdAt=channel.getCreatedAt();
        this.channelGroup = channel.getChannelGroup();
    }
}
