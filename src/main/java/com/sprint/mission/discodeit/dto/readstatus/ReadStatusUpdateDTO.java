package com.sprint.mission.discodeit.dto.readstatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public record ReadStatusUpdateDTO(
        UUID id,
        Instant channelLastReadTimes
)
{
}
