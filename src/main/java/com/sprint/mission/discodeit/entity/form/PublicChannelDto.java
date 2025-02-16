package com.sprint.mission.discodeit.entity.form;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelGroup;
import com.sprint.mission.discodeit.entity.ReadStatus;

import lombok.Getter;

import java.time.Instant;

@Getter
public class PublicChannelDto extends BaseEntity {
    private String channelName;
    private String description;
    private ReadStatus readStatus;
    private Instant recentMessageTime;
    private ChannelGroup channelGroup;

    public PublicChannelDto(Channel channel) {
        this.channelGroup = channel.getChannelGroup();
        this.channelName = channel.getChannelName();
        this.description = channel.getDescription();
        this.readStatus = channel.getReadStatus();

    }
}
