package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MessageResponse(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static MessageResponse EntityToDto(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .userId(message.getUserId())
                .channelId(message.getChannelId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
