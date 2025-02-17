package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.time.Instant;

public record ReadStatusResponseDto(
        String channelId,
        String userId,
        Instant updateAt
) {
    public ReadStatusResponseDto from(ReadStatus readStatus) {
        return new ReadStatusResponseDto(
                readStatus.getChannelId(),
                readStatus.getUserId(),
                readStatus.getUpdatedAt()
        );
    }
}
