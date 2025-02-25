package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ReadStatusResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        UUID channelId
) {
    public static ReadStatusResponse EntityToDto(ReadStatus readStatus) {
        return ReadStatusResponse.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .createdAt(readStatus.getCreatedAt())
                .updatedAt(readStatus.getUpdatedAt())
                .build();
    }
}
