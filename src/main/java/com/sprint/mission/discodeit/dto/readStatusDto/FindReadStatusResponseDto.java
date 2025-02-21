package com.sprint.mission.discodeit.dto.readStatusDto;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public class FindReadStatusResponseDto {
    UUID id;
    UUID userId;
    UUID channelId;
    Instant lastReadTime;

    public FindReadStatusResponseDto(ReadStatus readStatus) {
        this.id = readStatus.getId();
        this.userId = readStatus.getUserId();
        this.channelId = readStatus.getChannelId();
        this.lastReadTime = readStatus.getLastReadTime();
    }
}
