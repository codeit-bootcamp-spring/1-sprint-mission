package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelResponseDto(UUID id, Channel.ChannelType type, String name, String description,
                                 List<UserResponseDto> participants, Instant lastMessageAt) {
}
