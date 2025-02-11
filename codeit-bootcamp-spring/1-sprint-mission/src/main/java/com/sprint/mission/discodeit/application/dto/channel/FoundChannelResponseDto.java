package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record FoundChannelResponseDto(
        UUID channelId,
        String channelName,
        LocalDateTime lastMessageTime,
        Set<UUID> participatedUserId
) {
    
    public static FoundChannelResponseDto ofPublicChannel(Channel channel, LocalDateTime lastMessageTime) {
        return new FoundChannelResponseDto(channel.getId(), channel.getName(), lastMessageTime, Set.of());
    }

    public static FoundChannelResponseDto ofPrivateChannel(Channel channel, LocalDateTime lastMessageTime) {
        return new FoundChannelResponseDto(channel.getId(), channel.getName(), lastMessageTime,
                channel.getParticipantUserId());
    }
}
