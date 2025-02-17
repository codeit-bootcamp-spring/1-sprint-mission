package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BaseEntity;
import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.ChannelGroup;
import com.sprint.mission.discodeit.dto.entity.ReadStatus;

import lombok.Getter;

import java.time.Instant;

@Getter
public class PublicChannelDto extends BaseEntity {
    private String channelName;
    private String description;
    private ChannelGroup channelGroup;

    public PublicChannelDto(Channel channel) {
        this.channelGroup = channel.getChannelGroup();
        this.channelName = channel.getChannelName();
        this.description = channel.getDescription();
    }
}
