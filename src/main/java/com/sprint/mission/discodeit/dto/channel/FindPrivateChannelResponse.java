package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.channel.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindPrivateChannelResponse {
    private Instant recentMessageTime;
    private Set<UUID> channelMembersIds;
}
