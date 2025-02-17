package com.sprint.mission.discodeit.dto.form;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatusUpdateDto {
    private UUID userId;
    private UUID channelId;
    private Instant newLastReadAt;
}
