package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
    private Instant createdAt;
    private Instant updatedAt;
}
