package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {
    public static ReadStatusResponse from(ReadStatus readStatus){
        return new ReadStatusResponse(
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }
}
