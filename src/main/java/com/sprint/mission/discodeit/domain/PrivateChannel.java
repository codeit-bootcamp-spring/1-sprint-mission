package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.util.type.ChannelFormat;
import com.sprint.mission.discodeit.util.type.ChannelType;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class PrivateChannel extends Channel { //
    private ChannelFormat channelFormat;
    private ChannelType channelType;

    public PrivateChannel(List<UUID> joinMember, ChannelFormat channelFormat) {
        super(joinMember);
        this.channelFormat = channelFormat;
        this.channelType = ChannelType.PRIVATE;
    }
}
