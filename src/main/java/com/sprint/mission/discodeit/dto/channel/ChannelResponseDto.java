package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        ChannelType type,
        String name,
        String introduction,
        List<UUID> users
) {
    public static ChannelResponseDto from(UUID id, ChannelType type, String name, String introduction, List<UUID> users) {
        return new ChannelResponseDto(id, type, name, introduction, users);
    }
}
