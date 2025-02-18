package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.common.Name;
import com.sprint.mission.discodeit.enums.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ChannelDTO {

    @Builder
    public record request(UUID owner, String serverName, String description, ChannelType channelType,
                          List<UUID> members, Instant recent) {
    }

    @Builder
    public record response(Long id, UUID uuid, UUID ownerId, Name serverName, String description,
                           List<UUID> members,
                           Instant recentMessageTime) {
    }

    @Builder
    public record update(Long id, UUID owner, String serverName, String description, ChannelType channelType,
                         List<UUID> members, Instant recent) {

    }
}
